package net.vegatec.media_library.service.mapper;

import net.vegatec.media_library.domain.Genre;
import net.vegatec.media_library.domain.Track;
import net.vegatec.media_library.service.dto.GenreDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link Track} and its DTO {@link GenreDTO}.
 */
@Mapper(componentModel = "spring")
public interface GenreMapper extends EntityMapper<GenreDTO, Genre> {
    GenreDTO toDto(Genre s);
}
