package net.vegatec.crm.domain;

import static net.vegatec.crm.domain.BusinessTestSamples.*;
import static net.vegatec.crm.domain.ProductTestSamples.*;
import static net.vegatec.crm.domain.ProductTypeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashSet;
import java.util.Set;

import net.vegatec.crm.query.domain.Business;
import net.vegatec.crm.query.domain.Product;
import net.vegatec.crm.query.domain.ProductType;
import net.vegatec.crm.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProductTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Product.class);
        Product product1 = getProductSample1();
        Product product2 = new Product();
        assertThat(product1).isNotEqualTo(product2);

        product2.setId(product1.getId());
        assertThat(product1).isEqualTo(product2);

        product2 = getProductSample2();
        assertThat(product1).isNotEqualTo(product2);
    }

    @Test
    void typeTest() throws Exception {
        Product product = getProductRandomSampleGenerator();
        ProductType productTypeBack = getProductTypeRandomSampleGenerator();

        product.setType(productTypeBack);
        assertThat(product.getType()).isEqualTo(productTypeBack);

        product.type(null);
        assertThat(product.getType()).isNull();
    }

    @Test
    void componentsTest() throws Exception {
        Product product = getProductRandomSampleGenerator();
        Product productBack = getProductRandomSampleGenerator();

        product.addComponent(productBack);
        assertThat(product.getComponents()).containsOnly(productBack);
        assertThat(productBack.getComponentOf()).isEqualTo(product);

        product.removeComponent(productBack);
        assertThat(product.getComponents()).doesNotContain(productBack);
        assertThat(productBack.getComponentOf()).isNull();

        product.components(new HashSet<>(Set.of(productBack)));
        assertThat(product.getComponents()).containsOnly(productBack);
        assertThat(productBack.getComponentOf()).isEqualTo(product);

        product.setComponents(new HashSet<>());
        assertThat(product.getComponents()).doesNotContain(productBack);
        assertThat(productBack.getComponentOf()).isNull();
    }

    @Test
    void locatedAtTest() throws Exception {
        Product product = getProductRandomSampleGenerator();
        Business businessBack = getBusinessRandomSampleGenerator();

        product.setLocatedAt(businessBack);
        assertThat(product.getLocatedAt()).isEqualTo(businessBack);

        product.locatedAt(null);
        assertThat(product.getLocatedAt()).isNull();
    }

    @Test
    void componentOfTest() throws Exception {
        Product product = getProductRandomSampleGenerator();
        Product productBack = getProductRandomSampleGenerator();

        product.setComponentOf(productBack);
        assertThat(product.getComponentOf()).isEqualTo(productBack);

        product.componentOf(null);
        assertThat(product.getComponentOf()).isNull();
    }
}
