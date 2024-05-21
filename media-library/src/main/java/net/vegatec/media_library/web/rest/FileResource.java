package net.vegatec.media_library.web.rest;

import net.vegatec.media_library.mediator.SpringMediator;
import net.vegatec.media_library.service.commands.UploadImageFile;
import net.vegatec.media_library.service.commands.UploadMediaFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * REST controller for managing {@link net.vegatec.media_library.domain.Track}.
 */
@RestController
@RequestMapping("/api/files")
public class FileResource {

    private final Logger log = LoggerFactory.getLogger(FileResource.class);

    private static final String ENTITY_NAME = "track";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SpringMediator mediator;

    public FileResource( SpringMediator mediator) {
        this.mediator = mediator;

    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = "/image")
    public void imageFile(@RequestParam("file") MultipartFile downloadedFile, @RequestParam("folder") String folder) throws Exception {

        if (!downloadedFile.isEmpty()) {
            byte[] bytes = downloadedFile.getBytes();

            //store file in storage
            File tempFile = File.createTempFile(downloadedFile.getOriginalFilename(),"");
            try {
                Path path = Paths.get(tempFile.getAbsolutePath());
                Files.write(path, bytes);
                mediator.send(new UploadImageFile(tempFile, folder));
                log.info("received file %s ", downloadedFile.getOriginalFilename());

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }


    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = "/media")
    public void mediaFile(@RequestParam("file") MultipartFile downloadedFile) throws Exception {

        if (!downloadedFile.isEmpty()) {
            byte[] bytes = downloadedFile.getBytes();

            //store file in storage
            File tempFile = File.createTempFile("media", downloadedFile.getOriginalFilename());
            try {
                Path path = Paths.get(tempFile.getAbsolutePath());
                Files.write(path, bytes);
                mediator.send(new UploadMediaFile(tempFile));
                log.info("received file %s ", downloadedFile.getOriginalFilename());

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

}
