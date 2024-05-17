package net.vegatec.media_library.service.mapper;

import net.vegatec.media_library.domain.Album;
import net.vegatec.media_library.domain.Artist;
import net.vegatec.media_library.domain.Track;
import net.vegatec.media_library.service.dto.AlbumDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity {@link Track} and its DTO {@link AlbumDTO}.
 */
//@Mapper(componentModel = "spring", uses = { ArtistMapper.class })
@Mapper(componentModel = "spring", uses = { })
public interface AlbumMapper extends EntityMapper<AlbumDTO, Album> {
    @Mapping(target = "artist", source = "artist.name")
    AlbumDTO toDto(Album s);

    @Mapping(target = "artist.name", source = "artist")
    Album toEntity(AlbumDTO dto);

    default Artist mapArtist(String name) {
        return new Artist(name);
    }

}
