package net.vegatec.media_library.command.commands;


import java.time.Instant;
import java.time.LocalDateTime;

public class CreateMediaFile  extends BaseCommand<Long> {


    private String subfolder;

    private String path;

    private String title;

    private String artistName;

    private String albumTitle;

    private String albumArtistName;

    private String genreName;

    private Integer albumReleasedYear;

    private Long  playbackLength;

    private Integer trackNumber;


    private LocalDateTime createdOn;



    public String getSubfolder() {
        return subfolder;
    }

    public String getPath() {
        return path;
    }

    public String getTitle() {
        return title;
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
        return createdOn;
    }



    public CreateMediaFile(Long id,  String folder, String path, String title, String artistName, String albumTitle, String albumArtistName, Integer albumReleasedYear, String genreName, Long playbackLength, Integer trackNumber, LocalDateTime createdOn) {
        super(id);

        this.subfolder = folder;
        this.path = path;
        this.title = title;
        this.artistName = artistName;
        this.albumTitle = albumTitle;
        this.genreName = genreName;
        this.albumArtistName = albumArtistName;
        this.albumReleasedYear = albumReleasedYear;
        this.playbackLength = playbackLength;
        this.trackNumber = trackNumber;
        this.createdOn = createdOn;
    }


}
