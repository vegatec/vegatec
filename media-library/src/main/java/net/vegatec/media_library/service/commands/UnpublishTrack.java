package net.vegatec.media_library.service.commands;

import net.vegatec.media_library.mediator.Command;
import net.vegatec.media_library.service.dto.TrackDTO;

public class UnpublishTrack implements Command<TrackDTO> {
    private Long trackId;
    public UnpublishTrack(Long trackId) {
        this.trackId = trackId;
    }
    public Long getTrackId() {
        return trackId;
    }
}
