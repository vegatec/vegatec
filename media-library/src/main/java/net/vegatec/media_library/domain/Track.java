package net.vegatec.media_library.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.text.Normalizer;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Objects;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Track.
 */
@Entity
@Table(name = "track")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "track")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Track implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String DOWNLOADED = "downloaded";
    public static final String INBOX = "inbox";
    public static final String OUTBOX = "outbox";
    public static final String TRASH = "trash";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "file_path", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String filePath;

    @NotNull
    @Column(name = "subfolder", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String subfolder;

    @NotNull
    @Column(name = "name", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String name;

    @NotNull
    @Column(name = "sort_name", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String sortName;

    @Column(name = "track_number")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer trackNumber;

    @Column(name = "playback_length")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer playbackLength;

    @Column(name = "bit_rate")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer bitRate;

    @NotNull
    @Column(name = "created_on", nullable = false)
    private Instant createdOn;

    @Column(name = "tag_version_1")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean tagVersion1;

    @Column(name = "tag_version_2")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean tagVersion2;

    @ManyToOne(optional = false)
    @NotNull
    private TrackType type;

    @Embedded
    @AttributeOverrides(
            value = {
                    @AttributeOverride(name = "name", column = @Column(name = "album_name")),
                    @AttributeOverride(name = "sortName", column = @Column(name = "album_sort_name")),
                    @AttributeOverride(name = "releasedYear", column = @Column(name = "album_released_year")),
                    @AttributeOverride(name = "subfolder", column = @Column(name = "subfolder", updatable = false, insertable = false)),
            }
    )
    @Access(AccessType.FIELD)
    private Album album;

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
        updatePath();
    }

    public Track album(String name, String artist, Integer releasedYear) {
        this.setAlbum(new Album(name, artist, releasedYear));
        return this;
    }

    @Embedded
    @AttributeOverrides(
            value = {
                    @AttributeOverride(name = "name", column = @Column(name = "genre_name")),
                    @AttributeOverride(name = "sortName", column = @Column(name = "genre_sort_name"))
            }
    )
    @Access(AccessType.FIELD)
    private Genre genre;

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
         updatePath();
    }

    public Track genre(String genreName) {
        setGenre(new Genre(genreName));
        return this;
    }

    @Embedded
    @AttributeOverrides(
            value = {
                    @AttributeOverride(name = "name", column = @Column(name = "artist_name")),
                    @AttributeOverride(name = "sortName", column = @Column(name = "artist_sort_name"))
            }
    )
    @Access(AccessType.FIELD)
    private Artist artist;

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
        updatePath();
    }

    public Track artist(String  name) {
        setArtist(new Artist(name));
        return this;
    }

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Track id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Track filePath(String filePath) {
        this.setFilePath(filePath);
        return this;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }


    public String getFilePath() {
        return String.format("%s/%s", subfolder, filePath);
    }

    protected void updatePath() {
        this.filePath = getUpdatedPath();
    }

    public String getUpdatedPath() {
         return
                String.format(
                        "%s/%s/%s.mp3",
                        Integer.toHexString(album == null || album.getArtist() == null ? 0 : album.getArtist().hashCode()),
                        Integer.toHexString(album == null ? 0 : album.hashCode()),
                        Integer.toHexString(hashCode())
                );
    }

    public String getSubfolder() {
        return this.subfolder;
    }

    public Track subfolder(String subfolder) {
        this.setSubfolder(subfolder);
        return this;
    }

    public void setSubfolder(String subfolder) {
        this.subfolder = subfolder;
    }

    public String getName() {
        return this.name;
    }

    public Track name(String name) {
        this.setName(name);

        return this;
    }

    public void setName(String name) {
        this.name = name;
        this.sortName = (name == null)? null:
                Normalizer.normalize(name.toLowerCase().replaceAll("\\s+",""), Normalizer.Form.NFKD)
                        .replaceAll("\\p{M}", "");

        this.updatePath();
    }

//    public String getSortName() {
//        return this.sortName;
//    }
//
//    public Track sortName(String sortName) {
//        this.setSortName(sortName);
//        return this;
//    }
//
//    public void setSortName(String sortName) {
//        this.sortName = sortName;
//    }

    public Integer getTrackNumber() {
        return this.trackNumber;
    }

    public Track trackNumber(Integer trackNumber) {
        this.setTrackNumber(trackNumber);
        return this;
    }

    public void setTrackNumber(Integer trackNumber) {
        this.trackNumber = trackNumber;
    }

    public Integer getPlaybackLength() {
        return this.playbackLength;
    }

    public Track playbackLength(Integer playbackLength) {
        this.setPlaybackLength(playbackLength);
        return this;
    }

    public void setPlaybackLength(Integer playbackLength) {
        this.playbackLength = playbackLength;
    }

    public Integer getBitRate() {
        return this.bitRate;
    }

    public Track bitRate(Integer bitRate) {
        this.setBitRate(bitRate);
        return this;
    }

    public void setBitRate(Integer bitRate) {
        this.bitRate = bitRate;
    }

    public Instant getCreatedOn() {
        return this.createdOn;
    }

    public Track createdOn(Instant createdOn) {
        this.setCreatedOn(createdOn);
        return this;
    }

    public void setCreatedOn(Instant createdOn) {
        this.createdOn = createdOn;
    }

    public Boolean getTagVersion1() {
        return this.tagVersion1;
    }

    public Track tagVersion1(Boolean tagVersion1) {
        this.setTagVersion1(tagVersion1);
        return this;
    }

    public void setTagVersion1(Boolean tagVersion1) {
        this.tagVersion1 = tagVersion1;
    }

    public Boolean getTagVersion2() {
        return this.tagVersion2;
    }

    public Track tagVersion2(Boolean tagVersion2) {
        this.setTagVersion2(tagVersion2);
        return this;
    }

    public void setTagVersion2(Boolean tagVersion2) {
        this.tagVersion2 = tagVersion2;
    }

    public TrackType getType() {
        return this.type;
    }

    public void setType(TrackType trackType) {
        this.type = trackType;
    }

    public Track type(TrackType trackType) {
        this.setType(trackType);
        return this;
    }

    public String getArtworkPath() {
        return album.getArtworkPath();
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Track)) {
            return false;
        }
        return getId() != null && getId().equals(((Track) o).getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(trackNumber, genre == null ? 0: genre.hashCode(), name);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Track{" +
                "id=" + getId() +
                ", filePath='" + getFilePath() + "'" +
                ", subfolder='" + getSubfolder() + "'" +
                ", name='" + getName() + "'" +
//                ", sortName='" + getSortName() + "'" +
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
                "}";
    }
}
