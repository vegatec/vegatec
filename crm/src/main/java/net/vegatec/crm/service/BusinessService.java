package net.vegatec.crm.service;

import java.util.Optional;
import net.vegatec.crm.domain.Business;
import net.vegatec.crm.domain.Product;
import net.vegatec.crm.repository.BusinessRepository;
import net.vegatec.crm.repository.ProductRepository;
import net.vegatec.crm.service.dto.BusinessDTO;
import net.vegatec.crm.service.mapper.BusinessMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link net.vegatec.crm.domain.Business}.
 */
@Service
@Transactional
public class BusinessService {

    private final Logger log = LoggerFactory.getLogger(BusinessService.class);

    private final BusinessRepository businessRepository;

    private final BusinessMapper businessMapper;

    private final ProductRepository productRepository;

    public BusinessService(BusinessRepository businessRepository, BusinessMapper businessMapper, ProductRepository productRepository) {
        this.businessRepository = businessRepository;
        this.businessMapper = businessMapper;
        this.productRepository = productRepository;
    }

    /**
     * Save a business.
     *
     * @param businessDTO the entity to save.
     * @return the persisted entity.
     */
    public BusinessDTO save(BusinessDTO businessDTO) {
        log.debug("Request to save Business : {}", businessDTO);
        Business business = businessMapper.toEntity(businessDTO);
        business = businessRepository.save(business);
        return businessMapper.toDto(business);
    }

    /**
     * Update a business.
     *
     * @param businessDTO the entity to save.
     * @return the persisted entity.
     */
    public BusinessDTO update(BusinessDTO businessDTO) {
        log.debug("Request to update Business : {}", businessDTO);
        Business business = businessMapper.toEntity(businessDTO);
        business = businessRepository.save(business);
        return businessMapper.toDto(business);
    }

    /**
     * Partially update a business.
     *
     * @param businessDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<BusinessDTO> partialUpdate(BusinessDTO businessDTO) {
        log.debug("Request to partially update Business : {}", businessDTO);

        return businessRepository
            .findById(businessDTO.getId())
            .map(existingBusiness -> {
                businessMapper.partialUpdate(existingBusiness, businessDTO);

                return existingBusiness;
            })
            .map(businessRepository::save)
            .map(businessMapper::toDto);
    }

    /**
     * Get all the businesses.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<BusinessDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Businesses");
        return businessRepository.findAll(pageable).map(businessMapper::toDto);
    }

    /**
     * Get one business by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<BusinessDTO> findOne(Long id) {
        log.debug("Request to get Business : {}", id);
        return businessRepository.findById(id).map(businessMapper::toDto);
    }

    /**
     * Delete the business by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Business : {}", id);
        businessRepository.deleteById(id);
    }

    public Optional<BusinessDTO> addProduct(Long id, Long productId) {
        return businessRepository
            .findById(id)
            .map(existingBusiness -> {
                productRepository.findById(productId).map(product -> existingBusiness.addProduct(product));

                return existingBusiness;
            })
            .map(businessRepository::save)
            .map(businessMapper::toDto);
    }

    public Optional<BusinessDTO> removeProducts(Long id, Long productId) {
        return businessRepository
            .findById(id)
            .map(existingBusiness -> {
                Optional<Product> product = existingBusiness.getProducts().stream().filter(p -> p.getId() == productId).findFirst();
                if (product.isPresent()) existingBusiness.removeProduct(product.get());
                return existingBusiness;
            })
            .map(businessRepository::save)
            .map(businessMapper::toDto);
    }
}
