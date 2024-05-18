package net.vegatec.media_library.service.mapper;

import net.vegatec.media_library.domain.Artist;
import net.vegatec.media_library.domain.Track;
import net.vegatec.media_library.service.dto.ArtistDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link Track} and its DTO {@link ArtistDTO}.
 */
@Mapper(componentModel = "spring")
public interface ArtistMapper extends EntityMapper<ArtistDTO, Artist> {
    ArtistDTO toDto(Artist s);

    Artist toEntity(ArtistDTO dto);
}
