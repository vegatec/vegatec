package net.vegatec.media_library.service.commands;

import net.vegatec.media_library.mediator.Command;
import net.vegatec.media_library.service.dto.TrackDTO;

public class MoveToTrash implements Command<TrackDTO> {
    private Long trackId;
    public MoveToTrash(Long trackId) {
        this.trackId = trackId;
    }
}
