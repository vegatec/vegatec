package net.vegatec.media_library.web.rest;

import java.util.List;

import net.vegatec.media_library.domain.Album;
import net.vegatec.media_library.mediator.SpringMediator;
import net.vegatec.media_library.repository.AlbumRepository;
import net.vegatec.media_library.service.criteria.LibrarySearchCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.PaginationUtil;

/**
 * REST controller for managing {@link net.vegatec.media_library.domain.Track}.
 */
@RestController
@RequestMapping("/api/albums")
public class AlbumResource {

    private final Logger log = LoggerFactory.getLogger(AlbumResource.class);

    private static final String ENTITY_NAME = "track";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AlbumRepository albumRepository;

    private final SpringMediator mediator;

    public AlbumResource(AlbumRepository albumRepository, SpringMediator mediator) {
        this.albumRepository = albumRepository;
        this.mediator = mediator;
    }

    /**
     * {@code GET  /albums} : get all the albums.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of albums in body.
     */
    @GetMapping
    public ResponseEntity<List<Album>> getAllAlbums(LibrarySearchCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Tracks by criteria: {}", criteria);
        Page<Album> page = albumRepository.findAll(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }




}
