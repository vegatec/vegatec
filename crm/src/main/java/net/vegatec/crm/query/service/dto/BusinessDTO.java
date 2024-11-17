package net.vegatec.crm.query.service.dto;

import jakarta.validation.constraints.*;
import net.vegatec.crm.query.domain.Business;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link Business} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BusinessDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String address;

    @NotNull
    private String owner;

    private String contact;

    private String phone;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BusinessDTO)) {
            return false;
        }

        BusinessDTO businessDTO = (BusinessDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, businessDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BusinessDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", address='" + getAddress() + "'" +
            ", owner='" + getOwner() + "'" +
            ", contact='" + getContact() + "'" +
            ", phone='" + getPhone() + "'" +
            "}";
    }
}
