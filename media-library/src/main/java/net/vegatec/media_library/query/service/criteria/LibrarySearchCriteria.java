package net.vegatec.media_library.query.service.criteria;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;

/**
 * Created by robert on 10/12/15.
 */
public class LibrarySearchCriteria {

    public static String EMPTY_STRING = "";

    public LibrarySearchCriteria() {}

    public LibrarySearchCriteria(
        String filter,
        String genreName,
        String artistName,
        String albumName,
        String albumArtistName,
        String trackName,
        Integer releasedYear,
        Instant createdOn,
        String subfolder,
        Integer addedSince,
        Integer releasedSince
    ) {
        this.filter = filter;
        this.genreName = genreName;
        this.artistName = artistName;
        this.albumName = albumName;
        this.albumArtistName = albumArtistName;
        this.trackName = trackName;
        this.releasedYear = releasedYear;
        this.createdOn = createdOn;
        this.subfolder = subfolder;
        this.addedSince = addedSince;
        this.releasedSince = releasedSince;
    }

    private String filter;

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    private String genreName;

    public String getGenreName() {
        return genreName;
    }

    public void setGenreName(String genreName) {
        this.genreName = genreName;
    }

    private String artistName;

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    private String albumName;

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    private String albumArtistName;

    public String getAlbumArtistName() {
        return albumArtistName;
    }

    public void setAlbumArtistName(String albumArtistName) {
        this.albumArtistName = albumArtistName;
    }

    private String trackName;

    public String getTrackName() {
        return trackName;
    }

    public void setTrackName(String trackName) {
        this.trackName = trackName;
    }

    private Integer releasedYear;

    public Integer getReleasedYear() {
        return releasedYear;
    }

    public void setReleasedYear(Integer year) {
        this.releasedYear = year;
    }

    private Instant createdOn;

    public Instant getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Instant createdOn) {
        this.createdOn = createdOn;
    }

    private String subfolder;

    public String getSubfolder() {
        return subfolder;
    }

    public void setSubfolder(String subfolder) {
        this.subfolder = subfolder;
    }

    private Integer addedSince;

    public Integer getAddedSince() {
        return addedSince;
    }

    public void setAddedSince(int addedSince) {
        this.addedSince = addedSince;
    }

    public Instant getAddedAfter() {
        return addedSince == null ? null : Instant.now().minus(getAddedSince(), ChronoUnit.DAYS);
    }

    private Integer releasedSince;

    public Integer getReleasedSince() {
        return releasedSince;
    }

    public void setReleasedSince(Integer releasedSince) {
        this.releasedSince = releasedSince;
    }

    public Integer getReleasedAfter() {
        //   return 2021-2;
        return releasedSince == null ? null : LocalDateTime.now().getYear() - releasedSince;
    }

    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
