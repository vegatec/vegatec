package net.vegatec.media_library.service.commands.handlers;

import net.vegatec.media_library.config.ApplicationProperties;
import net.vegatec.media_library.domain.Track;
import net.vegatec.media_library.mediator.CommandHandler;
import net.vegatec.media_library.repository.TrackRepository;
import net.vegatec.media_library.service.commands.MoveTrack;
import net.vegatec.media_library.service.dto.TrackDTO;
import net.vegatec.media_library.service.mapper.TrackMapper;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.*;
import java.util.Optional;

@Component
public class MoveTrackHandler extends BaseTrackHandler implements CommandHandler<MoveTrack, TrackDTO> {
//    private static final Logger LOG = LoggerFactory.getLogger(MoveTrackHandler.class);
    private final TrackRepository   repository;
    private final TrackMapper trackMapper;
    private final ApplicationProperties applicationProperties;

    public MoveTrackHandler(TrackRepository repository, ApplicationProperties applicationProperties, TrackRepository repository1, TrackMapper trackMapper, ApplicationProperties applicationProperties1) {
        this.repository = repository;
        this.trackMapper = trackMapper;
        this.applicationProperties = applicationProperties;
    }

    @Override
    public TrackDTO handle(MoveTrack command) {
        LOG.info("Unpublish track with id {}", command.getTrackId());

        Optional<Track> optionalTrack= repository.findById(command.getTrackId());

        if   (optionalTrack.isPresent()) {

            try {

                Track track = optionalTrack.orElseThrow();

//                String originalSubfolder = track.getSubfolder();

                Path sourcePath= Path.of(applicationProperties.getMediaFolder(), track.getFilePath());

                track.subfolder(command.getSubfoler());

                track= repository.save(track);

                Path destinationPath= Path.of(applicationProperties.getMediaFolder(), track.getFilePath());
                moveFile(sourcePath, destinationPath);
                return  trackMapper.toDto(track);

            } catch (IOException e) {
                LOG.error("Error moving track", e);
            }


        }

        return null;
    }




}
