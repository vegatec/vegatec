package net.vegatec.crm.service.mapper;

import net.vegatec.crm.domain.Business;
import net.vegatec.crm.domain.Product;
import net.vegatec.crm.domain.ProductType;
import net.vegatec.crm.service.dto.BusinessDTO;
import net.vegatec.crm.service.dto.ProductDTO;
import net.vegatec.crm.service.dto.ProductTypeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Product} and its DTO {@link ProductDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProductMapper extends EntityMapper<ProductDTO, Product> {
    @Mapping(target = "type", source = "type", qualifiedByName = "productTypeName")
    @Mapping(target = "locatedAt", source = "locatedAt", qualifiedByName = "businessName")
    @Mapping(target = "componentOf", source = "componentOf", qualifiedByName = "productId")
    ProductDTO toDto(Product s);

    @Named("productTypeName")
    @BeanMapping(ignoreByDefault = false)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    ProductTypeDTO toDtoProductTypeName(ProductType productType);

    @Named("productId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ProductDTO toDtoProductId(Product product);

    @Named("businessName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    BusinessDTO toDtoBusinessName(Business business);
}
