package net.vegatec.media_library.query.service.mapper;

import net.vegatec.media_library.query.domain.Genre;
import net.vegatec.media_library.query.domain.Track;
import net.vegatec.media_library.query.service.dto.GenreDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link Track} and its DTO {@link GenreDTO}.
 */
@Mapper(componentModel = "spring")
public interface GenreMapper extends EntityMapper<GenreDTO, Genre> {
    GenreDTO toDto(Genre s);
}
