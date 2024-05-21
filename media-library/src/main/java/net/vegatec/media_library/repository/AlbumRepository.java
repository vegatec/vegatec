package net.vegatec.media_library.repository;

import net.vegatec.media_library.domain.Album;
import net.vegatec.media_library.domain.Track;
import net.vegatec.media_library.service.criteria.LibrarySearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data SQL repository for the Track entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AlbumRepository extends JpaRepository<Track, Long>, JpaSpecificationExecutor<Track> {
    //    @Query(
    //        value = "select distinct track.album  " +
    //        "from Track track  " +
    //        "where  ( " +
    //        "   ( :#{#searchCriteria.genreName} is null or track.genre.name = :#{#searchCriteria.genreName}) " +
    //        "and   ( :#{#searchCriteria.artistName} is null or track.artist.name = :#{#searchCriteria.artistName}) " +
    //        "and   ( :#{#searchCriteria.albumName} is null or track.album.name = :#{#searchCriteria.albumName}) " +
    //        "and   ( :#{#searchCriteria.trackName} is null or track.name = :#{#searchCriteria.trackName}) " +
    //        "and   ( :#{#searchCriteria.subfolder} is null or track.subfolder = :#{#searchCriteria.subfolder}) " +
    //        "and  ( ( :#{#searchCriteria.addedSince} is null or track.createdOn > :#{#searchCriteria.addedAfter})  " +
    //        "or   ( :#{#searchCriteria.releasedSince} is null or track.album.releasedYear > :#{#searchCriteria.releasedAfter}) )" +
    //        "and  ( :#{#searchCriteria.filter} is null or track.album.name like %:#{#searchCriteria.filter}% or track.album.artist.name like %:#{#searchCriteria.filter}% or track.artist.name like %:#{#searchCriteria.filter}% or track.name like %:#{#searchCriteria.filter}%   )  ) "
    //    )

    @Query(
        value = "select distinct track.album " +
        "from Track track  " +
        "where  ( " +
        "   ( :#{#searchCriteria.genreName} is null or track.genre.name = :#{#searchCriteria.genreName}) " +
        "and  ( :#{#searchCriteria.artistName} is null or track.artist.name = :#{#searchCriteria.artistName}) " +
        "and  ( :#{#searchCriteria.albumName} is null or track.album.name = :#{#searchCriteria.albumName}) " +
        "and  ( :#{#searchCriteria.albumArtistName} is null or track.album.artist.name = :#{#searchCriteria.albumArtistName}) " +
        "and  ( :#{#searchCriteria.trackName} is null or track.name = :#{#searchCriteria.trackName}) " +
        "and   ( :#{#searchCriteria.subfolder} is null or track.subfolder = :#{#searchCriteria.subfolder}) " +
        "and  ( :#{#searchCriteria.addedSince} is null or track.createdOn > :#{#searchCriteria.addedAfter})  " +
        "and  ( :#{#searchCriteria.releasedSince} is null or track.album.releasedYear > :#{#searchCriteria.releasedAfter}) " +
        "and  ( :#{#searchCriteria.filter} is null or track.album.name like %:#{#searchCriteria.filter}% or track.album.artist.name like %:#{#searchCriteria.filter}% or track.artist.name like %:#{#searchCriteria.filter}% or track.name like %:#{#searchCriteria.filter}%   )  ) "
    )
    Page<Album> findAll(@Param("searchCriteria") LibrarySearchCriteria searchCriteria, Pageable pageRequest);


}
