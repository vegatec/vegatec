package net.vegatec.media_library.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Album entity.
 */
public class AlbumDTO implements Serializable {

    @NotNull
    private String name;

    private Integer releasedYear;

    private String artworkPath;

    private ArtistDTO artist;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArtworkPath() {
        return artworkPath;
    }

    public void setArtworkPath(String artworkPath) {
        this.artworkPath = artworkPath;
    }

    public Integer getReleasedYear() {
        return releasedYear;
    }

    public void setReleasedYear(Integer releasedYear) {
        this.releasedYear = releasedYear;
    }

    public ArtistDTO getArtist() {
        return artist;
    }

    public void setArtist(ArtistDTO artist) {
        this.artist = artist;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AlbumDTO albumDTO = (AlbumDTO) o;

        if (!Objects.equals(hashCode(), albumDTO.hashCode())) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, artist, releasedYear);
    }

    @Override
    public String toString() {
        return (
            "AlbumDTO{" + ", name='" + name + "'" + ", artworkPath='" + artworkPath + "'" + ", releasedYear='" + releasedYear + "'" + '}'
        );
    }
}
