package net.vegatec.media_library.query.repository;

import java.util.List;
import java.util.Optional;
import net.vegatec.media_library.query.domain.Track;
import net.vegatec.media_library.query.service.criteria.LibrarySearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Track entity.
 */
@Repository
public interface TrackRepository extends JpaRepository<Track, Long>, JpaSpecificationExecutor<Track> {
    default Optional<Track> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Track> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Track> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(value = "select track from Track track left join fetch track.type", countQuery = "select count(track) from Track track")
    Page<Track> findAllWithToOneRelationships(Pageable pageable);

    @Query("select track from Track track left join fetch track.type")
    List<Track> findAllWithToOneRelationships();

    @Query("select track from Track track left join fetch track.type where track.id =:id")
    Optional<Track> findOneWithToOneRelationships(@Param("id") Long id);

    @Query(
        value = "select  track  " +
        "from Track track  " +
        "where  ( " +
        "   ( :#{#searchCriteria.genreName} is null or track.genre.name = :#{#searchCriteria.genreName}) " +
        "and   ( :#{#searchCriteria.artistName} is null or track.artist.name = :#{#searchCriteria.artistName}) " +
        "and   ( :#{#searchCriteria.albumName} is null or track.album.name = :#{#searchCriteria.albumName}) " +
        "and   ( :#{#searchCriteria.albumArtistName} is null or track.album.artist.name = :#{#searchCriteria.albumArtistName}) " +
        "and   ( :#{#searchCriteria.trackName} is null or track.name = :#{#searchCriteria.trackName}) " +
        "and   ( :#{#searchCriteria.subfolder} is null or track.subfolder = :#{#searchCriteria.subfolder}) " +
        "and  ( :#{#searchCriteria.addedSince} is null or track.createdOn > :#{#searchCriteria.addedAfter})  " +
        "and  ( :#{#searchCriteria.releasedSince} is null or track.album.releasedYear > :#{#searchCriteria.releasedAfter}) " +
        "and  ( :#{#searchCriteria.filter} is null or track.album.name like %:#{#searchCriteria.filter}% or track.album.artist.name like %:#{#searchCriteria.filter}% or track.artist.name like %:#{#searchCriteria.filter}% or track.name like %:#{#searchCriteria.filter}%   )  ) "
    )
    Page<Track> findAll(@Param("searchCriteria") LibrarySearchCriteria searchCriteria, Pageable pageRequest);
}
