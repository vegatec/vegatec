package net.vegatec.media_library.query.service.criteria;

import java.io.Serializable;
import java.util.Objects;

import net.vegatec.media_library.query.domain.Track;
import net.vegatec.media_library.query.web.rest.TrackResource;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link Track} entity. This class is used
 * in {@link TrackResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /tracks?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TrackCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter filePath;

    private StringFilter subfolder;

    private StringFilter name;

    private StringFilter sortName;

    private StringFilter artistName;

    private StringFilter artistSortName;

    private StringFilter albumName;

    private StringFilter albumSortName;

    private StringFilter albumArtistName;

    private StringFilter albumArtistSortName;

    private IntegerFilter albumReleasedYear;

    private StringFilter genreName;

    private StringFilter genreSortName;

    private IntegerFilter trackNumber;

    private IntegerFilter playbackLength;

    private IntegerFilter bitRate;

    private InstantFilter createdOn;

    private BooleanFilter tagVersion1;

    private BooleanFilter tagVersion2;

    private LongFilter typeId;

    private Boolean distinct;

    public TrackCriteria() {}

    public TrackCriteria(TrackCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.filePath = other.filePath == null ? null : other.filePath.copy();
        this.subfolder = other.subfolder == null ? null : other.subfolder.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.sortName = other.sortName == null ? null : other.sortName.copy();
        this.artistName = other.artistName == null ? null : other.artistName.copy();
        this.artistSortName = other.artistSortName == null ? null : other.artistSortName.copy();
        this.albumName = other.albumName == null ? null : other.albumName.copy();
        this.albumSortName = other.albumSortName == null ? null : other.albumSortName.copy();
        this.albumArtistName = other.albumArtistName == null ? null : other.albumArtistName.copy();
        this.albumArtistSortName = other.albumArtistSortName == null ? null : other.albumArtistSortName.copy();
        this.albumReleasedYear = other.albumReleasedYear == null ? null : other.albumReleasedYear.copy();
        this.genreName = other.genreName == null ? null : other.genreName.copy();
        this.genreSortName = other.genreSortName == null ? null : other.genreSortName.copy();
        this.trackNumber = other.trackNumber == null ? null : other.trackNumber.copy();
        this.playbackLength = other.playbackLength == null ? null : other.playbackLength.copy();
        this.bitRate = other.bitRate == null ? null : other.bitRate.copy();
        this.createdOn = other.createdOn == null ? null : other.createdOn.copy();
        this.tagVersion1 = other.tagVersion1 == null ? null : other.tagVersion1.copy();
        this.tagVersion2 = other.tagVersion2 == null ? null : other.tagVersion2.copy();
        this.typeId = other.typeId == null ? null : other.typeId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public TrackCriteria copy() {
        return new TrackCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getFilePath() {
        return filePath;
    }

    public StringFilter filePath() {
        if (filePath == null) {
            filePath = new StringFilter();
        }
        return filePath;
    }

    public void setFilePath(StringFilter filePath) {
        this.filePath = filePath;
    }

    public StringFilter getSubfolder() {
        return subfolder;
    }

    public StringFilter subfolder() {
        if (subfolder == null) {
            subfolder = new StringFilter();
        }
        return subfolder;
    }

    public void setSubfolder(StringFilter subfolder) {
        this.subfolder = subfolder;
    }

    public StringFilter getName() {
        return name;
    }

    public StringFilter name() {
        if (name == null) {
            name = new StringFilter();
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getSortName() {
        return sortName;
    }

    public StringFilter sortName() {
        if (sortName == null) {
            sortName = new StringFilter();
        }
        return sortName;
    }

    public void setSortName(StringFilter sortName) {
        this.sortName = sortName;
    }

    public StringFilter getArtistName() {
        return artistName;
    }

    public StringFilter artistName() {
        if (artistName == null) {
            artistName = new StringFilter();
        }
        return artistName;
    }

    public void setArtistName(StringFilter artistName) {
        this.artistName = artistName;
    }

    public StringFilter getArtistSortName() {
        return artistSortName;
    }

    public StringFilter artistSortName() {
        if (artistSortName == null) {
            artistSortName = new StringFilter();
        }
        return artistSortName;
    }

    public void setArtistSortName(StringFilter artistSortName) {
        this.artistSortName = artistSortName;
    }

    public StringFilter getAlbumName() {
        return albumName;
    }

    public StringFilter albumName() {
        if (albumName == null) {
            albumName = new StringFilter();
        }
        return albumName;
    }

    public void setAlbumName(StringFilter albumName) {
        this.albumName = albumName;
    }

    public StringFilter getAlbumSortName() {
        return albumSortName;
    }

    public StringFilter albumSortName() {
        if (albumSortName == null) {
            albumSortName = new StringFilter();
        }
        return albumSortName;
    }

    public void setAlbumSortName(StringFilter albumSortName) {
        this.albumSortName = albumSortName;
    }

    public StringFilter getAlbumArtistName() {
        return albumArtistName;
    }

    public StringFilter albumArtistName() {
        if (albumArtistName == null) {
            albumArtistName = new StringFilter();
        }
        return albumArtistName;
    }

    public void setAlbumArtistName(StringFilter albumArtistName) {
        this.albumArtistName = albumArtistName;
    }

    public StringFilter getAlbumArtistSortName() {
        return albumArtistSortName;
    }

    public StringFilter albumArtistSortName() {
        if (albumArtistSortName == null) {
            albumArtistSortName = new StringFilter();
        }
        return albumArtistSortName;
    }

    public void setAlbumArtistSortName(StringFilter albumArtistSortName) {
        this.albumArtistSortName = albumArtistSortName;
    }

    public IntegerFilter getAlbumReleasedYear() {
        return albumReleasedYear;
    }

    public IntegerFilter albumReleasedYear() {
        if (albumReleasedYear == null) {
            albumReleasedYear = new IntegerFilter();
        }
        return albumReleasedYear;
    }

    public void setAlbumReleasedYear(IntegerFilter albumReleasedYear) {
        this.albumReleasedYear = albumReleasedYear;
    }

    public StringFilter getGenreName() {
        return genreName;
    }

    public StringFilter genreName() {
        if (genreName == null) {
            genreName = new StringFilter();
        }
        return genreName;
    }

    public void setGenreName(StringFilter genreName) {
        this.genreName = genreName;
    }

    public StringFilter getGenreSortName() {
        return genreSortName;
    }

    public StringFilter genreSortName() {
        if (genreSortName == null) {
            genreSortName = new StringFilter();
        }
        return genreSortName;
    }

    public void setGenreSortName(StringFilter genreSortName) {
        this.genreSortName = genreSortName;
    }

    public IntegerFilter getTrackNumber() {
        return trackNumber;
    }

    public IntegerFilter trackNumber() {
        if (trackNumber == null) {
            trackNumber = new IntegerFilter();
        }
        return trackNumber;
    }

    public void setTrackNumber(IntegerFilter trackNumber) {
        this.trackNumber = trackNumber;
    }

    public IntegerFilter getPlaybackLength() {
        return playbackLength;
    }

    public IntegerFilter playbackLength() {
        if (playbackLength == null) {
            playbackLength = new IntegerFilter();
        }
        return playbackLength;
    }

    public void setPlaybackLength(IntegerFilter playbackLength) {
        this.playbackLength = playbackLength;
    }

    public IntegerFilter getBitRate() {
        return bitRate;
    }

    public IntegerFilter bitRate() {
        if (bitRate == null) {
            bitRate = new IntegerFilter();
        }
        return bitRate;
    }

    public void setBitRate(IntegerFilter bitRate) {
        this.bitRate = bitRate;
    }

    public InstantFilter getCreatedOn() {
        return createdOn;
    }

    public InstantFilter createdOn() {
        if (createdOn == null) {
            createdOn = new InstantFilter();
        }
        return createdOn;
    }

    public void setCreatedOn(InstantFilter createdOn) {
        this.createdOn = createdOn;
    }

    public BooleanFilter getTagVersion1() {
        return tagVersion1;
    }

    public BooleanFilter tagVersion1() {
        if (tagVersion1 == null) {
            tagVersion1 = new BooleanFilter();
        }
        return tagVersion1;
    }

    public void setTagVersion1(BooleanFilter tagVersion1) {
        this.tagVersion1 = tagVersion1;
    }

    public BooleanFilter getTagVersion2() {
        return tagVersion2;
    }

    public BooleanFilter tagVersion2() {
        if (tagVersion2 == null) {
            tagVersion2 = new BooleanFilter();
        }
        return tagVersion2;
    }

    public void setTagVersion2(BooleanFilter tagVersion2) {
        this.tagVersion2 = tagVersion2;
    }

    public LongFilter getTypeId() {
        return typeId;
    }

    public LongFilter typeId() {
        if (typeId == null) {
            typeId = new LongFilter();
        }
        return typeId;
    }

    public void setTypeId(LongFilter typeId) {
        this.typeId = typeId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final TrackCriteria that = (TrackCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(filePath, that.filePath) &&
            Objects.equals(subfolder, that.subfolder) &&
            Objects.equals(name, that.name) &&
            Objects.equals(sortName, that.sortName) &&
            Objects.equals(artistName, that.artistName) &&
            Objects.equals(artistSortName, that.artistSortName) &&
            Objects.equals(albumName, that.albumName) &&
            Objects.equals(albumSortName, that.albumSortName) &&
            Objects.equals(albumArtistName, that.albumArtistName) &&
            Objects.equals(albumArtistSortName, that.albumArtistSortName) &&
            Objects.equals(albumReleasedYear, that.albumReleasedYear) &&
            Objects.equals(genreName, that.genreName) &&
            Objects.equals(genreSortName, that.genreSortName) &&
            Objects.equals(trackNumber, that.trackNumber) &&
            Objects.equals(playbackLength, that.playbackLength) &&
            Objects.equals(bitRate, that.bitRate) &&
            Objects.equals(createdOn, that.createdOn) &&
            Objects.equals(tagVersion1, that.tagVersion1) &&
            Objects.equals(tagVersion2, that.tagVersion2) &&
            Objects.equals(typeId, that.typeId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            filePath,
            subfolder,
            name,
            sortName,
            artistName,
            artistSortName,
            albumName,
            albumSortName,
            albumArtistName,
            albumArtistSortName,
            albumReleasedYear,
            genreName,
            genreSortName,
            trackNumber,
            playbackLength,
            bitRate,
            createdOn,
            tagVersion1,
            tagVersion2,
            typeId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TrackCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (filePath != null ? "filePath=" + filePath + ", " : "") +
            (subfolder != null ? "subfolder=" + subfolder + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (sortName != null ? "sortName=" + sortName + ", " : "") +
            (artistName != null ? "artistName=" + artistName + ", " : "") +
            (artistSortName != null ? "artistSortName=" + artistSortName + ", " : "") +
            (albumName != null ? "albumName=" + albumName + ", " : "") +
            (albumSortName != null ? "albumSortName=" + albumSortName + ", " : "") +
            (albumArtistName != null ? "albumArtistName=" + albumArtistName + ", " : "") +
            (albumArtistSortName != null ? "albumArtistSortName=" + albumArtistSortName + ", " : "") +
            (albumReleasedYear != null ? "albumReleasedYear=" + albumReleasedYear + ", " : "") +
            (genreName != null ? "genreName=" + genreName + ", " : "") +
            (genreSortName != null ? "genreSortName=" + genreSortName + ", " : "") +
            (trackNumber != null ? "trackNumber=" + trackNumber + ", " : "") +
            (playbackLength != null ? "playbackLength=" + playbackLength + ", " : "") +
            (bitRate != null ? "bitRate=" + bitRate + ", " : "") +
            (createdOn != null ? "createdOn=" + createdOn + ", " : "") +
            (tagVersion1 != null ? "tagVersion1=" + tagVersion1 + ", " : "") +
            (tagVersion2 != null ? "tagVersion2=" + tagVersion2 + ", " : "") +
            (typeId != null ? "typeId=" + typeId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
