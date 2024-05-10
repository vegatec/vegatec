package net.vegatec.media_library.service.commands;

import net.vegatec.media_library.mediator.Command;
import net.vegatec.media_library.service.dto.TrackDTO;

public class MoveTrack implements Command<TrackDTO> {


    private Long trackId;

    private String subfoler;

    public MoveTrack(Long trackId, String subfoler) {

        this.trackId = trackId;
        this.subfoler = subfoler;
    }

    public Long getTrackId() {
        return trackId;
    }
    public String getSubfoler() { return subfoler; }
}
