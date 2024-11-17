package net.vegatec.crm.query.service.mapper;

import net.vegatec.crm.query.service.mapper.ProductMapper;
import org.junit.jupiter.api.BeforeEach;

class ProductMapperTest {

    private ProductMapper productMapper;

    @BeforeEach
    public void setUp() {
        productMapper = new ProductMapperImpl();
    }
}
