package net.vegatec.media_library.service;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.time.Instant;
import java.util.*;
import java.util.stream.Stream;

import com.mpatric.mp3agic.*;
import net.vegatec.media_library.config.ApplicationProperties;
import net.vegatec.media_library.domain.Track;
import net.vegatec.media_library.domain.TrackType;
import net.vegatec.media_library.service.events.FileCreated;
import net.vegatec.media_library.repository.TrackRepository;
import net.vegatec.media_library.repository.TrackTypeRepository;
import net.vegatec.media_library.repository.search.TrackSearchRepository;
import net.vegatec.media_library.service.criteria.TrackCriteria;
import net.vegatec.media_library.service.dto.TrackDTO;
import net.vegatec.media_library.service.mapper.TrackMapper;
import net.vegatec.media_library.util.DebounceExecutor;
import net.vegatec.media_library.util.ImageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.filter.StringFilter;

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






//    @Async
//    public void execute(Track track) throws Exception {
//
//        logger.debug("Updating  media file  {}", track);
//
//        // 1. find original file
//        Optional<Track> originalTrack = trackRepository.findById(track.getId());
//        if (originalTrack.isEmpty())
//            throw new Exception("Not media file was found!");
//
//        logger.info("-->>>>>>>>>> original file path  {}", originalTrack.get().getFilePath());
//
//        moveMediaFile(originalTrack.get(), Track.INBOX);
//
//        Path currentFilePath = Paths.get(applicationProperties.getMediaFolder(), originalTrack.get().getFilePath()).toAbsolutePath();
//
//        //move to temp to update tags
//
//        logger.info("-->>>>>>>>>>before creating temp file  {}", currentFilePath.getParent().toFile());
//        File tempFile = File.createTempFile("media-library", ".mp3", currentFilePath.getParent().toFile());
//        logger.info("-->>>>>>>>>>temp file  {}",tempFile.toString());
//
//        if (currentFilePath.toFile().renameTo(tempFile)) {
//
//
//            // 3. update  id3 tags using original media file
//            Mp3File mp3File = new Mp3File(tempFile);
//
//            ID3v2 id3v2Tag = new ID3v23Tag();
//            id3v2Tag.setTitle(track.getName());
//            id3v2Tag.setArtist(track.getArtist().getName());
//            id3v2Tag.setAlbum(track.getAlbum().getName());
//            id3v2Tag.setAlbumArtist(track.getAlbum().getArtist().getName());
//            id3v2Tag.setYear(track.getAlbum().getReleasedYear().toString());
//
//            try {
//                id3v2Tag.setGenreDescription(track.getGenre().getName());
//            } catch (Exception ex) {
//
//            }
//            mp3File.setId3v2Tag(id3v2Tag);
//
//            // 4. now update original media file with new information
//            originalTrack.get()
//                    .subfolder(Track.INBOX)
//                    .name(track.getName())
//                    .artist(track.getArtist().getName())
//                    .album( track.getAlbum().getName(), track.getAlbum().getArtist().getName(), track.getAlbum().getReleasedYear())
//                    .genre(track.getGenre().getName());
//
//            File destFile = Paths.get(applicationProperties.getMediaFolder(), originalTrack.get().getFilePath()).toAbsolutePath().toFile();
//
//            // 5. create parent folders if don't exists
//            if (!destFile.getParentFile().exists())
//                destFile.getParentFile().mkdirs();
//
//
//            mp3File.save(destFile.getAbsolutePath());
//
//            tempFile.delete();
//
//
//
//            // 6. persist changes
//            trackRepository.save(originalTrack.get());
//
//        }
//    }








}
