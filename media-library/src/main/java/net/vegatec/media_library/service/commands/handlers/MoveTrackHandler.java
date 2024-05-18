package net.vegatec.media_library.service.commands.handlers;

import net.vegatec.media_library.config.ApplicationProperties;
import net.vegatec.media_library.domain.Track;
import net.vegatec.media_library.mediator.CommandHandler;
import net.vegatec.media_library.repository.TrackRepository;
import net.vegatec.media_library.service.TrackService;
import net.vegatec.media_library.service.commands.MoveTrack;
import net.vegatec.media_library.service.commands.UnpublishTrack;
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
public class MoveTrackHandler extends BaseTrackHandler implements CommandHandler<MoveTrack, TrackDTO> {
//    private static final Logger LOG = LoggerFactory.getLogger(MoveTrackHandler.class);
//    private final TrackRepository   repository;
//    private final TrackMapper trackMapper;
 //   private final ApplicationProperties applicationProperties;

    public MoveTrackHandler(TrackService service,  ApplicationProperties applicationProperties) {
        super(service, applicationProperties);
//        this.repository = repository;
//        this.trackMapper = trackMapper;

    //    this.applicationProperties = applicationProperties;
    }

    @Override
    public TrackDTO handle(MoveTrack command) {
        LOG.info("Unpublish track with id {}", command.getTrackId());

        Optional<TrackDTO> track= trackService.findOne(command.getTrackId());

        if   (track.isPresent()) {

            TrackDTO value = track.get();

            String originalSubfolder = value.getSubfolder();

            Path sourcePath= Path.of(applicationProperties.getMediaFolder(), value.getFilePath());

            value.setSubfolder(command.getSubfoler());


            try {

                value= trackService.save(value);;
                Path destinationPath= Path.of(applicationProperties.getMediaFolder(), value.getFilePath());
                moveFile(sourcePath, destinationPath);
                return  value;

            } catch (IOException e) {
                value.setSubfolder(originalSubfolder);
            }


        }

        return null;
    }




}
