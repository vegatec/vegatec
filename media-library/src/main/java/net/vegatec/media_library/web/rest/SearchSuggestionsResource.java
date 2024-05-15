package net.vegatec.media_library.web.rest;

import com.codahale.metrics.annotation.Timed;
import net.vegatec.media_library.repository.SearchSuggestionsRepository;
import net.vegatec.media_library.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URISyntaxException;
import java.util.List;

/**
 * REST controller for managing Track.
 */
@RestController
@RequestMapping("/api")
public class SearchSuggestionsResource {

    private final Logger log = LoggerFactory.getLogger(SearchSuggestionsResource.class);

    private SearchSuggestionsRepository searchSuggestionsRepository;

    public SearchSuggestionsResource(SearchSuggestionsRepository searchSuggestionsRepository ){
        this.searchSuggestionsRepository= searchSuggestionsRepository;

    }

    /**
     * GET  /tracks : get all the tracks.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of tracks in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */



    @GetMapping("/artists/suggestions")
    @Timed
    public ResponseEntity<List<String>> findArtistSuggestions(@RequestParam(name="term") String term, Pageable pageable) throws URISyntaxException  {
        log.debug("REST request to get all artist names matching  {}", term);

        final Page<String> page =  searchSuggestionsRepository.findArtistSuggestions(pageable, term);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/artists/suggestions");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/albums/suggestions")
    @Timed
    public ResponseEntity<List<String>> findAlbumSuggestions(@RequestParam(name = "term") String term, Pageable pageable) throws URISyntaxException  {
        log.debug("REST request to get all album names matching  {}", term);

        final Page<String> page =  searchSuggestionsRepository.findAlbumSuggestions(pageable, term);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/albums/suggestions");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/tracks/suggestions")
    @Timed
    public ResponseEntity<List<String>> findTrackSuggestions(@RequestParam( name="term") String term, Pageable pageable) throws URISyntaxException  {
        log.debug("REST request to get all track names matching  {}", term);

        final Page<String> page =  searchSuggestionsRepository.findTrackSuggestions(pageable, term);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/tracks/suggestions");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }





}
