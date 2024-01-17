package net.vegatec.media_library.service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import net.vegatec.media_library.domain.TrackType;
import net.vegatec.media_library.repository.TrackTypeRepository;
import net.vegatec.media_library.repository.search.TrackTypeSearchRepository;
import net.vegatec.media_library.service.dto.TrackTypeDTO;
import net.vegatec.media_library.service.mapper.TrackTypeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link net.vegatec.media_library.domain.TrackType}.
 */
@Service
@Transactional
public class TrackTypeService {

    private final Logger log = LoggerFactory.getLogger(TrackTypeService.class);

    private final TrackTypeRepository trackTypeRepository;

    private final TrackTypeMapper trackTypeMapper;

    private final TrackTypeSearchRepository trackTypeSearchRepository;

    public TrackTypeService(
        TrackTypeRepository trackTypeRepository,
        TrackTypeMapper trackTypeMapper,
        TrackTypeSearchRepository trackTypeSearchRepository
    ) {
        this.trackTypeRepository = trackTypeRepository;
        this.trackTypeMapper = trackTypeMapper;
        this.trackTypeSearchRepository = trackTypeSearchRepository;
    }

    /**
     * Save a trackType.
     *
     * @param trackTypeDTO the entity to save.
     * @return the persisted entity.
     */
    public TrackTypeDTO save(TrackTypeDTO trackTypeDTO) {
        log.debug("Request to save TrackType : {}", trackTypeDTO);
        TrackType trackType = trackTypeMapper.toEntity(trackTypeDTO);
        trackType = trackTypeRepository.save(trackType);
        TrackTypeDTO result = trackTypeMapper.toDto(trackType);
        trackTypeSearchRepository.index(trackType);
        return result;
    }

    /**
     * Update a trackType.
     *
     * @param trackTypeDTO the entity to save.
     * @return the persisted entity.
     */
    public TrackTypeDTO update(TrackTypeDTO trackTypeDTO) {
        log.debug("Request to update TrackType : {}", trackTypeDTO);
        TrackType trackType = trackTypeMapper.toEntity(trackTypeDTO);
        trackType = trackTypeRepository.save(trackType);
        TrackTypeDTO result = trackTypeMapper.toDto(trackType);
        trackTypeSearchRepository.index(trackType);
        return result;
    }

    /**
     * Partially update a trackType.
     *
     * @param trackTypeDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TrackTypeDTO> partialUpdate(TrackTypeDTO trackTypeDTO) {
        log.debug("Request to partially update TrackType : {}", trackTypeDTO);

        return trackTypeRepository
            .findById(trackTypeDTO.getId())
            .map(existingTrackType -> {
                trackTypeMapper.partialUpdate(existingTrackType, trackTypeDTO);

                return existingTrackType;
            })
            .map(trackTypeRepository::save)
            .map(savedTrackType -> {
                trackTypeSearchRepository.index(savedTrackType);
                return savedTrackType;
            })
            .map(trackTypeMapper::toDto);
    }

    /**
     * Get all the trackTypes.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<TrackTypeDTO> findAll() {
        log.debug("Request to get all TrackTypes");
        return trackTypeRepository.findAll().stream().map(trackTypeMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one trackType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TrackTypeDTO> findOne(Long id) {
        log.debug("Request to get TrackType : {}", id);
        return trackTypeRepository.findById(id).map(trackTypeMapper::toDto);
    }

    /**
     * Delete the trackType by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete TrackType : {}", id);
        trackTypeRepository.deleteById(id);
        trackTypeSearchRepository.deleteFromIndexById(id);
    }

    /**
     * Search for the trackType corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<TrackTypeDTO> search(String query) {
        log.debug("Request to search TrackTypes for query {}", query);
        try {
            return StreamSupport.stream(trackTypeSearchRepository.search(query).spliterator(), false).map(trackTypeMapper::toDto).toList();
        } catch (RuntimeException e) {
            throw e;
        }
    }
}
