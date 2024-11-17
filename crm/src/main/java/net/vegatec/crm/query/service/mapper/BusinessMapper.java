package net.vegatec.crm.query.service.mapper;

import net.vegatec.crm.query.domain.Business;
import net.vegatec.crm.query.service.dto.BusinessDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Business} and its DTO {@link BusinessDTO}.
 */
@Mapper(componentModel = "spring")
public interface BusinessMapper extends EntityMapper<BusinessDTO, Business> {}
