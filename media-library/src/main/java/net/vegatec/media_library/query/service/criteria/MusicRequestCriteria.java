package net.vegatec.media_library.query.service.criteria;

import java.io.Serializable;
import java.util.Objects;

import net.vegatec.media_library.query.domain.MusicRequest;
import net.vegatec.media_library.query.web.rest.MusicRequestResource;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link MusicRequest} entity. This class is used
 * in {@link MusicRequestResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /music-requests?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MusicRequestCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter song;

    private StringFilter artist;

    private StringFilter album;

    private StringFilter genre;

    private StringFilter requestedBy;

    private InstantFilter requestedOn;

    private StringFilter url;

    private BooleanFilter done;

    private Boolean distinct;

    public MusicRequestCriteria() {}

    public MusicRequestCriteria(MusicRequestCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.song = other.song == null ? null : other.song.copy();
        this.artist = other.artist == null ? null : other.artist.copy();
        this.album = other.album == null ? null : other.album.copy();
        this.genre = other.genre == null ? null : other.genre.copy();
        this.requestedBy = other.requestedBy == null ? null : other.requestedBy.copy();
        this.requestedOn = other.requestedOn == null ? null : other.requestedOn.copy();
        this.url = other.url == null ? null : other.url.copy();
        this.done = other.done == null ? null : other.done.copy();
        this.distinct = other.distinct;
    }

    @Override
    public MusicRequestCriteria copy() {
        return new MusicRequestCriteria(this);
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

    public StringFilter getSong() {
        return song;
    }

    public StringFilter song() {
        if (song == null) {
            song = new StringFilter();
        }
        return song;
    }

    public void setSong(StringFilter song) {
        this.song = song;
    }

    public StringFilter getArtist() {
        return artist;
    }

    public StringFilter artist() {
        if (artist == null) {
            artist = new StringFilter();
        }
        return artist;
    }

    public void setArtist(StringFilter artist) {
        this.artist = artist;
    }

    public StringFilter getAlbum() {
        return album;
    }

    public StringFilter album() {
        if (album == null) {
            album = new StringFilter();
        }
        return album;
    }

    public void setAlbum(StringFilter album) {
        this.album = album;
    }

    public StringFilter getGenre() {
        return genre;
    }

    public StringFilter genre() {
        if (genre == null) {
            genre = new StringFilter();
        }
        return genre;
    }

    public void setGenre(StringFilter genre) {
        this.genre = genre;
    }

    public StringFilter getRequestedBy() {
        return requestedBy;
    }

    public StringFilter requestedBy() {
        if (requestedBy == null) {
            requestedBy = new StringFilter();
        }
        return requestedBy;
    }

    public void setRequestedBy(StringFilter requestedBy) {
        this.requestedBy = requestedBy;
    }

    public InstantFilter getRequestedOn() {
        return requestedOn;
    }

    public InstantFilter requestedOn() {
        if (requestedOn == null) {
            requestedOn = new InstantFilter();
        }
        return requestedOn;
    }

    public void setRequestedOn(InstantFilter requestedOn) {
        this.requestedOn = requestedOn;
    }

    public StringFilter getUrl() {
        return url;
    }

    public StringFilter url() {
        if (url == null) {
            url = new StringFilter();
        }
        return url;
    }

    public void setUrl(StringFilter url) {
        this.url = url;
    }

    public BooleanFilter getDone() {
        return done;
    }

    public BooleanFilter done() {
        if (done == null) {
            done = new BooleanFilter();
        }
        return done;
    }

    public void setDone(BooleanFilter done) {
        this.done = done;
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
        final MusicRequestCriteria that = (MusicRequestCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(song, that.song) &&
            Objects.equals(artist, that.artist) &&
            Objects.equals(album, that.album) &&
            Objects.equals(genre, that.genre) &&
            Objects.equals(requestedBy, that.requestedBy) &&
            Objects.equals(requestedOn, that.requestedOn) &&
            Objects.equals(url, that.url) &&
            Objects.equals(done, that.done) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, song, artist, album, genre, requestedBy, requestedOn, url, done, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MusicRequestCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (song != null ? "song=" + song + ", " : "") +
            (artist != null ? "artist=" + artist + ", " : "") +
            (album != null ? "album=" + album + ", " : "") +
            (genre != null ? "genre=" + genre + ", " : "") +
            (requestedBy != null ? "requestedBy=" + requestedBy + ", " : "") +
            (requestedOn != null ? "requestedOn=" + requestedOn + ", " : "") +
            (url != null ? "url=" + url + ", " : "") +
            (done != null ? "done=" + done + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
