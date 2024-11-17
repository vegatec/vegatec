package net.vegatec.crm.query.service.mapper;

import net.vegatec.crm.query.service.mapper.BusinessMapper;
import org.junit.jupiter.api.BeforeEach;

class BusinessMapperTest {

    private BusinessMapper businessMapper;

    @BeforeEach
    public void setUp() {
        businessMapper = new BusinessMapperImpl();
    }
}
