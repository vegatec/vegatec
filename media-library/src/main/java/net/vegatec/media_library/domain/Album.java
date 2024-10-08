package net.vegatec.media_library.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.io.Serializable;
import java.text.Normalizer;
import java.util.Objects;

/**
 * A Album.
 */
@Embeddable
public class Album implements Serializable {

    protected Album() {}
    public Album(String name, String artistName, int releasedYear) {
        this.setName(name);
        this.setArtist( new Artist(artistName));
        this.setReleasedYear(releasedYear);
    }

    public int getId() {
        return hashCode();
    }

    //    @Column(name = "name")
//    @Access(AccessType.FIELD)
    private String name;
    private String sortName;

    public String getName() {
        return name;
    }

    protected void setName(String name) {
        this.name = name;
        this.sortName = (name == null)? null:
            Normalizer.normalize(name.toLowerCase().replaceAll("\\s+",""), Normalizer.Form.NFKD)
                .replaceAll("\\p{M}", "");
    }




    //    @Column(name = "released_year")
//    @Access(AccessType.FIELD)
    private Integer releasedYear;

    public Integer getReleasedYear() {
        return releasedYear;
    }
    protected void setReleasedYear(Integer releasedYear) {
        this.releasedYear = releasedYear;
    }

    @JsonIgnore
    @Column(name = "subfolder", updatable = false, insertable = false)
    @Access(AccessType.FIELD)
    private String subfolder;

    public String getArtworkPath() {
        return String.format(
            "media/%s/%s/%s",
            subfolder,
            Integer.toHexString(artist == null ? 0: artist.hashCode()),
            Integer.toHexString(hashCode())
        );
    }

    @Embedded
    @AttributeOverrides(
        value = {
            @AttributeOverride(name = "name", column = @Column(name = "album_artist_name")),
            @AttributeOverride(name = "sortName", column = @Column(name = "album_artist_sort_name")),
        }
    )
    @Access(AccessType.FIELD)
    private Artist artist;

    public Artist getArtist() {
        return artist;
    }
    public void setArtist(Artist artist) {
        this.artist = artist;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, artist.getName(), releasedYear);
       // return name.hashCode();
    }

    @Override
    public String toString() {
        return (
            "Album{" +
                ", name='" +
                name +
                "'" +
                //      ", sortName='" + sortName + "'" +
                ", artworkPath='" +
                getArtworkPath() +
                "'" +
                ", releasedOn='" +
                releasedYear +
                "'" +
                '}'
        );
    }
}
