package net.vegatec.media_library.repository;

import net.vegatec.media_library.domain.Track;
import net.vegatec.media_library.domain.embeddable._Album;
import net.vegatec.media_library.web.rest.dto.SearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


/**
 * Spring Data JPA repository for the Track entity.
 */


@Repository
public interface SearchSuggestionsRepository extends JpaRepository<Track, Long> {
    @Query(value =
        "select distinct track.artist.name  " +
            "from Track track  " +
            "where subfolder='outbox' and :#{#term} is null or track.artist.sortName like %:#{#term}%  " +
            "order by track.artist.name"
    )
    Page<String> findArtistSuggestions(Pageable pageRequest, @Param("term") String term);

    @Query(value =
        "select distinct track.album.name   " +
            "from Track track  " +
            "where subfolder='outbox' and :#{#term} is null or track.album.sortName like %:#{#term}%  " +
            "order by track.album.name"
    )
    Page<String> findAlbumSuggestions(Pageable pageRequest, @Param("term") String term);

    @Query(value =
        "select distinct track.name  " +
            "from Track track  " +
            "where subfolder='outbox' and :#{#term} is null or track.sortName like %:#{#term}%  " +
            "order by track.name"

    )
    Page<String> findTrackSuggestions(Pageable pageRequest, @Param("term") String term);
}
