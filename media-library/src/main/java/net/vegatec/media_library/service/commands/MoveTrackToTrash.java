package net.vegatec.media_library.service.commands;

import net.vegatec.media_library.mediator.Command;
import net.vegatec.media_library.service.dto.TrackDTO;

public class MoveTrackToTrash implements Command<TrackDTO> {
    public Long getTrackId() {
        return trackId;
    }

    private Long trackId;
    public MoveTrackToTrash(Long trackId) {
        this.trackId = trackId;
    }
}
