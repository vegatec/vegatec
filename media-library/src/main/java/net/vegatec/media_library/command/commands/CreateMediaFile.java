package net.vegatec.media_library.command.commands;


import co.elastic.clients.elasticsearch.nodes.Ingest;

import java.io.File;
import java.time.Instant;
import java.time.LocalDateTime;

public class CreateMediaFile  extends BaseCommand<Long> {

    private File file;

//    private String subfolder;
//
//    private String path;
//
//    private String title;
//
//    private String artistName;
//
//    private String albumTitle;
//
//    private String albumArtistName;
//
//    private String genreName;
//
//    private Integer albumReleasedYear;
//
//    private Integer  playbackLength;
//
//    private Integer trackNumber;
//
//
//    private Instant createdOn;



//    public String getSubfolder() {
//        return subfolder;
//    }
//
//    public String getPath() {
//        return path;
//    }
//
//    public String getTitle() {
//        return title;
//    }
//
//    public String getArtistName() {
//        return artistName;
//    }
//
//
//    public String getAlbumTitle() {
//        return albumTitle;
//    }
//
//    public String getAlbumArtistName() {
//        return albumArtistName;
//    }
//
//    public Integer getAlbumReleasedYear() {
//        return albumReleasedYear;
//    }
//
//    public String getGenreName() {
//        return genreName;
//    }
//
//
//    public Integer getPlaybackLength() {
//        return playbackLength;
//    }
//
//    public Integer getTrackNumber() {
//        return trackNumber;
//    }
//
//    public Instant getCreatedOn() {
//        return createdOn;
//    }

    public File getFile() {
        return file;
    }

    public CreateMediaFile(Long id, File file) {
        super(id);

        this.file = file;

    }

//    public CreateMediaFile(Long id, String folder, String path, String title, String artistName, String albumTitle, String albumArtistName, Integer albumReleasedYear, String genreName, Integer playbackLength, Integer trackNumber, Instant createdOn) {
//        super(id);
//
//        this.subfolder = folder;
//        this.path = path;
//        this.title = title;
//        this.artistName = artistName;
//        this.albumTitle = albumTitle;
//        this.genreName = genreName;
//        this.albumArtistName = albumArtistName;
//        this.albumReleasedYear = albumReleasedYear;
//        this.playbackLength = playbackLength;
//        this.trackNumber = trackNumber;
//        this.createdOn = createdOn;
//    }


}
