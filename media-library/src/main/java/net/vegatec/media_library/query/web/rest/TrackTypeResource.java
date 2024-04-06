package net.vegatec.media_library.query.web.rest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import net.vegatec.media_library.query.domain.TrackType;
import net.vegatec.media_library.query.repository.TrackTypeRepository;
import net.vegatec.media_library.query.service.TrackTypeService;
import net.vegatec.media_library.query.service.dto.TrackTypeDTO;
import net.vegatec.media_library.query.web.rest.errors.BadRequestAlertException;
import net.vegatec.media_library.query.web.rest.errors.ElasticsearchExceptionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link TrackType}.
 */
@RestController
@RequestMapping("/api/track-types")
public class TrackTypeResource {

    private final Logger log = LoggerFactory.getLogger(TrackTypeResource.class);

    private static final String ENTITY_NAME = "trackType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TrackTypeService trackTypeService;

    private final TrackTypeRepository trackTypeRepository;

    public TrackTypeResource(TrackTypeService trackTypeService, TrackTypeRepository trackTypeRepository) {
        this.trackTypeService = trackTypeService;
        this.trackTypeRepository = trackTypeRepository;
    }

    /**
     * {@code POST  /track-types} : Create a new trackType.
     *
     * @param trackTypeDTO the trackTypeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new trackTypeDTO, or with status {@code 400 (Bad Request)} if the trackType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<TrackTypeDTO> createTrackType(@Valid @RequestBody TrackTypeDTO trackTypeDTO) throws URISyntaxException {
        log.debug("REST request to save TrackType : {}", trackTypeDTO);
        if (trackTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new trackType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TrackTypeDTO result = trackTypeService.save(trackTypeDTO);
        return ResponseEntity
            .created(new URI("/api/track-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /track-types/:id} : Updates an existing trackType.
     *
     * @param id the id of the trackTypeDTO to save.
     * @param trackTypeDTO the trackTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated trackTypeDTO,
     * or with status {@code 400 (Bad Request)} if the trackTypeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the trackTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TrackTypeDTO> updateTrackType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TrackTypeDTO trackTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update TrackType : {}, {}", id, trackTypeDTO);
        if (trackTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, trackTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!trackTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TrackTypeDTO result = trackTypeService.update(trackTypeDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, trackTypeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /track-types/:id} : Partial updates given fields of an existing trackType, field will ignore if it is null
     *
     * @param id the id of the trackTypeDTO to save.
     * @param trackTypeDTO the trackTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated trackTypeDTO,
     * or with status {@code 400 (Bad Request)} if the trackTypeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the trackTypeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the trackTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TrackTypeDTO> partialUpdateTrackType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TrackTypeDTO trackTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update TrackType partially : {}, {}", id, trackTypeDTO);
        if (trackTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, trackTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!trackTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TrackTypeDTO> result = trackTypeService.partialUpdate(trackTypeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, trackTypeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /track-types} : get all the trackTypes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of trackTypes in body.
     */
    @GetMapping("")
    public List<TrackTypeDTO> getAllTrackTypes() {
        log.debug("REST request to get all TrackTypes");
        return trackTypeService.findAll();
    }

    /**
     * {@code GET  /track-types/:id} : get the "id" trackType.
     *
     * @param id the id of the trackTypeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the trackTypeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TrackTypeDTO> getTrackType(@PathVariable("id") Long id) {
        log.debug("REST request to get TrackType : {}", id);
        Optional<TrackTypeDTO> trackTypeDTO = trackTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(trackTypeDTO);
    }

    /**
     * {@code DELETE  /track-types/:id} : delete the "id" trackType.
     *
     * @param id the id of the trackTypeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTrackType(@PathVariable("id") Long id) {
        log.debug("REST request to delete TrackType : {}", id);
        trackTypeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /track-types/_search?query=:query} : search for the trackType corresponding
     * to the query.
     *
     * @param query the query of the trackType search.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public List<TrackTypeDTO> searchTrackTypes(@RequestParam("query") String query) {
        log.debug("REST request to search TrackTypes for query {}", query);
        try {
            return trackTypeService.search(query);
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
