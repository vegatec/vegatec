package net.vegatec.media_library.query.service.mapper;

import net.vegatec.media_library.query.domain.Artist;
import net.vegatec.media_library.query.domain.Track;
import net.vegatec.media_library.query.service.dto.ArtistDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link Track} and its DTO {@link ArtistDTO}.
 */
@Mapper(componentModel = "spring")
public interface ArtistMapper extends EntityMapper<ArtistDTO, Artist> {
    ArtistDTO toDto(Artist s);
}
