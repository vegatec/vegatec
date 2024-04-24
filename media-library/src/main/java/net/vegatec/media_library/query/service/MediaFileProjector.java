package net.vegatec.media_library.query.service;

import jakarta.transaction.Transactional;
import net.vegatec.media_library.common.events.MediaFileCreated;
import net.vegatec.media_library.common.events.MediaFileImported;
import net.vegatec.media_library.query.domain.Track;
import net.vegatec.media_library.query.domain.TrackType;
import net.vegatec.media_library.query.repository.TrackRepository;
import net.vegatec.media_library.query.repository.TrackTypeRepository;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;

//@Slf4j

@Component
//@Transactional
public class MediaFileProjector {

    private final TrackRepository repository;
    private final TrackTypeRepository trackTypeRepository;

    public MediaFileProjector(TrackRepository repository, TrackTypeRepository trackTypeRepository) {
        this.repository = repository;
        this.trackTypeRepository = trackTypeRepository;

    }


    @EventHandler
    public void on(MediaFileCreated event) {
     //   log.debug("Handling a Media File Created event {}", event.getId());
        TrackType type= trackTypeRepository.getReferenceById(1L);
        Track track = new Track()
            .id(event.getId())
            .trackNumber(event.getTrackNumber())
            .name(event.getName())
            .filePath(event.getPath())
            .subfolder(event.getSubfolder())
            .playbackLength(event.getPlaybackLength())
            .createdOn(event.getCreatedOn())
            .artist(event.getArtistName())
            .album(event.getAlbumTitle(), event.getAlbumArtistName(), event.getAlbumReleasedYear())
            .genre(event.getGenreName())
            .type(type)
            .bitRate(92000)
            .tagVersion1(false)
            .tagVersion2(true);

        track.updatePath();

        this.repository.save(track);
    }

}
