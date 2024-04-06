package net.vegatec.media_library.query.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A TrackType.
 */
@Entity
@Table(name = "track_type")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "tracktype")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TrackType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String name;

    @Column(name = "credits_needed")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer creditsNeeded;

    @Column(name = "vip_credits_needed")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer vipCreditsNeeded;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TrackType id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public TrackType name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCreditsNeeded() {
        return this.creditsNeeded;
    }

    public TrackType creditsNeeded(Integer creditsNeeded) {
        this.setCreditsNeeded(creditsNeeded);
        return this;
    }

    public void setCreditsNeeded(Integer creditsNeeded) {
        this.creditsNeeded = creditsNeeded;
    }

    public Integer getVipCreditsNeeded() {
        return this.vipCreditsNeeded;
    }

    public TrackType vipCreditsNeeded(Integer vipCreditsNeeded) {
        this.setVipCreditsNeeded(vipCreditsNeeded);
        return this;
    }

    public void setVipCreditsNeeded(Integer vipCreditsNeeded) {
        this.vipCreditsNeeded = vipCreditsNeeded;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TrackType)) {
            return false;
        }
        return getId() != null && getId().equals(((TrackType) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TrackType{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", creditsNeeded=" + getCreditsNeeded() +
            ", vipCreditsNeeded=" + getVipCreditsNeeded() +
            "}";
    }
}
