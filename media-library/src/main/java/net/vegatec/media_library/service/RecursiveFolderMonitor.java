package net.vegatec.media_library.service;

import net.vegatec.media_library.config.ApplicationProperties;
import net.vegatec.media_library.domain.Track;
import net.vegatec.media_library.service.events.FileCreated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.nio.file.LinkOption.NOFOLLOW_LINKS;
import static java.nio.file.StandardWatchEventKinds.*;


/**
 * https://docs.oracle.com/javase/tutorial/essential/io/examples/WatchDir.java
 */
@Service
public class RecursiveFolderMonitor {

    private static final Logger LOG = LoggerFactory.getLogger(RecursiveFolderMonitor.class);

//    @Value("${root.folder}")
    private Path rootFolder;

    private WatchService watcher;

    private  Map<WatchKey,Path> keys;

    private boolean trace = false;

    private ExecutorService executor;

    private final ApplicationProperties applicationProperties;


   // private PropertyChangeSupport support= new PropertyChangeSupport(this);

    private final ApplicationEventPublisher applicationEventPublisher;

    public RecursiveFolderMonitor(ApplicationProperties applicationProperties, ApplicationEventPublisher applicationEventPublisher) {
        this.applicationProperties = applicationProperties;
        rootFolder = Paths.get(applicationProperties.getMediaFolder(), Track.DOWNLOADED);
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @PostConstruct
    public void init() throws IOException {
        watcher = FileSystems.getDefault().newWatchService();
        executor = Executors.newSingleThreadExecutor();

        this.keys = new HashMap<WatchKey,Path>();

        startRecursiveWatcher();
    }

    @PreDestroy
    public void cleanup() {
        try {
            watcher.close();
        } catch (IOException e) {
            LOG.error("Error closing watcher service", e);
        }
        executor.shutdown();
    }

    /**
     * Register the given directory with the WatchService
     */
    private void register(Path dir) throws IOException {
        WatchKey key = dir.register(watcher, ENTRY_CREATE);
        if (trace) {
            Path prev = keys.get(key);
            if (prev == null) {
                System.out.format("register: %s\n", dir);
            } else {
                if (!dir.equals(prev)) {
                    System.out.format("update: %s -> %s\n", prev, dir);
                }
            }
        }
        keys.put(key, dir);
    }


    /**
     * Register the given directory, and all its sub-directories, with the
     * WatchService.
     */
    private void registerAll(final Path start) throws IOException {
        // register directory and sub-directories
        Files.walkFileTree(start, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
                throws IOException
            {
                register(dir);
                return FileVisitResult.CONTINUE;
            }
        });
    }


    @SuppressWarnings("unchecked")
    private static <T> WatchEvent<T> cast(WatchEvent<?> event) {
        return (WatchEvent<T>)event;
    }


    private void startRecursiveWatcher() throws IOException {
        LOG.info("Starting Recursive Watcher");

        LOG.info("Scanning %s ...\n", rootFolder);
        registerAll(rootFolder);
        LOG.info("Done.");


        // enable trace after initial registration
        this.trace = true;

        executor.execute(() -> {
            while(true) {

                // wait for key to be signalled
                WatchKey key;
                try {
                    key = watcher.take();
                } catch (InterruptedException x) {
                    return;
                }

                Path dir = keys.get(key);
                if (dir == null) {
                    LOG.error("WatchKey not recognized!!");
                    continue;
                }

                for (WatchEvent<?> event : key.pollEvents()) {
                    WatchEvent.Kind kind = event.kind();

                    // TBD - provide example of how OVERFLOW event is handled
                    if (kind == OVERFLOW) {
                        continue;
                    }

                    // Context for directory entry event is the file name of entry
                    WatchEvent<Path> ev = cast(event);
                    Path name = ev.context();
                    Path child = dir.resolve(name);

                    // print out event
                    System.out.format("%s: %s\n", event.kind().name(), child);

                    // if directory is created, and watching recursively, then
                    // register it and its sub-directories
                    if (kind == ENTRY_CREATE) {
                        try {
                            if (Files.isDirectory(child, NOFOLLOW_LINKS)) {
                                registerAll(child);
                            } else {
                                LOG.info("Detected new file " + child.toAbsolutePath());
                                File file = child.toFile();
                                if (file.getName().toLowerCase().endsWith(".mp3"))
                                    applicationEventPublisher.publishEvent(new FileCreated(this, file));

                            }
                        } catch (IOException x) {
                            // ignore to keep sample readable
                        }
                    }
                }

                // reset key and remove from set if directory no longer accessible
                boolean valid = key.reset();
                if (!valid) {
                    keys.remove(key);

                    // all directories are inaccessible
                    if (keys.isEmpty()) {
                        break;
                    }
                }
            }
        });
    }


}
