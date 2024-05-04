package net.vegatec.media_library.service;
import com.sun.nio.file.SensitivityWatchEventModifier;

import net.vegatec.media_library.config.ApplicationProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import static java.nio.file.StandardWatchEventKinds.*;

//@Service
public class RecursiveMediaFileMonitor {

    private static final Logger LOG = LoggerFactory.getLogger(RecursiveMediaFileMonitor.class);

//    @Value("${root.folder}")
    private File rootFolder;

    private WatchService watcher;

    private ExecutorService executor;

    private final ApplicationProperties applicationProperties;



    private final TrackService trackService;


    public RecursiveMediaFileMonitor(ApplicationProperties applicationProperties, TrackService trackService) {
        this.applicationProperties = applicationProperties;
        this.trackService = trackService;
        rootFolder = Paths.get(applicationProperties.getMediaFolder(), TrackService.DOWNLOADED).toFile();
    }

    @PostConstruct
    public void init() throws IOException {
        watcher = FileSystems.getDefault().newWatchService();
        executor = Executors.newSingleThreadExecutor();
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

    private void startRecursiveWatcher() throws IOException {
        LOG.info("Starting Recursive Watcher");

        final Map<WatchKey, Path> keys = new HashMap<>();

        Consumer<Path> register = p -> {
            if (!p.toFile().exists() || !p.toFile().isDirectory()) {
                throw new RuntimeException("folder " + p + " does not exist or is not a directory");
            }
            try {
                Files.walkFileTree(p, new SimpleFileVisitor<Path>() {
                    @Override
                    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                        LOG.info("registering " + dir + " in watcher service");
                        WatchKey watchKey = dir.register(watcher, new WatchEvent.Kind[]{ENTRY_CREATE, ENTRY_DELETE}, SensitivityWatchEventModifier.HIGH);
                        keys.put(watchKey, dir);
                        return FileVisitResult.CONTINUE;
                    }
                });
            } catch (IOException e) {
                throw new RuntimeException("Error registering path " + p);
            }
        };

        register.accept(rootFolder.toPath());

        executor.submit(() -> {
            while (true) {
                final WatchKey key;
                try {
                    key = watcher.take(); // wait for a key to be available
                } catch (InterruptedException ex) {
                    return;
                }

                final Path dir = keys.get(key);
                if (dir == null) {
                    System.err.println("WatchKey " + key + " not recognized!");
                    continue;
                }

                key.pollEvents().stream()
                    .filter(e -> (e.kind() != OVERFLOW))
                    .map(e -> ((WatchEvent<Path>) e))
                    .forEach(e -> {
                        final Path path=  e.context();
                        final Path absPath = dir.resolve(path);

                        if(e.kind() == ENTRY_DELETE) {
                            LOG.info("Folder " + absPath + " deleted");
                            WatchKey watchKey= keys.entrySet().stream().filter(k -> k.getValue().equals(absPath)).findFirst().get().getKey();
                            if (watchKey != null) {
                                watchKey.cancel();
                                keys.remove(watchKey);
                                LOG.info("Related key " + absPath + " cancelled");

                            }

                        } else   if (e.kind() == ENTRY_CREATE) {

                            if (absPath.toFile().isDirectory()) {

                                register.accept(absPath);

                            } else {
                                final File file = absPath.toFile();
                                LOG.info("Detected new file " + file.getAbsolutePath());
                                if (file.getName().toLowerCase().endsWith(".mp3"))
                                    try {
                                        trackService.importFile(file);
                                    } catch (Exception ex) {
                                        LOG.error("Error importing file " + file.getAbsolutePath(), ex);
                                    }
                                else
                                    file.delete();
                            }
                        }
                    });

                boolean valid = key.reset(); // IMPORTANT: The key must be reset after processed
                if (!valid) {
                    break;
                }
            }
        });
    }
}
