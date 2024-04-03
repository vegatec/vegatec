package net.vegatec.media_library.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import net.vegatec.media_library.IntegrationTest;
import net.vegatec.media_library.query.domain.MusicRequest;
import net.vegatec.media_library.query.repository.MusicRequestRepository;
import net.vegatec.media_library.query.repository.search.MusicRequestSearchRepository;
import net.vegatec.media_library.query.web.rest.MusicRequestResource;
import org.apache.commons.collections4.IterableUtils;
import org.assertj.core.util.IterableUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link MusicRequestResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MusicRequestResourceIT {

    private static final String DEFAULT_SONG = "AAAAAAAAAA";
    private static final String UPDATED_SONG = "BBBBBBBBBB";

    private static final String DEFAULT_ARTIST = "AAAAAAAAAA";
    private static final String UPDATED_ARTIST = "BBBBBBBBBB";

    private static final String DEFAULT_ALBUM = "AAAAAAAAAA";
    private static final String UPDATED_ALBUM = "BBBBBBBBBB";

    private static final String DEFAULT_GENRE = "AAAAAAAAAA";
    private static final String UPDATED_GENRE = "BBBBBBBBBB";

    private static final String DEFAULT_REQUESTED_BY = "AAAAAAAAAA";
    private static final String UPDATED_REQUESTED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_REQUESTED_ON = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_REQUESTED_ON = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_URL = "AAAAAAAAAA";
    private static final String UPDATED_URL = "BBBBBBBBBB";

    private static final Boolean DEFAULT_DONE = false;
    private static final Boolean UPDATED_DONE = true;

    private static final String ENTITY_API_URL = "/api/music-requests";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/music-requests/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MusicRequestRepository musicRequestRepository;

    @Autowired
    private MusicRequestSearchRepository musicRequestSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMusicRequestMockMvc;

    private MusicRequest musicRequest;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MusicRequest createEntity(EntityManager em) {
        MusicRequest musicRequest = new MusicRequest()
            .song(DEFAULT_SONG)
            .artist(DEFAULT_ARTIST)
            .album(DEFAULT_ALBUM)
            .genre(DEFAULT_GENRE)
            .requestedBy(DEFAULT_REQUESTED_BY)
            .requestedOn(DEFAULT_REQUESTED_ON)
            .url(DEFAULT_URL)
            .done(DEFAULT_DONE);
        return musicRequest;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MusicRequest createUpdatedEntity(EntityManager em) {
        MusicRequest musicRequest = new MusicRequest()
            .song(UPDATED_SONG)
            .artist(UPDATED_ARTIST)
            .album(UPDATED_ALBUM)
            .genre(UPDATED_GENRE)
            .requestedBy(UPDATED_REQUESTED_BY)
            .requestedOn(UPDATED_REQUESTED_ON)
            .url(UPDATED_URL)
            .done(UPDATED_DONE);
        return musicRequest;
    }

    @AfterEach
    public void cleanupElasticSearchRepository() {
        musicRequestSearchRepository.deleteAll();
        assertThat(musicRequestSearchRepository.count()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        musicRequest = createEntity(em);
    }

    @Test
    @Transactional
    void createMusicRequest() throws Exception {
        int databaseSizeBeforeCreate = musicRequestRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(musicRequestSearchRepository.findAll());
        // Create the MusicRequest
        restMusicRequestMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(musicRequest))
            )
            .andExpect(status().isCreated());

        // Validate the MusicRequest in the database
        List<MusicRequest> musicRequestList = musicRequestRepository.findAll();
        assertThat(musicRequestList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(musicRequestSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        MusicRequest testMusicRequest = musicRequestList.get(musicRequestList.size() - 1);
        assertThat(testMusicRequest.getSong()).isEqualTo(DEFAULT_SONG);
        assertThat(testMusicRequest.getArtist()).isEqualTo(DEFAULT_ARTIST);
        assertThat(testMusicRequest.getAlbum()).isEqualTo(DEFAULT_ALBUM);
        assertThat(testMusicRequest.getGenre()).isEqualTo(DEFAULT_GENRE);
        assertThat(testMusicRequest.getRequestedBy()).isEqualTo(DEFAULT_REQUESTED_BY);
        assertThat(testMusicRequest.getRequestedOn()).isEqualTo(DEFAULT_REQUESTED_ON);
        assertThat(testMusicRequest.getUrl()).isEqualTo(DEFAULT_URL);
        assertThat(testMusicRequest.getDone()).isEqualTo(DEFAULT_DONE);
    }

    @Test
    @Transactional
    void createMusicRequestWithExistingId() throws Exception {
        // Create the MusicRequest with an existing ID
        musicRequest.setId(1L);

        int databaseSizeBeforeCreate = musicRequestRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(musicRequestSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restMusicRequestMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(musicRequest))
            )
            .andExpect(status().isBadRequest());

        // Validate the MusicRequest in the database
        List<MusicRequest> musicRequestList = musicRequestRepository.findAll();
        assertThat(musicRequestList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(musicRequestSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllMusicRequests() throws Exception {
        // Initialize the database
        musicRequestRepository.saveAndFlush(musicRequest);

        // Get all the musicRequestList
        restMusicRequestMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(musicRequest.getId().intValue())))
            .andExpect(jsonPath("$.[*].song").value(hasItem(DEFAULT_SONG)))
            .andExpect(jsonPath("$.[*].artist").value(hasItem(DEFAULT_ARTIST)))
            .andExpect(jsonPath("$.[*].album").value(hasItem(DEFAULT_ALBUM)))
            .andExpect(jsonPath("$.[*].genre").value(hasItem(DEFAULT_GENRE)))
            .andExpect(jsonPath("$.[*].requestedBy").value(hasItem(DEFAULT_REQUESTED_BY)))
            .andExpect(jsonPath("$.[*].requestedOn").value(hasItem(DEFAULT_REQUESTED_ON.toString())))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL)))
            .andExpect(jsonPath("$.[*].done").value(hasItem(DEFAULT_DONE.booleanValue())));
    }

    @Test
    @Transactional
    void getMusicRequest() throws Exception {
        // Initialize the database
        musicRequestRepository.saveAndFlush(musicRequest);

        // Get the musicRequest
        restMusicRequestMockMvc
            .perform(get(ENTITY_API_URL_ID, musicRequest.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(musicRequest.getId().intValue()))
            .andExpect(jsonPath("$.song").value(DEFAULT_SONG))
            .andExpect(jsonPath("$.artist").value(DEFAULT_ARTIST))
            .andExpect(jsonPath("$.album").value(DEFAULT_ALBUM))
            .andExpect(jsonPath("$.genre").value(DEFAULT_GENRE))
            .andExpect(jsonPath("$.requestedBy").value(DEFAULT_REQUESTED_BY))
            .andExpect(jsonPath("$.requestedOn").value(DEFAULT_REQUESTED_ON.toString()))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL))
            .andExpect(jsonPath("$.done").value(DEFAULT_DONE.booleanValue()));
    }

    @Test
    @Transactional
    void getMusicRequestsByIdFiltering() throws Exception {
        // Initialize the database
        musicRequestRepository.saveAndFlush(musicRequest);

        Long id = musicRequest.getId();

        defaultMusicRequestShouldBeFound("id.equals=" + id);
        defaultMusicRequestShouldNotBeFound("id.notEquals=" + id);

        defaultMusicRequestShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultMusicRequestShouldNotBeFound("id.greaterThan=" + id);

        defaultMusicRequestShouldBeFound("id.lessThanOrEqual=" + id);
        defaultMusicRequestShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllMusicRequestsBySongIsEqualToSomething() throws Exception {
        // Initialize the database
        musicRequestRepository.saveAndFlush(musicRequest);

        // Get all the musicRequestList where song equals to DEFAULT_SONG
        defaultMusicRequestShouldBeFound("song.equals=" + DEFAULT_SONG);

        // Get all the musicRequestList where song equals to UPDATED_SONG
        defaultMusicRequestShouldNotBeFound("song.equals=" + UPDATED_SONG);
    }

    @Test
    @Transactional
    void getAllMusicRequestsBySongIsInShouldWork() throws Exception {
        // Initialize the database
        musicRequestRepository.saveAndFlush(musicRequest);

        // Get all the musicRequestList where song in DEFAULT_SONG or UPDATED_SONG
        defaultMusicRequestShouldBeFound("song.in=" + DEFAULT_SONG + "," + UPDATED_SONG);

        // Get all the musicRequestList where song equals to UPDATED_SONG
        defaultMusicRequestShouldNotBeFound("song.in=" + UPDATED_SONG);
    }

    @Test
    @Transactional
    void getAllMusicRequestsBySongIsNullOrNotNull() throws Exception {
        // Initialize the database
        musicRequestRepository.saveAndFlush(musicRequest);

        // Get all the musicRequestList where song is not null
        defaultMusicRequestShouldBeFound("song.specified=true");

        // Get all the musicRequestList where song is null
        defaultMusicRequestShouldNotBeFound("song.specified=false");
    }

    @Test
    @Transactional
    void getAllMusicRequestsBySongContainsSomething() throws Exception {
        // Initialize the database
        musicRequestRepository.saveAndFlush(musicRequest);

        // Get all the musicRequestList where song contains DEFAULT_SONG
        defaultMusicRequestShouldBeFound("song.contains=" + DEFAULT_SONG);

        // Get all the musicRequestList where song contains UPDATED_SONG
        defaultMusicRequestShouldNotBeFound("song.contains=" + UPDATED_SONG);
    }

    @Test
    @Transactional
    void getAllMusicRequestsBySongNotContainsSomething() throws Exception {
        // Initialize the database
        musicRequestRepository.saveAndFlush(musicRequest);

        // Get all the musicRequestList where song does not contain DEFAULT_SONG
        defaultMusicRequestShouldNotBeFound("song.doesNotContain=" + DEFAULT_SONG);

        // Get all the musicRequestList where song does not contain UPDATED_SONG
        defaultMusicRequestShouldBeFound("song.doesNotContain=" + UPDATED_SONG);
    }

    @Test
    @Transactional
    void getAllMusicRequestsByArtistIsEqualToSomething() throws Exception {
        // Initialize the database
        musicRequestRepository.saveAndFlush(musicRequest);

        // Get all the musicRequestList where artist equals to DEFAULT_ARTIST
        defaultMusicRequestShouldBeFound("artist.equals=" + DEFAULT_ARTIST);

        // Get all the musicRequestList where artist equals to UPDATED_ARTIST
        defaultMusicRequestShouldNotBeFound("artist.equals=" + UPDATED_ARTIST);
    }

    @Test
    @Transactional
    void getAllMusicRequestsByArtistIsInShouldWork() throws Exception {
        // Initialize the database
        musicRequestRepository.saveAndFlush(musicRequest);

        // Get all the musicRequestList where artist in DEFAULT_ARTIST or UPDATED_ARTIST
        defaultMusicRequestShouldBeFound("artist.in=" + DEFAULT_ARTIST + "," + UPDATED_ARTIST);

        // Get all the musicRequestList where artist equals to UPDATED_ARTIST
        defaultMusicRequestShouldNotBeFound("artist.in=" + UPDATED_ARTIST);
    }

    @Test
    @Transactional
    void getAllMusicRequestsByArtistIsNullOrNotNull() throws Exception {
        // Initialize the database
        musicRequestRepository.saveAndFlush(musicRequest);

        // Get all the musicRequestList where artist is not null
        defaultMusicRequestShouldBeFound("artist.specified=true");

        // Get all the musicRequestList where artist is null
        defaultMusicRequestShouldNotBeFound("artist.specified=false");
    }

    @Test
    @Transactional
    void getAllMusicRequestsByArtistContainsSomething() throws Exception {
        // Initialize the database
        musicRequestRepository.saveAndFlush(musicRequest);

        // Get all the musicRequestList where artist contains DEFAULT_ARTIST
        defaultMusicRequestShouldBeFound("artist.contains=" + DEFAULT_ARTIST);

        // Get all the musicRequestList where artist contains UPDATED_ARTIST
        defaultMusicRequestShouldNotBeFound("artist.contains=" + UPDATED_ARTIST);
    }

    @Test
    @Transactional
    void getAllMusicRequestsByArtistNotContainsSomething() throws Exception {
        // Initialize the database
        musicRequestRepository.saveAndFlush(musicRequest);

        // Get all the musicRequestList where artist does not contain DEFAULT_ARTIST
        defaultMusicRequestShouldNotBeFound("artist.doesNotContain=" + DEFAULT_ARTIST);

        // Get all the musicRequestList where artist does not contain UPDATED_ARTIST
        defaultMusicRequestShouldBeFound("artist.doesNotContain=" + UPDATED_ARTIST);
    }

    @Test
    @Transactional
    void getAllMusicRequestsByAlbumIsEqualToSomething() throws Exception {
        // Initialize the database
        musicRequestRepository.saveAndFlush(musicRequest);

        // Get all the musicRequestList where album equals to DEFAULT_ALBUM
        defaultMusicRequestShouldBeFound("album.equals=" + DEFAULT_ALBUM);

        // Get all the musicRequestList where album equals to UPDATED_ALBUM
        defaultMusicRequestShouldNotBeFound("album.equals=" + UPDATED_ALBUM);
    }

    @Test
    @Transactional
    void getAllMusicRequestsByAlbumIsInShouldWork() throws Exception {
        // Initialize the database
        musicRequestRepository.saveAndFlush(musicRequest);

        // Get all the musicRequestList where album in DEFAULT_ALBUM or UPDATED_ALBUM
        defaultMusicRequestShouldBeFound("album.in=" + DEFAULT_ALBUM + "," + UPDATED_ALBUM);

        // Get all the musicRequestList where album equals to UPDATED_ALBUM
        defaultMusicRequestShouldNotBeFound("album.in=" + UPDATED_ALBUM);
    }

    @Test
    @Transactional
    void getAllMusicRequestsByAlbumIsNullOrNotNull() throws Exception {
        // Initialize the database
        musicRequestRepository.saveAndFlush(musicRequest);

        // Get all the musicRequestList where album is not null
        defaultMusicRequestShouldBeFound("album.specified=true");

        // Get all the musicRequestList where album is null
        defaultMusicRequestShouldNotBeFound("album.specified=false");
    }

    @Test
    @Transactional
    void getAllMusicRequestsByAlbumContainsSomething() throws Exception {
        // Initialize the database
        musicRequestRepository.saveAndFlush(musicRequest);

        // Get all the musicRequestList where album contains DEFAULT_ALBUM
        defaultMusicRequestShouldBeFound("album.contains=" + DEFAULT_ALBUM);

        // Get all the musicRequestList where album contains UPDATED_ALBUM
        defaultMusicRequestShouldNotBeFound("album.contains=" + UPDATED_ALBUM);
    }

    @Test
    @Transactional
    void getAllMusicRequestsByAlbumNotContainsSomething() throws Exception {
        // Initialize the database
        musicRequestRepository.saveAndFlush(musicRequest);

        // Get all the musicRequestList where album does not contain DEFAULT_ALBUM
        defaultMusicRequestShouldNotBeFound("album.doesNotContain=" + DEFAULT_ALBUM);

        // Get all the musicRequestList where album does not contain UPDATED_ALBUM
        defaultMusicRequestShouldBeFound("album.doesNotContain=" + UPDATED_ALBUM);
    }

    @Test
    @Transactional
    void getAllMusicRequestsByGenreIsEqualToSomething() throws Exception {
        // Initialize the database
        musicRequestRepository.saveAndFlush(musicRequest);

        // Get all the musicRequestList where genre equals to DEFAULT_GENRE
        defaultMusicRequestShouldBeFound("genre.equals=" + DEFAULT_GENRE);

        // Get all the musicRequestList where genre equals to UPDATED_GENRE
        defaultMusicRequestShouldNotBeFound("genre.equals=" + UPDATED_GENRE);
    }

    @Test
    @Transactional
    void getAllMusicRequestsByGenreIsInShouldWork() throws Exception {
        // Initialize the database
        musicRequestRepository.saveAndFlush(musicRequest);

        // Get all the musicRequestList where genre in DEFAULT_GENRE or UPDATED_GENRE
        defaultMusicRequestShouldBeFound("genre.in=" + DEFAULT_GENRE + "," + UPDATED_GENRE);

        // Get all the musicRequestList where genre equals to UPDATED_GENRE
        defaultMusicRequestShouldNotBeFound("genre.in=" + UPDATED_GENRE);
    }

    @Test
    @Transactional
    void getAllMusicRequestsByGenreIsNullOrNotNull() throws Exception {
        // Initialize the database
        musicRequestRepository.saveAndFlush(musicRequest);

        // Get all the musicRequestList where genre is not null
        defaultMusicRequestShouldBeFound("genre.specified=true");

        // Get all the musicRequestList where genre is null
        defaultMusicRequestShouldNotBeFound("genre.specified=false");
    }

    @Test
    @Transactional
    void getAllMusicRequestsByGenreContainsSomething() throws Exception {
        // Initialize the database
        musicRequestRepository.saveAndFlush(musicRequest);

        // Get all the musicRequestList where genre contains DEFAULT_GENRE
        defaultMusicRequestShouldBeFound("genre.contains=" + DEFAULT_GENRE);

        // Get all the musicRequestList where genre contains UPDATED_GENRE
        defaultMusicRequestShouldNotBeFound("genre.contains=" + UPDATED_GENRE);
    }

    @Test
    @Transactional
    void getAllMusicRequestsByGenreNotContainsSomething() throws Exception {
        // Initialize the database
        musicRequestRepository.saveAndFlush(musicRequest);

        // Get all the musicRequestList where genre does not contain DEFAULT_GENRE
        defaultMusicRequestShouldNotBeFound("genre.doesNotContain=" + DEFAULT_GENRE);

        // Get all the musicRequestList where genre does not contain UPDATED_GENRE
        defaultMusicRequestShouldBeFound("genre.doesNotContain=" + UPDATED_GENRE);
    }

    @Test
    @Transactional
    void getAllMusicRequestsByRequestedByIsEqualToSomething() throws Exception {
        // Initialize the database
        musicRequestRepository.saveAndFlush(musicRequest);

        // Get all the musicRequestList where requestedBy equals to DEFAULT_REQUESTED_BY
        defaultMusicRequestShouldBeFound("requestedBy.equals=" + DEFAULT_REQUESTED_BY);

        // Get all the musicRequestList where requestedBy equals to UPDATED_REQUESTED_BY
        defaultMusicRequestShouldNotBeFound("requestedBy.equals=" + UPDATED_REQUESTED_BY);
    }

    @Test
    @Transactional
    void getAllMusicRequestsByRequestedByIsInShouldWork() throws Exception {
        // Initialize the database
        musicRequestRepository.saveAndFlush(musicRequest);

        // Get all the musicRequestList where requestedBy in DEFAULT_REQUESTED_BY or UPDATED_REQUESTED_BY
        defaultMusicRequestShouldBeFound("requestedBy.in=" + DEFAULT_REQUESTED_BY + "," + UPDATED_REQUESTED_BY);

        // Get all the musicRequestList where requestedBy equals to UPDATED_REQUESTED_BY
        defaultMusicRequestShouldNotBeFound("requestedBy.in=" + UPDATED_REQUESTED_BY);
    }

    @Test
    @Transactional
    void getAllMusicRequestsByRequestedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        musicRequestRepository.saveAndFlush(musicRequest);

        // Get all the musicRequestList where requestedBy is not null
        defaultMusicRequestShouldBeFound("requestedBy.specified=true");

        // Get all the musicRequestList where requestedBy is null
        defaultMusicRequestShouldNotBeFound("requestedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllMusicRequestsByRequestedByContainsSomething() throws Exception {
        // Initialize the database
        musicRequestRepository.saveAndFlush(musicRequest);

        // Get all the musicRequestList where requestedBy contains DEFAULT_REQUESTED_BY
        defaultMusicRequestShouldBeFound("requestedBy.contains=" + DEFAULT_REQUESTED_BY);

        // Get all the musicRequestList where requestedBy contains UPDATED_REQUESTED_BY
        defaultMusicRequestShouldNotBeFound("requestedBy.contains=" + UPDATED_REQUESTED_BY);
    }

    @Test
    @Transactional
    void getAllMusicRequestsByRequestedByNotContainsSomething() throws Exception {
        // Initialize the database
        musicRequestRepository.saveAndFlush(musicRequest);

        // Get all the musicRequestList where requestedBy does not contain DEFAULT_REQUESTED_BY
        defaultMusicRequestShouldNotBeFound("requestedBy.doesNotContain=" + DEFAULT_REQUESTED_BY);

        // Get all the musicRequestList where requestedBy does not contain UPDATED_REQUESTED_BY
        defaultMusicRequestShouldBeFound("requestedBy.doesNotContain=" + UPDATED_REQUESTED_BY);
    }

    @Test
    @Transactional
    void getAllMusicRequestsByRequestedOnIsEqualToSomething() throws Exception {
        // Initialize the database
        musicRequestRepository.saveAndFlush(musicRequest);

        // Get all the musicRequestList where requestedOn equals to DEFAULT_REQUESTED_ON
        defaultMusicRequestShouldBeFound("requestedOn.equals=" + DEFAULT_REQUESTED_ON);

        // Get all the musicRequestList where requestedOn equals to UPDATED_REQUESTED_ON
        defaultMusicRequestShouldNotBeFound("requestedOn.equals=" + UPDATED_REQUESTED_ON);
    }

    @Test
    @Transactional
    void getAllMusicRequestsByRequestedOnIsInShouldWork() throws Exception {
        // Initialize the database
        musicRequestRepository.saveAndFlush(musicRequest);

        // Get all the musicRequestList where requestedOn in DEFAULT_REQUESTED_ON or UPDATED_REQUESTED_ON
        defaultMusicRequestShouldBeFound("requestedOn.in=" + DEFAULT_REQUESTED_ON + "," + UPDATED_REQUESTED_ON);

        // Get all the musicRequestList where requestedOn equals to UPDATED_REQUESTED_ON
        defaultMusicRequestShouldNotBeFound("requestedOn.in=" + UPDATED_REQUESTED_ON);
    }

    @Test
    @Transactional
    void getAllMusicRequestsByRequestedOnIsNullOrNotNull() throws Exception {
        // Initialize the database
        musicRequestRepository.saveAndFlush(musicRequest);

        // Get all the musicRequestList where requestedOn is not null
        defaultMusicRequestShouldBeFound("requestedOn.specified=true");

        // Get all the musicRequestList where requestedOn is null
        defaultMusicRequestShouldNotBeFound("requestedOn.specified=false");
    }

    @Test
    @Transactional
    void getAllMusicRequestsByUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        musicRequestRepository.saveAndFlush(musicRequest);

        // Get all the musicRequestList where url equals to DEFAULT_URL
        defaultMusicRequestShouldBeFound("url.equals=" + DEFAULT_URL);

        // Get all the musicRequestList where url equals to UPDATED_URL
        defaultMusicRequestShouldNotBeFound("url.equals=" + UPDATED_URL);
    }

    @Test
    @Transactional
    void getAllMusicRequestsByUrlIsInShouldWork() throws Exception {
        // Initialize the database
        musicRequestRepository.saveAndFlush(musicRequest);

        // Get all the musicRequestList where url in DEFAULT_URL or UPDATED_URL
        defaultMusicRequestShouldBeFound("url.in=" + DEFAULT_URL + "," + UPDATED_URL);

        // Get all the musicRequestList where url equals to UPDATED_URL
        defaultMusicRequestShouldNotBeFound("url.in=" + UPDATED_URL);
    }

    @Test
    @Transactional
    void getAllMusicRequestsByUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        musicRequestRepository.saveAndFlush(musicRequest);

        // Get all the musicRequestList where url is not null
        defaultMusicRequestShouldBeFound("url.specified=true");

        // Get all the musicRequestList where url is null
        defaultMusicRequestShouldNotBeFound("url.specified=false");
    }

    @Test
    @Transactional
    void getAllMusicRequestsByUrlContainsSomething() throws Exception {
        // Initialize the database
        musicRequestRepository.saveAndFlush(musicRequest);

        // Get all the musicRequestList where url contains DEFAULT_URL
        defaultMusicRequestShouldBeFound("url.contains=" + DEFAULT_URL);

        // Get all the musicRequestList where url contains UPDATED_URL
        defaultMusicRequestShouldNotBeFound("url.contains=" + UPDATED_URL);
    }

    @Test
    @Transactional
    void getAllMusicRequestsByUrlNotContainsSomething() throws Exception {
        // Initialize the database
        musicRequestRepository.saveAndFlush(musicRequest);

        // Get all the musicRequestList where url does not contain DEFAULT_URL
        defaultMusicRequestShouldNotBeFound("url.doesNotContain=" + DEFAULT_URL);

        // Get all the musicRequestList where url does not contain UPDATED_URL
        defaultMusicRequestShouldBeFound("url.doesNotContain=" + UPDATED_URL);
    }

    @Test
    @Transactional
    void getAllMusicRequestsByDoneIsEqualToSomething() throws Exception {
        // Initialize the database
        musicRequestRepository.saveAndFlush(musicRequest);

        // Get all the musicRequestList where done equals to DEFAULT_DONE
        defaultMusicRequestShouldBeFound("done.equals=" + DEFAULT_DONE);

        // Get all the musicRequestList where done equals to UPDATED_DONE
        defaultMusicRequestShouldNotBeFound("done.equals=" + UPDATED_DONE);
    }

    @Test
    @Transactional
    void getAllMusicRequestsByDoneIsInShouldWork() throws Exception {
        // Initialize the database
        musicRequestRepository.saveAndFlush(musicRequest);

        // Get all the musicRequestList where done in DEFAULT_DONE or UPDATED_DONE
        defaultMusicRequestShouldBeFound("done.in=" + DEFAULT_DONE + "," + UPDATED_DONE);

        // Get all the musicRequestList where done equals to UPDATED_DONE
        defaultMusicRequestShouldNotBeFound("done.in=" + UPDATED_DONE);
    }

    @Test
    @Transactional
    void getAllMusicRequestsByDoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        musicRequestRepository.saveAndFlush(musicRequest);

        // Get all the musicRequestList where done is not null
        defaultMusicRequestShouldBeFound("done.specified=true");

        // Get all the musicRequestList where done is null
        defaultMusicRequestShouldNotBeFound("done.specified=false");
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMusicRequestShouldBeFound(String filter) throws Exception {
        restMusicRequestMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(musicRequest.getId().intValue())))
            .andExpect(jsonPath("$.[*].song").value(hasItem(DEFAULT_SONG)))
            .andExpect(jsonPath("$.[*].artist").value(hasItem(DEFAULT_ARTIST)))
            .andExpect(jsonPath("$.[*].album").value(hasItem(DEFAULT_ALBUM)))
            .andExpect(jsonPath("$.[*].genre").value(hasItem(DEFAULT_GENRE)))
            .andExpect(jsonPath("$.[*].requestedBy").value(hasItem(DEFAULT_REQUESTED_BY)))
            .andExpect(jsonPath("$.[*].requestedOn").value(hasItem(DEFAULT_REQUESTED_ON.toString())))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL)))
            .andExpect(jsonPath("$.[*].done").value(hasItem(DEFAULT_DONE.booleanValue())));

        // Check, that the count call also returns 1
        restMusicRequestMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMusicRequestShouldNotBeFound(String filter) throws Exception {
        restMusicRequestMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMusicRequestMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingMusicRequest() throws Exception {
        // Get the musicRequest
        restMusicRequestMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMusicRequest() throws Exception {
        // Initialize the database
        musicRequestRepository.saveAndFlush(musicRequest);

        int databaseSizeBeforeUpdate = musicRequestRepository.findAll().size();
        musicRequestSearchRepository.save(musicRequest);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(musicRequestSearchRepository.findAll());

        // Update the musicRequest
        MusicRequest updatedMusicRequest = musicRequestRepository.findById(musicRequest.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMusicRequest are not directly saved in db
        em.detach(updatedMusicRequest);
        updatedMusicRequest
            .song(UPDATED_SONG)
            .artist(UPDATED_ARTIST)
            .album(UPDATED_ALBUM)
            .genre(UPDATED_GENRE)
            .requestedBy(UPDATED_REQUESTED_BY)
            .requestedOn(UPDATED_REQUESTED_ON)
            .url(UPDATED_URL)
            .done(UPDATED_DONE);

        restMusicRequestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedMusicRequest.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedMusicRequest))
            )
            .andExpect(status().isOk());

        // Validate the MusicRequest in the database
        List<MusicRequest> musicRequestList = musicRequestRepository.findAll();
        assertThat(musicRequestList).hasSize(databaseSizeBeforeUpdate);
        MusicRequest testMusicRequest = musicRequestList.get(musicRequestList.size() - 1);
        assertThat(testMusicRequest.getSong()).isEqualTo(UPDATED_SONG);
        assertThat(testMusicRequest.getArtist()).isEqualTo(UPDATED_ARTIST);
        assertThat(testMusicRequest.getAlbum()).isEqualTo(UPDATED_ALBUM);
        assertThat(testMusicRequest.getGenre()).isEqualTo(UPDATED_GENRE);
        assertThat(testMusicRequest.getRequestedBy()).isEqualTo(UPDATED_REQUESTED_BY);
        assertThat(testMusicRequest.getRequestedOn()).isEqualTo(UPDATED_REQUESTED_ON);
        assertThat(testMusicRequest.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testMusicRequest.getDone()).isEqualTo(UPDATED_DONE);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(musicRequestSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<MusicRequest> musicRequestSearchList = IterableUtils.toList(musicRequestSearchRepository.findAll());
                MusicRequest testMusicRequestSearch = musicRequestSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testMusicRequestSearch.getSong()).isEqualTo(UPDATED_SONG);
                assertThat(testMusicRequestSearch.getArtist()).isEqualTo(UPDATED_ARTIST);
                assertThat(testMusicRequestSearch.getAlbum()).isEqualTo(UPDATED_ALBUM);
                assertThat(testMusicRequestSearch.getGenre()).isEqualTo(UPDATED_GENRE);
                assertThat(testMusicRequestSearch.getRequestedBy()).isEqualTo(UPDATED_REQUESTED_BY);
                assertThat(testMusicRequestSearch.getRequestedOn()).isEqualTo(UPDATED_REQUESTED_ON);
                assertThat(testMusicRequestSearch.getUrl()).isEqualTo(UPDATED_URL);
                assertThat(testMusicRequestSearch.getDone()).isEqualTo(UPDATED_DONE);
            });
    }

    @Test
    @Transactional
    void putNonExistingMusicRequest() throws Exception {
        int databaseSizeBeforeUpdate = musicRequestRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(musicRequestSearchRepository.findAll());
        musicRequest.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMusicRequestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, musicRequest.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(musicRequest))
            )
            .andExpect(status().isBadRequest());

        // Validate the MusicRequest in the database
        List<MusicRequest> musicRequestList = musicRequestRepository.findAll();
        assertThat(musicRequestList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(musicRequestSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchMusicRequest() throws Exception {
        int databaseSizeBeforeUpdate = musicRequestRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(musicRequestSearchRepository.findAll());
        musicRequest.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMusicRequestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(musicRequest))
            )
            .andExpect(status().isBadRequest());

        // Validate the MusicRequest in the database
        List<MusicRequest> musicRequestList = musicRequestRepository.findAll();
        assertThat(musicRequestList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(musicRequestSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMusicRequest() throws Exception {
        int databaseSizeBeforeUpdate = musicRequestRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(musicRequestSearchRepository.findAll());
        musicRequest.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMusicRequestMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(musicRequest))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MusicRequest in the database
        List<MusicRequest> musicRequestList = musicRequestRepository.findAll();
        assertThat(musicRequestList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(musicRequestSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateMusicRequestWithPatch() throws Exception {
        // Initialize the database
        musicRequestRepository.saveAndFlush(musicRequest);

        int databaseSizeBeforeUpdate = musicRequestRepository.findAll().size();

        // Update the musicRequest using partial update
        MusicRequest partialUpdatedMusicRequest = new MusicRequest();
        partialUpdatedMusicRequest.setId(musicRequest.getId());

        partialUpdatedMusicRequest.song(UPDATED_SONG).artist(UPDATED_ARTIST).album(UPDATED_ALBUM).requestedOn(UPDATED_REQUESTED_ON);

        restMusicRequestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMusicRequest.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMusicRequest))
            )
            .andExpect(status().isOk());

        // Validate the MusicRequest in the database
        List<MusicRequest> musicRequestList = musicRequestRepository.findAll();
        assertThat(musicRequestList).hasSize(databaseSizeBeforeUpdate);
        MusicRequest testMusicRequest = musicRequestList.get(musicRequestList.size() - 1);
        assertThat(testMusicRequest.getSong()).isEqualTo(UPDATED_SONG);
        assertThat(testMusicRequest.getArtist()).isEqualTo(UPDATED_ARTIST);
        assertThat(testMusicRequest.getAlbum()).isEqualTo(UPDATED_ALBUM);
        assertThat(testMusicRequest.getGenre()).isEqualTo(DEFAULT_GENRE);
        assertThat(testMusicRequest.getRequestedBy()).isEqualTo(DEFAULT_REQUESTED_BY);
        assertThat(testMusicRequest.getRequestedOn()).isEqualTo(UPDATED_REQUESTED_ON);
        assertThat(testMusicRequest.getUrl()).isEqualTo(DEFAULT_URL);
        assertThat(testMusicRequest.getDone()).isEqualTo(DEFAULT_DONE);
    }

    @Test
    @Transactional
    void fullUpdateMusicRequestWithPatch() throws Exception {
        // Initialize the database
        musicRequestRepository.saveAndFlush(musicRequest);

        int databaseSizeBeforeUpdate = musicRequestRepository.findAll().size();

        // Update the musicRequest using partial update
        MusicRequest partialUpdatedMusicRequest = new MusicRequest();
        partialUpdatedMusicRequest.setId(musicRequest.getId());

        partialUpdatedMusicRequest
            .song(UPDATED_SONG)
            .artist(UPDATED_ARTIST)
            .album(UPDATED_ALBUM)
            .genre(UPDATED_GENRE)
            .requestedBy(UPDATED_REQUESTED_BY)
            .requestedOn(UPDATED_REQUESTED_ON)
            .url(UPDATED_URL)
            .done(UPDATED_DONE);

        restMusicRequestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMusicRequest.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMusicRequest))
            )
            .andExpect(status().isOk());

        // Validate the MusicRequest in the database
        List<MusicRequest> musicRequestList = musicRequestRepository.findAll();
        assertThat(musicRequestList).hasSize(databaseSizeBeforeUpdate);
        MusicRequest testMusicRequest = musicRequestList.get(musicRequestList.size() - 1);
        assertThat(testMusicRequest.getSong()).isEqualTo(UPDATED_SONG);
        assertThat(testMusicRequest.getArtist()).isEqualTo(UPDATED_ARTIST);
        assertThat(testMusicRequest.getAlbum()).isEqualTo(UPDATED_ALBUM);
        assertThat(testMusicRequest.getGenre()).isEqualTo(UPDATED_GENRE);
        assertThat(testMusicRequest.getRequestedBy()).isEqualTo(UPDATED_REQUESTED_BY);
        assertThat(testMusicRequest.getRequestedOn()).isEqualTo(UPDATED_REQUESTED_ON);
        assertThat(testMusicRequest.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testMusicRequest.getDone()).isEqualTo(UPDATED_DONE);
    }

    @Test
    @Transactional
    void patchNonExistingMusicRequest() throws Exception {
        int databaseSizeBeforeUpdate = musicRequestRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(musicRequestSearchRepository.findAll());
        musicRequest.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMusicRequestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, musicRequest.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(musicRequest))
            )
            .andExpect(status().isBadRequest());

        // Validate the MusicRequest in the database
        List<MusicRequest> musicRequestList = musicRequestRepository.findAll();
        assertThat(musicRequestList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(musicRequestSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMusicRequest() throws Exception {
        int databaseSizeBeforeUpdate = musicRequestRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(musicRequestSearchRepository.findAll());
        musicRequest.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMusicRequestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(musicRequest))
            )
            .andExpect(status().isBadRequest());

        // Validate the MusicRequest in the database
        List<MusicRequest> musicRequestList = musicRequestRepository.findAll();
        assertThat(musicRequestList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(musicRequestSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMusicRequest() throws Exception {
        int databaseSizeBeforeUpdate = musicRequestRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(musicRequestSearchRepository.findAll());
        musicRequest.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMusicRequestMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(musicRequest))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MusicRequest in the database
        List<MusicRequest> musicRequestList = musicRequestRepository.findAll();
        assertThat(musicRequestList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(musicRequestSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteMusicRequest() throws Exception {
        // Initialize the database
        musicRequestRepository.saveAndFlush(musicRequest);
        musicRequestRepository.save(musicRequest);
        musicRequestSearchRepository.save(musicRequest);

        int databaseSizeBeforeDelete = musicRequestRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(musicRequestSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the musicRequest
        restMusicRequestMockMvc
            .perform(delete(ENTITY_API_URL_ID, musicRequest.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<MusicRequest> musicRequestList = musicRequestRepository.findAll();
        assertThat(musicRequestList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(musicRequestSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchMusicRequest() throws Exception {
        // Initialize the database
        musicRequest = musicRequestRepository.saveAndFlush(musicRequest);
        musicRequestSearchRepository.save(musicRequest);

        // Search the musicRequest
        restMusicRequestMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + musicRequest.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(musicRequest.getId().intValue())))
            .andExpect(jsonPath("$.[*].song").value(hasItem(DEFAULT_SONG)))
            .andExpect(jsonPath("$.[*].artist").value(hasItem(DEFAULT_ARTIST)))
            .andExpect(jsonPath("$.[*].album").value(hasItem(DEFAULT_ALBUM)))
            .andExpect(jsonPath("$.[*].genre").value(hasItem(DEFAULT_GENRE)))
            .andExpect(jsonPath("$.[*].requestedBy").value(hasItem(DEFAULT_REQUESTED_BY)))
            .andExpect(jsonPath("$.[*].requestedOn").value(hasItem(DEFAULT_REQUESTED_ON.toString())))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL)))
            .andExpect(jsonPath("$.[*].done").value(hasItem(DEFAULT_DONE.booleanValue())));
    }
}
