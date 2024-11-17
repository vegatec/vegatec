package net.vegatec.crm.query.domain;

import static net.vegatec.crm.query.domain.BusinessTestSamples.*;
import static net.vegatec.crm.query.domain.ProductTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashSet;
import java.util.Set;

import net.vegatec.crm.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BusinessTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Business.class);
        Business business1 = getBusinessSample1();
        Business business2 = new Business();
        assertThat(business1).isNotEqualTo(business2);

        business2.setId(business1.getId());
        assertThat(business1).isEqualTo(business2);

        business2 = getBusinessSample2();
        assertThat(business1).isNotEqualTo(business2);
    }

    @Test
    void productsTest() throws Exception {
        Business business = getBusinessRandomSampleGenerator();
        Product productBack = getProductRandomSampleGenerator();

        business.addProduct(productBack);
        assertThat(business.getProducts()).containsOnly(productBack);
        assertThat(productBack.getLocatedAt()).isEqualTo(business);

        business.removeProduct(productBack);
        assertThat(business.getProducts()).doesNotContain(productBack);
        assertThat(productBack.getLocatedAt()).isNull();

        business.products(new HashSet<>(Set.of(productBack)));
        assertThat(business.getProducts()).containsOnly(productBack);
        assertThat(productBack.getLocatedAt()).isEqualTo(business);

        business.setProducts(new HashSet<>());
        assertThat(business.getProducts()).doesNotContain(productBack);
        assertThat(productBack.getLocatedAt()).isNull();
    }
}
