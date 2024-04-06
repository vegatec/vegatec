package net.vegatec.media_library.query.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Album entity.
 */
public class GenreDTO implements Serializable {

    @NotNull
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        GenreDTO albumDTO = (GenreDTO) o;

        if (!Objects.equals(hashCode(), albumDTO.hashCode())) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "GenreDTO{" + ", name='" + name + "'" + '}';
    }
}
