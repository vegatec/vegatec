package net.vegatec.media_library.service.commands.handlers;

import net.vegatec.media_library.config.ApplicationProperties;
import net.vegatec.media_library.domain.Track;
import net.vegatec.media_library.mediator.CommandHandler;
import net.vegatec.media_library.repository.TrackRepository;
import net.vegatec.media_library.service.TrackService;
import net.vegatec.media_library.service.commands.UpdateTrack;
import net.vegatec.media_library.service.dto.TrackDTO;
import net.vegatec.media_library.service.mapper.TrackMapper;
import org.apache.commons.io.FileExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.*;
import java.util.Optional;
import java.util.stream.Stream;

@Component
public class UpdateTrackHandler extends BaseTrackHandler implements CommandHandler<UpdateTrack, TrackDTO> {
//    private static final Logger LOG = LoggerFactory.getLogger(UpdateTrackHandler.class);
//    private final TrackService trackService;
//
//    private final ApplicationProperties applicationProperties;

    public UpdateTrackHandler(TrackService service, ApplicationProperties applicationProperties) {
        super(service, applicationProperties);
//        this.trackService = service;
//
//        this.applicationProperties = applicationProperties;
    }

    @Override
    public TrackDTO handle(UpdateTrack command) {
        LOG.info("Unpublish track with id {}", command.getTrack().getId());



            Optional<TrackDTO> _dto = trackService.findOne(command.getTrack().getId());
            if (_dto.isPresent()) {
                TrackDTO dto = _dto.get();

                dto.setGenre(command.getTrack().getGenre());
                dto.setArtist(command.getTrack().getArtist());
                dto.setAlbum(command.getTrack().getAlbum());

                String originalSubfolder = dto.getSubfolder();

                Path sourcePath= Path.of(applicationProperties.getMediaFolder(), dto.getFilePath());

                dto.setSubfolder(Track.INBOX);


                try {


                    Optional<TrackDTO> result = trackService.partialUpdate(dto);

                    Path destinationPath= Path.of(applicationProperties.getMediaFolder(), result.get().getFilePath());

                    moveFile(sourcePath, destinationPath);

                    return  result.get();

                } catch (IOException e) {
                    dto.setSubfolder(originalSubfolder);
                }

            }





        return null;
    }




}
