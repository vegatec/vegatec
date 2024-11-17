package net.vegatec.crm.query.service.mapper;

import net.vegatec.crm.query.service.mapper.ProductTypeMapper;
import org.junit.jupiter.api.BeforeEach;

class ProductTypeMapperTest {

    private ProductTypeMapper productTypeMapper;

    @BeforeEach
    public void setUp() {
        productTypeMapper = new ProductTypeMapperImpl();
    }
}
