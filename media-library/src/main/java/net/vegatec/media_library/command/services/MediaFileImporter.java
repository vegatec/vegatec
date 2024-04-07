package net.vegatec.media_library.command.services;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.*;


@Service
public class MediaFileImporter {
    private WatchService watchService;
    private final CommandGateway commandGateway;


    public MediaFileImporter(CommandGateway commandGateway)   {
        this.commandGateway = commandGateway;

        try {
            this.watchService = FileSystems.getDefault().newWatchService();
            Path path = Paths.get("/media/robert/data/media/downloaded/");

            path.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);
            WatchKey key;
            while ((key = watchService.take()) != null) {
                for (WatchEvent<?> event : key.pollEvents()) {
                    System.out.println(
                        "Event kind:" + event.kind()
                            + ". File affected: " + event.context() + ".");
                }
                key.reset();
            }
        } catch (Exception ex) {
            System.err.println("something bad happened");
        }
    }
}
