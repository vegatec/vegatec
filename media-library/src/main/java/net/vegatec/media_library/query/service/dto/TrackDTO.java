package net.vegatec.media_library.query.service.dto;

import jakarta.validation.constraints.*;
import net.vegatec.media_library.query.domain.Track;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link Track} entity.
 */
public class TrackDTO implements Serializable {

    private Long id;

    @NotNull
    private String subfolder;

    private String artworkPath;

    private String filePath;

    @NotNull
    private String name;

    @NotNull
    private ArtistDTO artist;

    @NotNull
    private AlbumDTO album;

    @NotNull
    private GenreDTO genre;

    private Integer trackNumber;

    private Integer playbackLength;

    private Integer bitRate;

    @NotNull
    private Instant createdOn;

    private Boolean tagVersion1;

    private Boolean tagVersion2;

    private TrackTypeDTO type;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getArtworkPath() {
        return artworkPath;
    }

    public void setFilePath(String path) {
        this.filePath = path;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setArtworkPath(String artworkPath) {
        this.artworkPath = artworkPath;
    }

    public String getSubfolder() {
        return subfolder;
    }

    public void setSubfolder(String subfolder) {
        this.subfolder = subfolder;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    //    public String getSortName() {
    //        return sortName;
    //    }
    //
    //    public void setSortName(String sortName) {
    //        this.sortName = sortName;
    //    }

    public ArtistDTO getArtist() {
        return artist;
    }

    public void setArtist(ArtistDTO artist) {
        this.artist = artist;
    }

    //    public String getArtistSortName() {
    //        return artistSortName;
    //    }
    //
    //    public void setArtistSortName(String artistSortName) {
    //        this.artistSortName = artistSortName;
    //    }

    public AlbumDTO getAlbum() {
        return album;
    }

    public void setAlbum(AlbumDTO album) {
        this.album = album;
    }

    //    public String getAlbumSortName() {
    //        return albumSortName;
    //    }
    //
    //    public void setAlbumSortName(String albumSortName) {
    //        this.albumSortName = albumSortName;
    //    }
    //
    //    public String getAlbumArtistName() {
    //        return albumArtistName;
    //    }
    //
    //    public void setAlbumArtistName(String albumArtistName) {
    //        this.albumArtistName = albumArtistName;
    //    }
    //
    //    public String getAlbumArtistSortName() {
    //        return albumArtistSortName;
    //    }
    //
    //    public void setAlbumArtistSortName(String albumArtistSortName) {
    //        this.albumArtistSortName = albumArtistSortName;
    //    }
    //
    //    public Integer getAlbumReleasedYear() {
    //        return albumReleasedYear;
    //    }
    //
    //    public void setAlbumReleasedYear(Integer albumReleasedYear) {
    //        this.albumReleasedYear = albumReleasedYear;
    //    }

    public GenreDTO getGenre() {
        return genre;
    }

    public void setGenre(GenreDTO genre) {
        this.genre = genre;
    }

    //    public String getGenreSortName() {
    //        return genreSortName;
    //    }
    //
    //    public void setGenreSortName(String genreSortName) {
    //        this.genreSortName = genreSortName;
    //    }

    public Integer getTrackNumber() {
        return trackNumber;
    }

    public void setTrackNumber(Integer trackNumber) {
        this.trackNumber = trackNumber;
    }

    public Integer getPlaybackLength() {
        return playbackLength;
    }

    public void setPlaybackLength(Integer playbackLength) {
        this.playbackLength = playbackLength;
    }

    public Integer getBitRate() {
        return bitRate;
    }

    public void setBitRate(Integer bitRate) {
        this.bitRate = bitRate;
    }

    public Instant getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Instant createdOn) {
        this.createdOn = createdOn;
    }

    public Boolean getTagVersion1() {
        return tagVersion1;
    }

    public void setTagVersion1(Boolean tagVersion1) {
        this.tagVersion1 = tagVersion1;
    }

    public Boolean getTagVersion2() {
        return tagVersion2;
    }

    public void setTagVersion2(Boolean tagVersion2) {
        this.tagVersion2 = tagVersion2;
    }

    public TrackTypeDTO getType() {
        return type;
    }

    public void setType(TrackTypeDTO type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TrackDTO)) {
            return false;
        }

        TrackDTO trackDTO = (TrackDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, trackDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TrackDTO{" +
            "id=" + getId() +
//            ", filePath='" + getFilePath() + "'" +
            ", subfolder='" + getSubfolder() + "'" +
            ", name='" + getName() + "'" +
//            ", sortName='" + getSortName() + "'" +
//            ", artistName='" + getArtistName() + "'" +
//            ", artistSortName='" + getArtistSortName() + "'" +
//            ", albumName='" + getAlbumName() + "'" +
//            ", albumSortName='" + getAlbumSortName() + "'" +
//            ", albumArtistName='" + getAlbumArtistName() + "'" +
//            ", albumArtistSortName='" + getAlbumArtistSortName() + "'" +
//            ", albumReleasedYear=" + getAlbumReleasedYear() +
//            ", genreName='" + getGenreName() + "'" +
//            ", genreSortName='" + getGenreSortName() + "'" +
            ", trackNumber=" + getTrackNumber() +
            ", playbackLength=" + getPlaybackLength() +
            ", bitRate=" + getBitRate() +
            ", createdOn='" + getCreatedOn() + "'" +
            ", tagVersion1='" + getTagVersion1() + "'" +
            ", tagVersion2='" + getTagVersion2() + "'" +
            ", type=" + getType() +
            "}";
    }
}
