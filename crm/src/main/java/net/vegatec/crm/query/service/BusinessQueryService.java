package net.vegatec.crm.service;

import jakarta.persistence.criteria.JoinType;
import java.util.List;
import net.vegatec.crm.domain.*; // for static metamodels
import net.vegatec.crm.domain.Business;
import net.vegatec.crm.repository.BusinessRepository;
import net.vegatec.crm.service.criteria.BusinessCriteria;
import net.vegatec.crm.service.dto.BusinessDTO;
import net.vegatec.crm.service.mapper.BusinessMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Business} entities in the database.
 * The main input is a {@link BusinessCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link BusinessDTO} or a {@link Page} of {@link BusinessDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class BusinessQueryService extends QueryService<Business> {

    private final Logger log = LoggerFactory.getLogger(BusinessQueryService.class);

    private final BusinessRepository businessRepository;

    private final BusinessMapper businessMapper;

    public BusinessQueryService(BusinessRepository businessRepository, BusinessMapper businessMapper) {
        this.businessRepository = businessRepository;
        this.businessMapper = businessMapper;
    }

    /**
     * Return a {@link List} of {@link BusinessDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<BusinessDTO> findByCriteria(BusinessCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Business> specification = createSpecification(criteria);
        return businessMapper.toDto(businessRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link BusinessDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<BusinessDTO> findByCriteria(BusinessCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Business> specification = createSpecification(criteria);
        return businessRepository.findAll(specification, page).map(businessMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(BusinessCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Business> specification = createSpecification(criteria);
        return businessRepository.count(specification);
    }

    /**
     * Function to convert {@link BusinessCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Business> createSpecification(BusinessCriteria criteria) {
        Specification<Business> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Business_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Business_.name));
            }
            if (criteria.getAddress() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAddress(), Business_.address));
            }
            if (criteria.getOwner() != null) {
                specification = specification.and(buildStringSpecification(criteria.getOwner(), Business_.owner));
            }
            if (criteria.getContact() != null) {
                specification = specification.and(buildStringSpecification(criteria.getContact(), Business_.contact));
            }
            if (criteria.getPhone() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhone(), Business_.phone));
            }
            if (criteria.getProductsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getProductsId(), root -> root.join(Business_.products, JoinType.LEFT).get(Product_.id))
                    );
            }
        }
        return specification;
    }
}
