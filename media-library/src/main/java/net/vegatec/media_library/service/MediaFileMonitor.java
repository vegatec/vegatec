package net.vegatec.media_library.service;

import org.slf4j.Logger;


import net.vegatec.media_library.config.ApplicationProperties;


import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


import java.io.File;
import java.nio.file.*;

@Service
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
        try {
            this.watchService = FileSystems.getDefault().newWatchService();

            Path folder = Paths.get(applicationProperties.getMediaFolder(), TrackService.DOWNLOADED);

            folder.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);

            WatchKey key;
            while ((key = watchService.take()) != null) {
                for (WatchEvent<?> event : key.pollEvents()) {

                    WatchEvent<Path> ev = (WatchEvent<Path>) event;
                    Path filename = ev.context();

                    System.out.println(
                        "Event kind:" + event.kind()
                            + ". File affected: " + filename.toFile() + ".");

                    File file= Paths.get(applicationProperties.getMediaFolder(), TrackService.DOWNLOADED, filename.toString()).toFile();

                    if (file.exists())
                        trackService.importFile(file);
                }
                key.reset();
            }
        } catch (Exception ex) {
            System.err.println("something bad happened");
        }
    }


}
