package net.vegatec.media_library.query.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
//import javax.persistence.Access;
//import javax.persistence.AccessType;
//import javax.persistence.Column;
//import javax.persistence.Embeddable;
import java.io.Serializable;
import java.text.Normalizer;
import java.util.Objects;

@Embeddable
public class Genre implements Serializable {

    public int getId() {
        return hashCode();
    }

    @Access(AccessType.FIELD)
    @Column(name = "name", insertable = false, updatable = false)
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return (
            "Album{" +
            ", name='" +
            name +
            "'" +
            //         ", sortName='" + sortName + "'" +
            '}'
        );
    }
}
