package net.vegatec.media_library.query.service.mapper;

import net.vegatec.media_library.query.domain.Track;
import net.vegatec.media_library.query.service.dto.TrackDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

/**
 * Mapper for the entity {@link Track} and its DTO {@link TrackDTO}.
 */
@Mapper(componentModel = "spring", uses = { TrackTypeMapper.class, ArtistMapper.class, AlbumMapper.class, GenreMapper.class })
public interface TrackMapper extends EntityMapper<TrackDTO, Track> {
    @Mappings(
        {
            @Mapping(target = "type", source = "type", qualifiedByName = "name"),
            @Mapping(target = "artist", source = "artist"),
            @Mapping(target = "album", source = "album"),
            @Mapping(target = "genre", source = "genre"),
        }
    )
    TrackDTO toDto(Track s);
}
