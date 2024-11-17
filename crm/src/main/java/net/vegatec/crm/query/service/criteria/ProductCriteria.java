package net.vegatec.crm.query.service.criteria;

import java.io.Serializable;
import java.util.Objects;

import net.vegatec.crm.query.domain.Product;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link Product} entity. This class is used
 * in {@link net.vegatec.crm.web.rest.ProductResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /products?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProductCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter model;

    private StringFilter serialNumber;

    private StringFilter manufacturer;

    private LocalDateFilter createdOn;

    private LongFilter typeId;

    private LongFilter componentsId;

    private LongFilter locatedAtId;

    private LongFilter componentOfId;

    private Boolean distinct;

    public ProductCriteria() {}

    public ProductCriteria(ProductCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.model = other.model == null ? null : other.model.copy();
        this.serialNumber = other.serialNumber == null ? null : other.serialNumber.copy();
        this.manufacturer = other.manufacturer == null ? null : other.manufacturer.copy();
        this.createdOn = other.createdOn == null ? null : other.createdOn.copy();
        this.typeId = other.typeId == null ? null : other.typeId.copy();
        this.componentsId = other.componentsId == null ? null : other.componentsId.copy();
        this.locatedAtId = other.locatedAtId == null ? null : other.locatedAtId.copy();
        this.componentOfId = other.componentOfId == null ? null : other.componentOfId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public ProductCriteria copy() {
        return new ProductCriteria(this);
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

    public StringFilter getName() {
        return name;
    }

    public StringFilter name() {
        if (name == null) {
            name = new StringFilter();
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getModel() {
        return model;
    }

    public StringFilter model() {
        if (model == null) {
            model = new StringFilter();
        }
        return model;
    }

    public void setModel(StringFilter model) {
        this.model = model;
    }

    public StringFilter getSerialNumber() {
        return serialNumber;
    }

    public StringFilter serialNumber() {
        if (serialNumber == null) {
            serialNumber = new StringFilter();
        }
        return serialNumber;
    }

    public void setSerialNumber(StringFilter serialNumber) {
        this.serialNumber = serialNumber;
    }

    public StringFilter getManufacturer() {
        return manufacturer;
    }

    public StringFilter manufacturer() {
        if (manufacturer == null) {
            manufacturer = new StringFilter();
        }
        return manufacturer;
    }

    public void setManufacturer(StringFilter manufacturer) {
        this.manufacturer = manufacturer;
    }

    public LocalDateFilter getCreatedOn() {
        return createdOn;
    }

    public LocalDateFilter createdOn() {
        if (createdOn == null) {
            createdOn = new LocalDateFilter();
        }
        return createdOn;
    }

    public void setCreatedOn(LocalDateFilter createdOn) {
        this.createdOn = createdOn;
    }

    public LongFilter getTypeId() {
        return typeId;
    }

    public LongFilter typeId() {
        if (typeId == null) {
            typeId = new LongFilter();
        }
        return typeId;
    }

    public void setTypeId(LongFilter typeId) {
        this.typeId = typeId;
    }

    public LongFilter getComponentsId() {
        return componentsId;
    }

    public LongFilter componentsId() {
        if (componentsId == null) {
            componentsId = new LongFilter();
        }
        return componentsId;
    }

    public void setComponentsId(LongFilter componentsId) {
        this.componentsId = componentsId;
    }

    public LongFilter getLocatedAtId() {
        return locatedAtId;
    }

    public LongFilter locatedAtId() {
        if (locatedAtId == null) {
            locatedAtId = new LongFilter();
        }
        return locatedAtId;
    }

    public void setLocatedAtId(LongFilter locatedAtId) {
        this.locatedAtId = locatedAtId;
    }

    public LongFilter getComponentOfId() {
        return componentOfId;
    }

    public LongFilter componentOfId() {
        if (componentOfId == null) {
            componentOfId = new LongFilter();
        }
        return componentOfId;
    }

    public void setComponentOfId(LongFilter componentOfId) {
        this.componentOfId = componentOfId;
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
        final ProductCriteria that = (ProductCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(model, that.model) &&
            Objects.equals(serialNumber, that.serialNumber) &&
            Objects.equals(manufacturer, that.manufacturer) &&
            Objects.equals(createdOn, that.createdOn) &&
            Objects.equals(typeId, that.typeId) &&
            Objects.equals(componentsId, that.componentsId) &&
            Objects.equals(locatedAtId, that.locatedAtId) &&
            Objects.equals(componentOfId, that.componentOfId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            name,
            model,
            serialNumber,
            manufacturer,
            createdOn,
            typeId,
            componentsId,
            locatedAtId,
            componentOfId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (model != null ? "model=" + model + ", " : "") +
            (serialNumber != null ? "serialNumber=" + serialNumber + ", " : "") +
            (manufacturer != null ? "manufacturer=" + manufacturer + ", " : "") +
            (createdOn != null ? "createdOn=" + createdOn + ", " : "") +
            (typeId != null ? "typeId=" + typeId + ", " : "") +
            (componentsId != null ? "componentsId=" + componentsId + ", " : "") +
            (locatedAtId != null ? "locatedAtId=" + locatedAtId + ", " : "") +
            (componentOfId != null ? "componentOfId=" + componentOfId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
