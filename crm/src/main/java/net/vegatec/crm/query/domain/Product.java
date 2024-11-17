package net.vegatec.crm.query.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Product.
 */
@Entity
@Table(name = "product")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "model", nullable = false)
    private String model;

    @NotNull
    @Column(name = "serial_number", nullable = false)
    private String serialNumber;

    @Column(name = "manufacturer")
    private String manufacturer;

    @NotNull
    @Column(name = "created_on", nullable = false)
    private LocalDate createdOn;

    @ManyToOne(fetch = FetchType.LAZY)
    private ProductType type;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "componentOf")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "type", "components", "locatedAt", "componentOf" }, allowSetters = true)
    private Set<Product> components = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "products" }, allowSetters = true)
    private Business locatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "type", "components", "locatedAt", "componentOf" }, allowSetters = true)
    private Product componentOf;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Product id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Product name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    @PostLoad
    protected void updateName() {
        this.name = MessageFormat.format("{0} {1} {2} {3}", manufacturer, type.getName(), model, serialNumber);
    }

    public String getModel() {
        return this.model;
    }

    public Product model(String model) {
        this.setModel(model);
        return this;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getSerialNumber() {
        return this.serialNumber;
    }

    public Product serialNumber(String serialNumber) {
        this.setSerialNumber(serialNumber);
        return this;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getManufacturer() {
        return this.manufacturer;
    }

    public Product manufacturer(String manufacturer) {
        this.setManufacturer(manufacturer);
        return this;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public LocalDate getCreatedOn() {
        return this.createdOn;
    }

    public Product createdOn(LocalDate createdOn) {
        this.setCreatedOn(createdOn);
        return this;
    }

    public void setCreatedOn(LocalDate createdOn) {
        this.createdOn = createdOn;
    }

    public ProductType getType() {
        return this.type;
    }

    public void setType(ProductType productType) {
        this.type = productType;
    }

    public Product type(ProductType productType) {
        this.setType(productType);
        return this;
    }

    public Set<Product> getComponents() {
        return this.components;
    }

    public void setComponents(Set<Product> products) {
        if (this.components != null) {
            this.components.forEach(i -> i.setComponentOf(null));
        }
        if (products != null) {
            products.forEach(i -> i.setComponentOf(this));
        }
        this.components = products;
    }

    public Product components(Set<Product> products) {
        this.setComponents(products);
        return this;
    }

    public Product addComponent(Product product) {
        this.components.add(product);
        product.setComponentOf(this);
        return this;
    }

    public Product removeComponent(Product product) {
        this.components.remove(product);
        product.setComponentOf(null);
        return this;
    }

    public Business getLocatedAt() {
        return this.locatedAt;
    }

    public void setLocatedAt(Business business) {
        this.locatedAt = business;
    }

    public Product locatedAt(Business business) {
        this.setLocatedAt(business);
        return this;
    }

    public Product getComponentOf() {
        return this.componentOf;
    }

    public void setComponentOf(Product product) {
        this.componentOf = product;
    }

    public Product componentOf(Product product) {
        this.setComponentOf(product);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Product)) {
            return false;
        }
        return getId() != null && getId().equals(((Product) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Product{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", model='" + getModel() + "'" +
            ", serialNumber='" + getSerialNumber() + "'" +
            ", manufacturer='" + getManufacturer() + "'" +
            ", createdOn='" + getCreatedOn() + "'" +
            "}";
    }
}
