package net.vegatec.media_library.service.commands;

import net.vegatec.media_library.mediator.Command;
import net.vegatec.media_library.service.dto.TrackDTO;

import java.util.Optional;

public class UpdateTrack implements Command<TrackDTO> {



    private TrackDTO track;

    public UpdateTrack(TrackDTO track) {
        this.track = track;

    }

    public TrackDTO getTrack() {
        return track;
    }

}
