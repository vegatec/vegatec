package net.vegatec.crm.query.domain;

import static net.vegatec.crm.query.domain.ProductTypeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import net.vegatec.crm.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProductTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductType.class);
        ProductType productType1 = getProductTypeSample1();
        ProductType productType2 = new ProductType();
        assertThat(productType1).isNotEqualTo(productType2);

        productType2.setId(productType1.getId());
        assertThat(productType1).isEqualTo(productType2);

        productType2 = getProductTypeSample2();
        assertThat(productType1).isNotEqualTo(productType2);
    }
}
