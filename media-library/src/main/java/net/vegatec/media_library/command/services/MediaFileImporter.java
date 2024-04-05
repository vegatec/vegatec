package net.vegatec.media_library.command.services;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.*;

@Service
public class MediaFileImporter {

    private final CommandGateway commandGateway;

    Path path = Paths.get("/media/robert/data/media/downloaded");

    private WatchService watchService;

    public MediaFileImporter(CommandGateway commandGateway) throws IOException, InterruptedException {

        this.commandGateway = commandGateway;


        watchService = FileSystems.getDefault().newWatchService();
        path.register(
            watchService,
            StandardWatchEventKinds.ENTRY_CREATE
            );


        WatchKey key;
        while ((key = watchService.take()) != null) {
            for (WatchEvent<?> event : key.pollEvents()) {
                System.out.println(
                    "Event kind:" + event.kind()
                        + ". File affected: " + event.context() + ".");
            }
            key.reset();
        }

    }

}
