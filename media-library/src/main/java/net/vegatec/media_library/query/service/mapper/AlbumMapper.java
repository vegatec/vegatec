package net.vegatec.media_library.query.service.mapper;

import net.vegatec.media_library.query.domain.Album;
import net.vegatec.media_library.query.domain.Track;
import net.vegatec.media_library.query.service.dto.AlbumDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity {@link Track} and its DTO {@link AlbumDTO}.
 */
@Mapper(componentModel = "spring", uses = { ArtistMapper.class })
public interface AlbumMapper extends EntityMapper<AlbumDTO, Album> {
    @Mapping(target = "artist", source = "artist")
    AlbumDTO toDto(Album s);
}
