package net.vegatec.media_library.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
//import javax.persistence.Access;
//import javax.persistence.AccessType;
//import javax.persistence.Column;
//import javax.persistence.Embeddable;
import jakarta.persistence.*;
import java.io.Serializable;
import java.text.Normalizer;
import java.util.Objects;

@Embeddable
public class Artist implements Serializable {

    public int getId() {
        return hashCode();
    }

    @Access(AccessType.FIELD)
    @Column(name = "name", insertable = false, updatable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Artist() {}

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "Artist{" + ", name='" + name + "'" + '}';
    }
}
