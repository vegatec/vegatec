package net.vegatec.media_library.service.commands;

import net.vegatec.media_library.mediator.Command;
import net.vegatec.media_library.service.dto.TrackDTO;

import java.util.Optional;

public class UpdateTrack implements Command<TrackDTO> {



    private TrackDTO track;
    private boolean inPlace;

    public UpdateTrack(TrackDTO track) {
        this.track = track;
        this.inPlace = false;

    }
    public UpdateTrack(TrackDTO track, boolean inPlace) {
        this.track = track;
        this.inPlace = inPlace;

    }

    public TrackDTO getTrack() {
        return track;
    }
    public boolean isInPlace() {    return  inPlace; }

}
