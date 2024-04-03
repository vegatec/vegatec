package net.vegatec.media_library.query.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A MusicRequest.
 */
@Entity
@Table(name = "music_requests")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "musicrequest")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MusicRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "song")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String song;

    @Column(name = "artist")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String artist;

    @Column(name = "album")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String album;

    @Column(name = "genre")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String genre;

    @Column(name = "requested_by")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String requestedBy;

    @Column(name = "requested_on")
    private Instant requestedOn;

    @Column(name = "url")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String url;

    @Column(name = "done")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean done;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public MusicRequest id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSong() {
        return this.song;
    }

    public MusicRequest song(String song) {
        this.setSong(song);
        return this;
    }

    public void setSong(String song) {
        this.song = song;
    }

    public String getArtist() {
        return this.artist;
    }

    public MusicRequest artist(String artist) {
        this.setArtist(artist);
        return this;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return this.album;
    }

    public MusicRequest album(String album) {
        this.setAlbum(album);
        return this;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getGenre() {
        return this.genre;
    }

    public MusicRequest genre(String genre) {
        this.setGenre(genre);
        return this;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getRequestedBy() {
        return this.requestedBy;
    }

    public MusicRequest requestedBy(String requestedBy) {
        this.setRequestedBy(requestedBy);
        return this;
    }

    public void setRequestedBy(String requestedBy) {
        this.requestedBy = requestedBy;
    }

    public Instant getRequestedOn() {
        return this.requestedOn;
    }

    public MusicRequest requestedOn(Instant requestedOn) {
        this.setRequestedOn(requestedOn);
        return this;
    }

    public void setRequestedOn(Instant requestedOn) {
        this.requestedOn = requestedOn;
    }

    public String getUrl() {
        return this.url;
    }

    public MusicRequest url(String url) {
        this.setUrl(url);
        return this;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Boolean getDone() {
        return this.done;
    }

    public MusicRequest done(Boolean done) {
        this.setDone(done);
        return this;
    }

    public void setDone(Boolean done) {
        this.done = done;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MusicRequest)) {
            return false;
        }
        return getId() != null && getId().equals(((MusicRequest) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MusicRequest{" +
            "id=" + getId() +
            ", song='" + getSong() + "'" +
            ", artist='" + getArtist() + "'" +
            ", album='" + getAlbum() + "'" +
            ", genre='" + getGenre() + "'" +
            ", requestedBy='" + getRequestedBy() + "'" +
            ", requestedOn='" + getRequestedOn() + "'" +
            ", url='" + getUrl() + "'" +
            ", done='" + getDone() + "'" +
            "}";
    }
}
