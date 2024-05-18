package net.vegatec.media_library.service.commands.handlers;

import net.vegatec.media_library.config.ApplicationProperties;
import net.vegatec.media_library.domain.Track;
import net.vegatec.media_library.mediator.CommandHandler;
import net.vegatec.media_library.service.TrackService;
import net.vegatec.media_library.service.commands.UpdateTrack;
import net.vegatec.media_library.service.commands.UploadAlbumCover;
import net.vegatec.media_library.service.dto.TrackDTO;
import net.vegatec.media_library.util.ImageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

@Component
public class UploadAlbumCoverHandler  implements CommandHandler<UploadAlbumCover, Void> {
    private static final Logger LOG = LoggerFactory.getLogger(UpdateTrackHandler.class);
//    private final TrackService trackService;
//
    private final ApplicationProperties applicationProperties;

    public UploadAlbumCoverHandler(ApplicationProperties applicationProperties) {

       this.applicationProperties = applicationProperties;
    }

    @Override
    public void handle(UploadAlbumCover command) {
        LOG.info("Uploading cover file {}", command.getFile());


        byte[] bytes;


        File downloadsFolder = new File(applicationProperties.getMediaFolder(), Track.DOWNLOADED);

        if (!downloadsFolder.exists())
            downloadsFolder.mkdir();


        if (!command.isEmpty()) {
            bytes = downloadedFile.getBytes();

            //store file in storage
            File tempFile = new File(String.format("%s/%s", downloadsFolder, downloadedFile.getOriginalFilename()));
            writeBytesToFileNio(bytes, tempFile.getAbsolutePath());

            if (!folder.isEmpty() && folder.startsWith("media")) {

                String artworkFileName = String.format("%s/%s/artwork.jpg", applicationProperties.getMediaFolder(), folder.replaceAll("media/", ""));
                File artworkFile = new File(artworkFileName);
                log.info("artwork file ->>>>>>>>>>>> {}", artworkFile.getAbsoluteFile());

                ImageUtils.scale(tempFile.toString(), 256, 256, artworkFile.getAbsolutePath());

                String thumbnailFileName = String.format("%s/%s/thumbnail.jpg", applicationProperties.getMediaFolder(),  folder.replaceAll("media/", ""));
                File thumbnailFile = new File(thumbnailFileName);
                ImageUtils.scale(tempFile.toString(), 128, 128, thumbnailFile.getAbsolutePath());

                tempFile.delete();


            } else {

                //MediaFile mediaFile = mediaFileBuilder.build(tempFile);
//                normalizeMediaFilesHandler.execute(null);
                importMediaFilesHandler.execute(null);
            }

        }

        System.out.println(String.format("receive %s ", downloadedFile.getOriginalFilename()));
    }




}
