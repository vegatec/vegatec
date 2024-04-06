package net.vegatec.media_library.query.service.mapper;

import net.vegatec.media_library.query.domain.TrackType;
import net.vegatec.media_library.query.service.dto.TrackTypeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TrackType} and its DTO {@link TrackTypeDTO}.
 */
@Mapper(componentModel = "spring")
public interface TrackTypeMapper extends EntityMapper<TrackTypeDTO, TrackType> {
    @Named("name")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    TrackTypeDTO toDtoName(TrackType trackType);
}
