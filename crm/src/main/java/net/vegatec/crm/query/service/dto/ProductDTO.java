package net.vegatec.crm.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link net.vegatec.crm.domain.Product} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProductDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String model;

    @NotNull
    private String serialNumber;

    private String manufacturer;

    @NotNull
    private LocalDate createdOn;

    private ProductTypeDTO type;

    private BusinessDTO locatedAt;

    private ProductDTO componentOf;

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

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public LocalDate getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDate createdOn) {
        this.createdOn = createdOn;
    }

    public ProductTypeDTO getType() {
        return type;
    }

    public void setType(ProductTypeDTO type) {
        this.type = type;
    }

    public BusinessDTO getLocatedAt() {
        return locatedAt;
    }

    public void setLocatedAt(BusinessDTO locatedAt) {
        this.locatedAt = locatedAt;
    }

    public ProductDTO getComponentOf() {
        return componentOf;
    }

    public void setComponentOf(ProductDTO componentOf) {
        this.componentOf = componentOf;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductDTO)) {
            return false;
        }

        ProductDTO productDTO = (ProductDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, productDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", model='" + getModel() + "'" +
            ", serialNumber='" + getSerialNumber() + "'" +
            ", manufacturer='" + getManufacturer() + "'" +
            ", createdOn='" + getCreatedOn() + "'" +
            ", type=" + getType() +
            ", locatedAt=" + getLocatedAt() +
            ", componentOf=" + getComponentOf() +
            "}";
    }
}
