package net.vegatec.media_library.query.service;

import java.util.Optional;
import net.vegatec.media_library.query.domain.Track;
import net.vegatec.media_library.query.repository.TrackRepository;
import net.vegatec.media_library.query.repository.search.TrackSearchRepository;
import net.vegatec.media_library.query.service.dto.TrackDTO;
import net.vegatec.media_library.query.service.mapper.TrackMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Track}.
 */
@Service
@Transactional
public class TrackService {

    private final Logger log = LoggerFactory.getLogger(TrackService.class);

    private final TrackRepository trackRepository;

    private final TrackMapper trackMapper;

    private final TrackSearchRepository trackSearchRepository;

    public TrackService(TrackRepository trackRepository, TrackMapper trackMapper, TrackSearchRepository trackSearchRepository) {
        this.trackRepository = trackRepository;
        this.trackMapper = trackMapper;
        this.trackSearchRepository = trackSearchRepository;
    }

    /**
     * Save a track.
     *
     * @param trackDTO the entity to save.
     * @return the persisted entity.
     */
//    public TrackDTO save(TrackDTO trackDTO) {
//        log.debug("Request to save Track : {}", trackDTO);
//        Track track = trackMapper.toEntity(trackDTO);
//        track = trackRepository.save(track);
//        TrackDTO result = trackMapper.toDto(track);
//        trackSearchRepository.index(track);
//        return result;
//    }

    /**
     * Update a track.
     *
     * @param trackDTO the entity to save.
     * @return the persisted entity.
     */
//    public TrackDTO update(TrackDTO trackDTO) {
//        log.debug("Request to update Track : {}", trackDTO);
//        Track track = trackMapper.toEntity(trackDTO);
//        track = trackRepository.save(track);
//        TrackDTO result = trackMapper.toDto(track);
//        trackSearchRepository.index(track);
//        return result;
//    }

    /**
     * Partially update a track.
     *
     * @param trackDTO the entity to update partially.
     * @return the persisted entity.
     */
//    public Optional<TrackDTO> partialUpdate(TrackDTO trackDTO) {
//        log.debug("Request to partially update Track : {}", trackDTO);
//
//        return trackRepository
//            .findById(trackDTO.getId())
//            .map(existingTrack -> {
//                trackMapper.partialUpdate(existingTrack, trackDTO);
//
//                return existingTrack;
//            })
//            .map(trackRepository::save)
//            .map(savedTrack -> {
//                trackSearchRepository.index(savedTrack);
//                return savedTrack;
//            })
//            .map(trackMapper::toDto);
//    }

    /**
     * Get all the tracks.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<TrackDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Tracks");
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
        log.debug("Request to get Track : {}", id);
        return trackRepository.findOneWithEagerRelationships(id).map(trackMapper::toDto);
    }

    /**
     * Delete the track by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Track : {}", id);
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
        log.debug("Request to search for a page of Tracks for query {}", query);
        return trackSearchRepository.search(query, pageable).map(trackMapper::toDto);
    }
}
