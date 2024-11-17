package net.vegatec.crm.query.service.mapper;

import net.vegatec.crm.query.domain.ProductType;
import net.vegatec.crm.query.service.dto.ProductTypeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ProductType} and its DTO {@link ProductTypeDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProductTypeMapper extends EntityMapper<ProductTypeDTO, ProductType> {}
