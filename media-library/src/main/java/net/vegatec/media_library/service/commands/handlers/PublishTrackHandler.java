package net.vegatec.media_library.service.commands.handlers;

import net.vegatec.media_library.domain.Track;
import net.vegatec.media_library.mediator.CommandHandler;
import net.vegatec.media_library.repository.TrackRepository;
import net.vegatec.media_library.service.RecursiveFolderMonitor;
import net.vegatec.media_library.service.commands.PublishTrack;
import net.vegatec.media_library.service.dto.TrackDTO;
import net.vegatec.media_library.service.mapper.TrackMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class PublishTrackHandler implements CommandHandler<PublishTrack, TrackDTO> {
    private static final Logger LOG = LoggerFactory.getLogger(PublishTrackHandler.class);
    private final TrackRepository   repository;
    private final TrackMapper trackMapper;

    public PublishTrackHandler(TrackRepository repository, TrackMapper trackMapper) {
        this.repository = repository;
        this.trackMapper = trackMapper;
    }

    @Override
    public TrackDTO handle(PublishTrack request) {
        LOG.info("Publishing track {}", request.getTrackId());

        Optional<Track> track= repository.findById(request.getTrackId());

        if   (track.isPresent()) {
            Track value = track.get();
            value.subfolder(Track.OUTBOX);
            value= repository.save(value);;

            return trackMapper.toDto(value);
        }

        return null;
    }
}
