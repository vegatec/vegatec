package net.vegatec.media_library.query.service;

import net.vegatec.media_library.common.events.MediaFileCreated;
import net.vegatec.media_library.query.domain.Track;
import net.vegatec.media_library.query.repository.TrackRepository;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;

//@Slf4j

@Component
public class MediaFileProjector {

    private final TrackRepository repository;

    public MediaFileProjector(TrackRepository repository) {
        this.repository = repository;
    }


    @EventHandler
    public void on(MediaFileCreated event) {
     //   log.debug("Handling a Media File Created event {}", event.getId());
        Track track = new Track()
            .id(event.getId())
            .trackNumber(event.getTrackNumber())
            .name(event.getTitle())
            .filePath(event.getPath())
            .subfolder(event.getSubfolder())
            .playbackLength(event.getPlaybackLength())
            .createdOn(event.getCreatedOn())
            .artist(event.getArtistName())
            .album(event.getAlbumTitle(), event.getAlbumArtistName(), event.getAlbumReleasedYear())
            .genre(event.getGenreName());

        this.repository.save(track);
    }

}
