package net.vegatec.media_library.service;

import java.util.Optional;
import net.vegatec.media_library.domain.MusicRequest;
import net.vegatec.media_library.repository.MusicRequestRepository;
import net.vegatec.media_library.repository.search.MusicRequestSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link net.vegatec.media_library.domain.MusicRequest}.
 */
@Service
@Transactional
public class MusicRequestService {

    private final Logger log = LoggerFactory.getLogger(MusicRequestService.class);

    private final MusicRequestRepository musicRequestRepository;

    private final MusicRequestSearchRepository musicRequestSearchRepository;

    public MusicRequestService(MusicRequestRepository musicRequestRepository, MusicRequestSearchRepository musicRequestSearchRepository) {
        this.musicRequestRepository = musicRequestRepository;
        this.musicRequestSearchRepository = musicRequestSearchRepository;
    }

    /**
     * Save a musicRequest.
     *
     * @param musicRequest the entity to save.
     * @return the persisted entity.
     */
    public MusicRequest save(MusicRequest musicRequest) {
        log.debug("Request to save MusicRequest : {}", musicRequest);
        MusicRequest result = musicRequestRepository.save(musicRequest);
        musicRequestSearchRepository.index(result);
        return result;
    }

    /**
     * Update a musicRequest.
     *
     * @param musicRequest the entity to save.
     * @return the persisted entity.
     */
    public MusicRequest update(MusicRequest musicRequest) {
        log.debug("Request to update MusicRequest : {}", musicRequest);
        MusicRequest result = musicRequestRepository.save(musicRequest);
        musicRequestSearchRepository.index(result);
        return result;
    }

    /**
     * Partially update a musicRequest.
     *
     * @param musicRequest the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<MusicRequest> partialUpdate(MusicRequest musicRequest) {
        log.debug("Request to partially update MusicRequest : {}", musicRequest);

        return musicRequestRepository
            .findById(musicRequest.getId())
            .map(existingMusicRequest -> {
                if (musicRequest.getSong() != null) {
                    existingMusicRequest.setSong(musicRequest.getSong());
                }
                if (musicRequest.getArtist() != null) {
                    existingMusicRequest.setArtist(musicRequest.getArtist());
                }
                if (musicRequest.getAlbum() != null) {
                    existingMusicRequest.setAlbum(musicRequest.getAlbum());
                }
                if (musicRequest.getGenre() != null) {
                    existingMusicRequest.setGenre(musicRequest.getGenre());
                }
                if (musicRequest.getRequestedBy() != null) {
                    existingMusicRequest.setRequestedBy(musicRequest.getRequestedBy());
                }
                if (musicRequest.getRequestedOn() != null) {
                    existingMusicRequest.setRequestedOn(musicRequest.getRequestedOn());
                }
                if (musicRequest.getUrl() != null) {
                    existingMusicRequest.setUrl(musicRequest.getUrl());
                }
                if (musicRequest.getDone() != null) {
                    existingMusicRequest.setDone(musicRequest.getDone());
                }

                return existingMusicRequest;
            })
            .map(musicRequestRepository::save)
            .map(savedMusicRequest -> {
                musicRequestSearchRepository.index(savedMusicRequest);
                return savedMusicRequest;
            });
    }

    /**
     * Get all the musicRequests.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<MusicRequest> findAll(Pageable pageable) {
        log.debug("Request to get all MusicRequests");
        return musicRequestRepository.findAll(pageable);
    }

    /**
     * Get one musicRequest by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<MusicRequest> findOne(Long id) {
        log.debug("Request to get MusicRequest : {}", id);
        return musicRequestRepository.findById(id);
    }

    /**
     * Delete the musicRequest by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete MusicRequest : {}", id);
        musicRequestRepository.deleteById(id);
        musicRequestSearchRepository.deleteFromIndexById(id);
    }

    /**
     * Search for the musicRequest corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<MusicRequest> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of MusicRequests for query {}", query);
        return musicRequestSearchRepository.search(query, pageable);
    }
}
