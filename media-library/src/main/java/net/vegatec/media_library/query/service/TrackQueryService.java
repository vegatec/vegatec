package net.vegatec.media_library.query.service;

import jakarta.persistence.criteria.JoinType;
import java.util.List;
import net.vegatec.media_library.query.domain.*; // for static metamodels
import net.vegatec.media_library.query.domain.Track;
import net.vegatec.media_library.query.repository.TrackRepository;
import net.vegatec.media_library.query.repository.search.TrackSearchRepository;
import net.vegatec.media_library.query.service.criteria.TrackCriteria;
import net.vegatec.media_library.query.service.dto.TrackDTO;
import net.vegatec.media_library.query.service.mapper.TrackMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Track} entities in the database.
 * The main input is a {@link TrackCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TrackDTO} or a {@link Page} of {@link TrackDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TrackQueryService extends QueryService<Track> {

    private final Logger log = LoggerFactory.getLogger(TrackQueryService.class);

    private final TrackRepository trackRepository;

    private final TrackMapper trackMapper;

    private final TrackSearchRepository trackSearchRepository;

    public TrackQueryService(TrackRepository trackRepository, TrackMapper trackMapper, TrackSearchRepository trackSearchRepository) {
        this.trackRepository = trackRepository;
        this.trackMapper = trackMapper;
        this.trackSearchRepository = trackSearchRepository;
    }

    /**
     * Return a {@link List} of {@link TrackDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TrackDTO> findByCriteria(TrackCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Track> specification = createSpecification(criteria);
        return trackMapper.toDto(trackRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link TrackDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TrackDTO> findByCriteria(TrackCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Track> specification = createSpecification(criteria);
        return trackRepository.findAll(specification, page).map(trackMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TrackCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Track> specification = createSpecification(criteria);
        return trackRepository.count(specification);
    }

    /**
     * Function to convert {@link TrackCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Track> createSpecification(TrackCriteria criteria) {
        Specification<Track> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Track_.id));
            }
            if (criteria.getFilePath() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFilePath(), Track_.filePath));
            }
            if (criteria.getSubfolder() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSubfolder(), Track_.subfolder));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Track_.name));
            }
            if (criteria.getSortName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSortName(), Track_.sortName));
            }
            //            if (criteria.getArtistName() != null) {
            //                specification = specification.and(buildStringSpecification(criteria.getArtistName(), Track_.artistName));
            //            }
            //            if (criteria.getArtistSortName() != null) {
            //                specification = specification.and(buildStringSpecification(criteria.getArtistSortName(), Track_.artistSortName));
            //            }
            //            if (criteria.getAlbumName() != null) {
            //                specification = specification.and(buildStringSpecification(criteria.getAlbumName(), Track_.albumName));
            //            }
            //            if (criteria.getAlbumSortName() != null) {
            //                specification = specification.and(buildStringSpecification(criteria.getAlbumSortName(), Track_.albumSortName));
            //            }
            //            if (criteria.getAlbumArtistName() != null) {
            //                specification = specification.and(buildStringSpecification(criteria.getAlbumArtistName(), Track_.albumArtistName));
            //            }
            //            if (criteria.getAlbumArtistSortName() != null) {
            //                specification = specification.and(buildStringSpecification(criteria.getAlbumArtistSortName(), Track_.albumArtistSortName));
            //            }
            //            if (criteria.getAlbumReleasedYear() != null) {
            //                specification = specification.and(buildRangeSpecification(criteria.getAlbumReleasedYear(), Track_.albumReleasedYear));
            //            }
            //            if (criteria.getGenreName() != null) {
            //                specification = specification.and(buildStringSpecification(criteria.getGenreName(), Track_.genreName));
            //            }
            //            if (criteria.getGenreSortName() != null) {
            //                specification = specification.and(buildStringSpecification(criteria.getGenreSortName(), Track_.genreSortName));
            //            }
            if (criteria.getTrackNumber() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTrackNumber(), Track_.trackNumber));
            }
            if (criteria.getPlaybackLength() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPlaybackLength(), Track_.playbackLength));
            }
            if (criteria.getBitRate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getBitRate(), Track_.bitRate));
            }
            if (criteria.getCreatedOn() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedOn(), Track_.createdOn));
            }
            if (criteria.getTagVersion1() != null) {
                specification = specification.and(buildSpecification(criteria.getTagVersion1(), Track_.tagVersion1));
            }
            if (criteria.getTagVersion2() != null) {
                specification = specification.and(buildSpecification(criteria.getTagVersion2(), Track_.tagVersion2));
            }
            if (criteria.getTypeId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getTypeId(), root -> root.join(Track_.type, JoinType.LEFT).get(TrackType_.id))
                    );
            }
        }
        return specification;
    }
}
