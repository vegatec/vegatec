package net.vegatec.media_library.common.events;

import java.time.Instant;
import java.time.LocalDateTime;


public class MediaFileCreated extends  BaseEvent<Long>{


    private String title;

    private String subfolder;

    private String path;

    private String artistName;

    private String albumTitle;

    private String albumArtistName;

    private Integer albumReleasedYear;

    private String genreName;

    private Long playbackLength;
    private Integer trackNumber;

    private LocalDateTime createdOn;



    public String getTitle() {
        return title;
    }

    public String getSubfolder() {
        return subfolder;
    }

    public String getPath() {
        return path;
    }


    public String getArtistName() {
        return artistName;
    }

    public String getAlbumTitle() {
        return albumTitle;
    }

    public String getAlbumArtistName() {
        return albumArtistName;
    }

    public Integer getAlbumReleasedYear() {
        return albumReleasedYear;
    }

    public String getGenreName() {
        return genreName;
    }

    public Long getPlaybackLength() {
        return playbackLength;
    }

    public Integer getTrackNumber() {
        return trackNumber;
    }

    public LocalDateTime getCreatedOn() {
        return  createdOn;
    }


    public MediaFileCreated(Long id, String title, String folder, String path, String artistName, String albumTitle, String albumArtistName, Integer albumReleasedYear, String genreName, Long playbackLength, Integer trackNumber,  LocalDateTime createdOn) {
        super(id);
        this.title = title;
        this.subfolder = folder;
        this.path = path;
        this.artistName = artistName;
        this.albumTitle = albumTitle;
        this.albumArtistName = albumArtistName;
        this.albumReleasedYear = albumReleasedYear;
        this.genreName = genreName;
        this.trackNumber = trackNumber;
        this.playbackLength = playbackLength;
        this.createdOn = createdOn;
    }

}
