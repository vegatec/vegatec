package net.vegatec.media_library.query.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import net.vegatec.media_library.query.domain.MusicRequest;
import net.vegatec.media_library.query.repository.MusicRequestRepository;
import net.vegatec.media_library.query.service.MusicRequestQueryService;
import net.vegatec.media_library.query.service.MusicRequestService;
import net.vegatec.media_library.query.service.criteria.MusicRequestCriteria;
import net.vegatec.media_library.query.web.rest.errors.BadRequestAlertException;
import net.vegatec.media_library.query.web.rest.errors.ElasticsearchExceptionMapper;
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
 * REST controller for managing {@link MusicRequest}.
 */
@RestController
@RequestMapping("/api/music-requests")
public class MusicRequestResource {

    private final Logger log = LoggerFactory.getLogger(MusicRequestResource.class);

    private static final String ENTITY_NAME = "musicRequest";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MusicRequestService musicRequestService;

    private final MusicRequestRepository musicRequestRepository;

    private final MusicRequestQueryService musicRequestQueryService;

    public MusicRequestResource(
        MusicRequestService musicRequestService,
        MusicRequestRepository musicRequestRepository,
        MusicRequestQueryService musicRequestQueryService
    ) {
        this.musicRequestService = musicRequestService;
        this.musicRequestRepository = musicRequestRepository;
        this.musicRequestQueryService = musicRequestQueryService;
    }

    /**
     * {@code POST  /music-requests} : Create a new musicRequest.
     *
     * @param musicRequest the musicRequest to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new musicRequest, or with status {@code 400 (Bad Request)} if the musicRequest has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<MusicRequest> createMusicRequest(@RequestBody MusicRequest musicRequest) throws URISyntaxException {
        log.debug("REST request to save MusicRequest : {}", musicRequest);
        if (musicRequest.getId() != null) {
            throw new BadRequestAlertException("A new musicRequest cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MusicRequest result = musicRequestService.save(musicRequest);
        return ResponseEntity
            .created(new URI("/api/music-requests/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /music-requests/:id} : Updates an existing musicRequest.
     *
     * @param id the id of the musicRequest to save.
     * @param musicRequest the musicRequest to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated musicRequest,
     * or with status {@code 400 (Bad Request)} if the musicRequest is not valid,
     * or with status {@code 500 (Internal Server Error)} if the musicRequest couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<MusicRequest> updateMusicRequest(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody MusicRequest musicRequest
    ) throws URISyntaxException {
        log.debug("REST request to update MusicRequest : {}, {}", id, musicRequest);
        if (musicRequest.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, musicRequest.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!musicRequestRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        MusicRequest result = musicRequestService.update(musicRequest);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, musicRequest.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /music-requests/:id} : Partial updates given fields of an existing musicRequest, field will ignore if it is null
     *
     * @param id the id of the musicRequest to save.
     * @param musicRequest the musicRequest to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated musicRequest,
     * or with status {@code 400 (Bad Request)} if the musicRequest is not valid,
     * or with status {@code 404 (Not Found)} if the musicRequest is not found,
     * or with status {@code 500 (Internal Server Error)} if the musicRequest couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MusicRequest> partialUpdateMusicRequest(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody MusicRequest musicRequest
    ) throws URISyntaxException {
        log.debug("REST request to partial update MusicRequest partially : {}, {}", id, musicRequest);
        if (musicRequest.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, musicRequest.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!musicRequestRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MusicRequest> result = musicRequestService.partialUpdate(musicRequest);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, musicRequest.getId().toString())
        );
    }

    /**
     * {@code GET  /music-requests} : get all the musicRequests.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of musicRequests in body.
     */
    @GetMapping("")
    public ResponseEntity<List<MusicRequest>> getAllMusicRequests(
        MusicRequestCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get MusicRequests by criteria: {}", criteria);

        Page<MusicRequest> page = musicRequestQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /music-requests/count} : count all the musicRequests.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countMusicRequests(MusicRequestCriteria criteria) {
        log.debug("REST request to count MusicRequests by criteria: {}", criteria);
        return ResponseEntity.ok().body(musicRequestQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /music-requests/:id} : get the "id" musicRequest.
     *
     * @param id the id of the musicRequest to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the musicRequest, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<MusicRequest> getMusicRequest(@PathVariable("id") Long id) {
        log.debug("REST request to get MusicRequest : {}", id);
        Optional<MusicRequest> musicRequest = musicRequestService.findOne(id);
        return ResponseUtil.wrapOrNotFound(musicRequest);
    }

    /**
     * {@code DELETE  /music-requests/:id} : delete the "id" musicRequest.
     *
     * @param id the id of the musicRequest to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMusicRequest(@PathVariable("id") Long id) {
        log.debug("REST request to delete MusicRequest : {}", id);
        musicRequestService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /music-requests/_search?query=:query} : search for the musicRequest corresponding
     * to the query.
     *
     * @param query the query of the musicRequest search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<MusicRequest>> searchMusicRequests(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to search for a page of MusicRequests for query {}", query);
        try {
            Page<MusicRequest> page = musicRequestService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
