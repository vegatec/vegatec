package net.vegatec.crm.service.mapper;

import net.vegatec.crm.domain.Business;
import net.vegatec.crm.service.dto.BusinessDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Business} and its DTO {@link BusinessDTO}.
 */
@Mapper(componentModel = "spring")
public interface BusinessMapper extends EntityMapper<BusinessDTO, Business> {}
