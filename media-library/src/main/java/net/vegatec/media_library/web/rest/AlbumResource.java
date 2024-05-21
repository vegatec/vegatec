package net.vegatec.media_library.web.rest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import net.vegatec.media_library.domain.Album;
import net.vegatec.media_library.mediator.SpringMediator;
import net.vegatec.media_library.repository.AlbumRepository;
import net.vegatec.media_library.repository.TrackRepository;
import net.vegatec.media_library.service.TrackQueryService;
import net.vegatec.media_library.service.TrackService;
import net.vegatec.media_library.service.commands.UploadFile;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

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
