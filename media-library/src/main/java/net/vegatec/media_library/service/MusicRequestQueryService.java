package net.vegatec.media_library.service;

import java.util.List;
import net.vegatec.media_library.domain.*; // for static metamodels
import net.vegatec.media_library.domain.MusicRequest;
import net.vegatec.media_library.repository.MusicRequestRepository;
import net.vegatec.media_library.repository.search.MusicRequestSearchRepository;
import net.vegatec.media_library.service.criteria.MusicRequestCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link MusicRequest} entities in the database.
 * The main input is a {@link MusicRequestCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link MusicRequest} or a {@link Page} of {@link MusicRequest} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MusicRequestQueryService extends QueryService<MusicRequest> {

    private final Logger log = LoggerFactory.getLogger(MusicRequestQueryService.class);

    private final MusicRequestRepository musicRequestRepository;

    private final MusicRequestSearchRepository musicRequestSearchRepository;

    public MusicRequestQueryService(
        MusicRequestRepository musicRequestRepository,
        MusicRequestSearchRepository musicRequestSearchRepository
    ) {
        this.musicRequestRepository = musicRequestRepository;
        this.musicRequestSearchRepository = musicRequestSearchRepository;
    }

    /**
     * Return a {@link List} of {@link MusicRequest} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<MusicRequest> findByCriteria(MusicRequestCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<MusicRequest> specification = createSpecification(criteria);
        return musicRequestRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link MusicRequest} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<MusicRequest> findByCriteria(MusicRequestCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<MusicRequest> specification = createSpecification(criteria);
        return musicRequestRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MusicRequestCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<MusicRequest> specification = createSpecification(criteria);
        return musicRequestRepository.count(specification);
    }

    /**
     * Function to convert {@link MusicRequestCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<MusicRequest> createSpecification(MusicRequestCriteria criteria) {
        Specification<MusicRequest> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), MusicRequest_.id));
            }
            if (criteria.getSong() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSong(), MusicRequest_.song));
            }
            if (criteria.getArtist() != null) {
                specification = specification.and(buildStringSpecification(criteria.getArtist(), MusicRequest_.artist));
            }
            if (criteria.getAlbum() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAlbum(), MusicRequest_.album));
            }
            if (criteria.getGenre() != null) {
                specification = specification.and(buildStringSpecification(criteria.getGenre(), MusicRequest_.genre));
            }
            if (criteria.getRequestedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getRequestedBy(), MusicRequest_.requestedBy));
            }
            if (criteria.getRequestedOn() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRequestedOn(), MusicRequest_.requestedOn));
            }
            if (criteria.getUrl() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUrl(), MusicRequest_.url));
            }
            if (criteria.getDone() != null) {
                specification = specification.and(buildSpecification(criteria.getDone(), MusicRequest_.done));
            }
        }
        return specification;
    }
}
