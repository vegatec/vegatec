package net.vegatec.media_library.web.rest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import net.vegatec.media_library.domain.Track;
import net.vegatec.media_library.mediator.SpringMediator;
import net.vegatec.media_library.repository.TrackRepository;
import net.vegatec.media_library.service.TrackQueryService;
import net.vegatec.media_library.service.TrackService;
import net.vegatec.media_library.service.commands.PublishTrack;
import net.vegatec.media_library.service.commands.UnpublishTrack;
import net.vegatec.media_library.service.criteria.LibrarySearchCriteria;
import net.vegatec.media_library.service.criteria.TrackCriteria;
import net.vegatec.media_library.service.dto.TrackDTO;
import net.vegatec.media_library.web.rest.errors.BadRequestAlertException;
import net.vegatec.media_library.web.rest.errors.ElasticsearchExceptionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link net.vegatec.media_library.domain.Track}.
 */
@RestController
@RequestMapping("/api/tracks")
public class TrackResource {

    private final Logger log = LoggerFactory.getLogger(TrackResource.class);

    private static final String ENTITY_NAME = "track";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TrackService trackService;

    private final TrackRepository trackRepository;

    private final TrackQueryService trackQueryService;

    private final SpringMediator mediator;

    public TrackResource(TrackService trackService, TrackRepository trackRepository, TrackQueryService trackQueryService, SpringMediator mediator) {
        this.trackService = trackService;
        this.trackRepository = trackRepository;
        this.trackQueryService = trackQueryService;
        this.mediator = mediator;
    }

    /**
     * {@code POST  /tracks} : Create a new track.
     *
     * @param trackDTO the trackDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new trackDTO, or with status {@code 400 (Bad Request)} if the track has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<TrackDTO> createTrack(@Valid @RequestBody TrackDTO trackDTO) throws URISyntaxException {
        log.debug("REST request to save Track : {}", trackDTO);
        if (trackDTO.getId() != null) {
            throw new BadRequestAlertException("A new track cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TrackDTO result = trackService.save(trackDTO);
        return ResponseEntity
            .created(new URI("/api/tracks/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /tracks/:id} : Updates an existing track.
     *
     * @param id the id of the trackDTO to save.
     * @param trackDTO the trackDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated trackDTO,
     * or with status {@code 400 (Bad Request)} if the trackDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the trackDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TrackDTO> updateTrack(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TrackDTO trackDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Track : {}, {}", id, trackDTO);
        if (trackDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, trackDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!trackRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TrackDTO result = trackService.update(trackDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, trackDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /tracks/:id} : Partial updates given fields of an existing track, field will ignore if it is null
     *
     * @param id the id of the trackDTO to save.
     * @param trackDTO the trackDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated trackDTO,
     * or with status {@code 400 (Bad Request)} if the trackDTO is not valid,
     * or with status {@code 404 (Not Found)} if the trackDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the trackDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TrackDTO> partialUpdateTrack(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TrackDTO trackDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Track partially : {}, {}", id, trackDTO);
        if (trackDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, trackDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!trackRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TrackDTO> result = trackService.partialUpdate(trackDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, trackDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /tracks} : get all the tracks.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tracks in body.
     */
    //    @GetMapping("")
    //    public ResponseEntity<List<TrackDTO>> getAllTracks(
    //        TrackCriteria criteria,
    //        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    //    ) {
    //        log.debug("REST request to get Tracks by criteria: {}", criteria);
    //
    //        Page<TrackDTO> page = trackQueryService.findByCriteria(criteria, pageable);
    //        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
    //        return ResponseEntity.ok().headers(headers).body(page.getContent());
    //    }

    @GetMapping
    public ResponseEntity<List<Track>> getAllTracks(LibrarySearchCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Tracks by criteria: {}", criteria);
        Page<Track> page = trackRepository.findAll(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /tracks/count} : count all the tracks.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countTracks(TrackCriteria criteria) {
        log.debug("REST request to count Tracks by criteria: {}", criteria);
        return ResponseEntity.ok().body(trackQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /tracks/:id} : get the "id" track.
     *
     * @param id the id of the trackDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the trackDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TrackDTO> getTrack(@PathVariable("id") Long id) {
        log.debug("REST request to get Track : {}", id);
        Optional<TrackDTO> trackDTO = trackService.findOne(id);
        return ResponseUtil.wrapOrNotFound(trackDTO);
    }

    /**
     * {@code DELETE  /tracks/:id} : delete the "id" track.
     *
     * @param id the id of the trackDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTrack(@PathVariable("id") Long id) {
        log.debug("REST request to delete Track : {}", id);
        trackService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /tracks/_search?query=:query} : search for the track corresponding
     * to the query.
     *
     * @param query the query of the track search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<TrackDTO>> searchTracks(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to search for a page of Tracks for query {}", query);
        try {
            Page<TrackDTO> page = trackService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }


    @PutMapping("/{id}/publish")
    public ResponseEntity<TrackDTO> publishTrack(
        @PathVariable("id") Long id

    ) {
        log.debug("REST request publish track with id {}", id);

            TrackDTO result= mediator.send(new PublishTrack(id));

            return ResponseEntity.ok(result);

    }


    @PutMapping("/{id}/unpublish")
    public ResponseEntity<TrackDTO> unpublishTrack(
        @PathVariable("id") Long id

    ) {
        log.debug("REST request unpublish track with id {}", id);

        TrackDTO result= mediator.send(new UnpublishTrack(id));

        return ResponseEntity.ok(result);

    }
}
