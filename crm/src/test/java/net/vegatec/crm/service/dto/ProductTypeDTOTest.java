package net.vegatec.crm.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import net.vegatec.crm.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProductTypeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductTypeDTO.class);
        ProductTypeDTO productTypeDTO1 = new ProductTypeDTO();
        productTypeDTO1.setId(1L);
        ProductTypeDTO productTypeDTO2 = new ProductTypeDTO();
        assertThat(productTypeDTO1).isNotEqualTo(productTypeDTO2);
        productTypeDTO2.setId(productTypeDTO1.getId());
        assertThat(productTypeDTO1).isEqualTo(productTypeDTO2);
        productTypeDTO2.setId(2L);
        assertThat(productTypeDTO1).isNotEqualTo(productTypeDTO2);
        productTypeDTO1.setId(null);
        assertThat(productTypeDTO1).isNotEqualTo(productTypeDTO2);
    }
}
