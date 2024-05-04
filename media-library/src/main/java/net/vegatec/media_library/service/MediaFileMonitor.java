package net.vegatec.media_library.service;

import com.mysql.cj.log.Log;
import com.sun.nio.file.ExtendedWatchEventModifier;
import net.vegatec.media_library.util.FileUtils;
import net.vegatec.media_library.util.ListDirectoryRecursively;
import org.slf4j.Logger;


import net.vegatec.media_library.config.ApplicationProperties;


import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

import static net.vegatec.media_library.util.FileUtils.listAllFiles;
import static org.apache.commons.io.FileUtils.listFiles;

//@Service
public class MediaFileMonitor implements Runnable {

    private final Logger logger = LoggerFactory.getLogger(MediaFileMonitor.class);
    private WatchService watchService;

    private final ApplicationProperties applicationProperties;

    private Thread thread;

    private final TrackService trackService;



    public MediaFileMonitor(ApplicationProperties applicationProperties, TrackService trackService)   {

        this.applicationProperties = applicationProperties;
        this.trackService = trackService;






        this.thread = new Thread(this);
        this.thread.start();

    }



    @Override
    public void run() {

        Path folder = Paths.get(applicationProperties.getMediaFolder(), TrackService.DOWNLOADED);

        try {
            this.watchService = FileSystems.getDefault().newWatchService();
            WatchKey key= folder.register(watchService,StandardWatchEventKinds.ENTRY_CREATE);

            key.cancel();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        for (;;) {
            WatchKey key;
            try {
                key = watchService.take();
            } catch (InterruptedException x) {
                return;
            }

            for (WatchEvent<?> event : key.pollEvents()) {

                WatchEvent<Path> ev = (WatchEvent<Path>) event;
                Path filename = ev.context();

                System.out.println(
                    "Event kind:" + event.kind()
                        + ". File affected: " + filename.toFile() + ".");
                File file = folder.resolve(filename).toFile();

                if (file.isDirectory()) {

                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

//                    ListDirectoryRecursively listDirectoryRecursively= new ListDirectoryRecursively(file.toPath());
//                    try {
//                        listDirectoryRecursively.call();
//                    } catch (IOException e) {
//                        throw new RuntimeException(e);
//                    }


//                    int count = 0;
//                    List files= new ArrayList();
//                    synchronized (this) {
//                         files.addAll(FileUtils.search(file, (dir, name) -> (name.toLowerCase().endsWith(".mp3"))));
//                    }
//
//                        for (Iterator iter = files.iterator(); iter.hasNext(); count++) {
//
//                            System.out.println("count: " + count);
//
//                            File mp3 = (File) iter.next();
//
//                            logger.info(mp3.getAbsolutePath());
////                            trackService.importFile(mp3);
//
//                        }


                        try {



                                Files.walkFileTree(file.toPath(), new SimpleFileVisitor<Path>() {
                                    @Override
                                    public FileVisitResult visitFile(Path file, BasicFileAttributes attributes) throws IOException {

                                        logger.info(file.toString());
                                        return FileVisitResult.CONTINUE;
                                    }
                                });

                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }


                } else if (file.exists() && file.getName().endsWith(".mp3")) {
//                        trackService.importFile(file);
                    logger.info(file.getAbsolutePath());
                }


            }

            boolean valid = key.reset();
            if (!valid) {
                break;
            }

        }
    }



    public Set<String> listFilesUsingDirectoryStream(String dir) throws IOException {
        Set<String> fileSet = new HashSet<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(dir))) {
            for (Path path : stream) {
                if (!Files.isDirectory(path)) {
                    fileSet.add(path.getFileName()
                        .toString());
                }
            }
        }
        return fileSet;
    }




}
