package net.vegatec.media_library.service.commands.handlers;

import net.vegatec.media_library.config.ApplicationProperties;
import net.vegatec.media_library.mediator.CommandHandler;
import net.vegatec.media_library.repository.AlbumRepository;
import net.vegatec.media_library.service.commands.UploadImageFile;
import net.vegatec.media_library.util.ImageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

@Component
public class ImageFileUploadHandler implements CommandHandler<UploadImageFile, Void> {
    private static final Logger LOG = LoggerFactory.getLogger(UpdateTrackHandler.class);

    private final ApplicationProperties applicationProperties;

    private final AlbumRepository albumRepository;

    public ImageFileUploadHandler(ApplicationProperties applicationProperties, AlbumRepository albumRepository) {

       this.applicationProperties = applicationProperties;
        this.albumRepository = albumRepository;
    }

    @Override
    public Void handle(UploadImageFile command) {
        LOG.info("Uploading  file {}", command.getFile());


        String folder= command.getFolder();
        File file= command.getFile();


        if (!folder.isEmpty() && folder.startsWith("media")) {
            try {

                String artworkFileName = String.format("%s/%s/artwork.jpg", applicationProperties.getMediaFolder(), folder.replaceAll("media/", ""));
                File artworkFile = new File(artworkFileName);
                LOG.info("artwork file ->>>>>>>>>>>> {}", artworkFile.getAbsoluteFile());

                ImageUtils.scale(file.toString(), 256, 256, artworkFile.getAbsolutePath());

                String thumbnailFileName = String.format("%s/%s/thumbnail.jpg", applicationProperties.getMediaFolder(), folder.replaceAll("media/", ""));
                File thumbnailFile = new File(thumbnailFileName);
                ImageUtils.scale(file.toString(), 128, 128, thumbnailFile.getAbsolutePath());

                file.delete();
            } catch (IOException e) {

                LOG.error("Something went wrong while uploading file", e);
            }

        }

        return null;
    }




}
