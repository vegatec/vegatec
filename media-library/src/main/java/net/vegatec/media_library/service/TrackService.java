package net.vegatec.media_library.service;

import java.util.*;

import net.vegatec.media_library.config.ApplicationProperties;
import net.vegatec.media_library.domain.Track;
import net.vegatec.media_library.repository.TrackRepository;
import net.vegatec.media_library.repository.TrackTypeRepository;
import net.vegatec.media_library.repository.search.TrackSearchRepository;
import net.vegatec.media_library.service.dto.TrackDTO;
import net.vegatec.media_library.service.mapper.TrackMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link net.vegatec.media_library.domain.Track}.
 */
@Service
@Transactional
public class TrackService {

    private final Logger logger = LoggerFactory.getLogger(TrackService.class);

    private final TrackRepository trackRepository;

    private final TrackTypeRepository trackTypeRepository;

    private final TrackMapper trackMapper;

    private final TrackSearchRepository trackSearchRepository;

    private final TrackQueryService trackQueryService;

    private final ApplicationProperties applicationProperties;



    public TrackService(TrackRepository trackRepository, TrackTypeRepository trackTypeRepository, TrackMapper trackMapper, TrackSearchRepository trackSearchRepository, TrackQueryService trackQueryService, ApplicationProperties applicationProperties, RecursiveFolderMonitor folderMonitor) {
        this.trackRepository = trackRepository;
        this.trackTypeRepository = trackTypeRepository;
        this.trackMapper = trackMapper;
        this.trackSearchRepository = trackSearchRepository;
        this.trackQueryService = trackQueryService;
        this.applicationProperties = applicationProperties;

    }

    /**
     * Save a track.
     *
     * @param trackDTO the entity to save.
     * @return the persisted entity.
     */
    public TrackDTO save(TrackDTO trackDTO) {
        logger.debug("Request to save Track : {}", trackDTO);
        Track track = trackMapper.toEntity(trackDTO);
        track = trackRepository.save(track);
        TrackDTO result = trackMapper.toDto(track);
        trackSearchRepository.index(track);
        return result;
    }

    /**
     * Update a track.
     *
     * @param trackDTO the entity to save.
     * @return the persisted entity.
     */
    public TrackDTO update(TrackDTO trackDTO) {
        logger.debug("Request to update Track : {}", trackDTO);
        Track track = trackMapper.toEntity(trackDTO);
        track = trackRepository.save(track);
        TrackDTO result = trackMapper.toDto(track);
        trackSearchRepository.index(track);
        return result;
    }

    /**
     * Partially update a track.
     *
     * @param trackDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TrackDTO> partialUpdate(TrackDTO trackDTO) {
        logger.debug("Request to partially update Track : {}", trackDTO);

        return trackRepository
            .findById(trackDTO.getId())
            .map(existingTrack -> {
                trackMapper.partialUpdate(existingTrack, trackDTO);

                return existingTrack;
            })
            .map(trackRepository::save)
            .map(savedTrack -> {
                trackSearchRepository.index(savedTrack);
                return savedTrack;
            })
            .map(trackMapper::toDto);
    }

    /**
     * Get all the tracks.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<TrackDTO> findAll(Pageable pageable) {
        logger.debug("Request to get all Tracks");
        return trackRepository.findAll(pageable).map(trackMapper::toDto);
    }

    /**
     * Get all the tracks with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<TrackDTO> findAllWithEagerRelationships(Pageable pageable) {
        return trackRepository.findAllWithEagerRelationships(pageable).map(trackMapper::toDto);
    }

    /**
     * Get one track by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TrackDTO> findOne(Long id) {
        logger.debug("Request to get Track : {}", id);
        return trackRepository.findOneWithEagerRelationships(id).map(trackMapper::toDto);
    }

    /**
     * Delete the track by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        logger.debug("Request to delete Track : {}", id);
        trackRepository.deleteById(id);
        trackSearchRepository.deleteFromIndexById(id);
    }

    /**
     * Search for the track corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<TrackDTO> search(String query, Pageable pageable) {
        logger.debug("Request to search for a page of Tracks for query {}", query);
        return trackSearchRepository.search(query, pageable).map(trackMapper::toDto);
    }



}
