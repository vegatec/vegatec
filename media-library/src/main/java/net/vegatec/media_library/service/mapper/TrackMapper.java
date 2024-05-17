package net.vegatec.media_library.service.mapper;

import net.vegatec.media_library.domain.Artist;
import net.vegatec.media_library.domain.Genre;
import net.vegatec.media_library.domain.Track;
import net.vegatec.media_library.service.dto.TrackDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

/**
 * Mapper for the entity {@link Track} and its DTO {@link TrackDTO}.
 */
//@Mapper(componentModel = "spring", uses = { TrackTypeMapper.class, ArtistMapper.class, AlbumMapper.class, GenreMapper.class })
@Mapper(componentModel = "spring", uses = { TrackTypeMapper.class,  AlbumMapper.class })
public interface TrackMapper extends EntityMapper<TrackDTO, Track> {
    @Mappings(
        {
            @Mapping(target = "type", source = "type", qualifiedByName = "name"),
            @Mapping(target = "artist", source = "artist.name"),
            @Mapping(target = "album", source = "album"),
            @Mapping(target = "genre", source = "genre.name"),
        }
    )
    TrackDTO toDto(Track s);


    @Mappings(
        {
      //      @Mapping(target = "type", source = "type", qualifiedByName = "name"),
            @Mapping(target = "artist.name", source = "artist"),
            @Mapping(target = "album", source = "album"),
            @Mapping(target = "genre.name", source = "genre"),
        }
    )
    Track toEntity(TrackDTO dto);


//    default Artist mapArtist(String name) {
//        return new Artist(name);
//    }

    default Genre mapGenre(String genre) {
        return new Genre(genre);
    }
}
