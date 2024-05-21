package net.vegatec.media_library.service.commands.handlers;

import net.vegatec.media_library.config.ApplicationProperties;
import net.vegatec.media_library.domain.Track;
import net.vegatec.media_library.mediator.CommandHandler;
import net.vegatec.media_library.repository.AlbumRepository;
import net.vegatec.media_library.service.commands.UploadImageFile;
import net.vegatec.media_library.service.commands.UploadMediaFile;
import net.vegatec.media_library.util.ImageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Component
public class MediaFileUploadHandler implements CommandHandler<UploadMediaFile, Void> {
    private static final Logger LOG = LoggerFactory.getLogger(UpdateTrackHandler.class);

    private final ApplicationProperties applicationProperties;

    private final AlbumRepository albumRepository;

    public MediaFileUploadHandler(ApplicationProperties applicationProperties, AlbumRepository albumRepository) {

       this.applicationProperties = applicationProperties;
        this.albumRepository = albumRepository;
    }

    @Override
    public Void handle(UploadMediaFile command) {
        LOG.info("Uploading  file {}", command.getFile());

        try {
            Path sourcePath= command.getFile().toPath();
            Path detinationPath = Path.of(applicationProperties.getMediaFolder(), Track.DOWNLOADED, command.getFile().getName());

            Files.move(sourcePath, detinationPath);
            LOG.info("artwork file ->>>>>>>>>>>> {}", detinationPath);


        } catch (IOException e) {

            LOG.error("Something went wrong while uploading file", e);
        }



        return null;
    }




}
