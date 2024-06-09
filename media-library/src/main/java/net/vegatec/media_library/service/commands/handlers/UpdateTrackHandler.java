package net.vegatec.media_library.service.commands.handlers;

import net.vegatec.media_library.config.ApplicationProperties;
import net.vegatec.media_library.domain.Track;
import net.vegatec.media_library.mediator.CommandHandler;
import net.vegatec.media_library.repository.TrackRepository;
import net.vegatec.media_library.service.TrackService;
import net.vegatec.media_library.service.commands.UpdateTrack;
import net.vegatec.media_library.service.dto.TrackDTO;
import net.vegatec.media_library.service.mapper.TrackMapper;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.*;
import java.util.Optional;

@Component
public class UpdateTrackHandler extends BaseTrackHandler implements CommandHandler<UpdateTrack, TrackDTO> {
//    private static final Logger LOG = LoggerFactory.getLogger(UpdateTrackHandler.class);
    private final TrackRepository repository;
    private final TrackMapper trackMapper;
    private final ApplicationProperties applicationProperties;
    private final TrackRepository trackRepository;

    public UpdateTrackHandler(TrackRepository repository, TrackMapper trackMapper, ApplicationProperties applicationProperties, TrackRepository trackRepository) {

        this.repository = repository;
        this.trackMapper = trackMapper;

        this.applicationProperties = applicationProperties;
        this.trackRepository = trackRepository;
    }

    @Override
    public TrackDTO handle(UpdateTrack command) {
        LOG.info("Unpublish track with id {}", command.getTrack().getId());

            Optional<Track> optionalTrack = repository.findById(command.getTrack().getId());
            if (optionalTrack.isPresent()) {

                try {


                    Track track = optionalTrack.orElseThrow();

                    Path sourcePath= Path.of(applicationProperties.getMediaFolder(), track.getFilePath());

                    track.genre(command.getTrack().getGenre().getName());
                    track.artist(command.getTrack().getArtist().getName());
                    track.album(command.getTrack().getAlbum().getName(), command.getTrack().getAlbum().getArtist().getName(), command.getTrack().getAlbum().getReleasedYear());

                //    String originalSubfolder = track.getSubfolder();

                    track.subfolder(Track.INBOX);

                    Track result = repository.save(track);

                    Path destinationPath= Path.of(applicationProperties.getMediaFolder(), result.getFilePath());

                    moveFile(sourcePath, destinationPath);

                    return  trackMapper.toDto(result);

                } catch (IOException e) {
                   LOG.error("Something wrong happened while updating track {}", command.getTrack().getName());
                }

            }

        return null;
    }




}
