package net.vegatec.media_library.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import net.vegatec.media_library.IntegrationTest;
import net.vegatec.media_library.query.domain.Track;
import net.vegatec.media_library.query.domain.TrackType;
import net.vegatec.media_library.query.repository.TrackRepository;
import net.vegatec.media_library.query.repository.search.TrackSearchRepository;
import net.vegatec.media_library.query.service.TrackService;
import net.vegatec.media_library.query.service.dto.TrackDTO;
import net.vegatec.media_library.query.service.mapper.TrackMapper;
import net.vegatec.media_library.query.web.rest.TrackResource;
import org.apache.commons.collections4.IterableUtils;
import org.assertj.core.util.IterableUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link TrackResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class TrackResourceIT {

    private static final String DEFAULT_FILE_PATH = "AAAAAAAAAA";
    private static final String UPDATED_FILE_PATH = "BBBBBBBBBB";

    private static final String DEFAULT_SUBFOLDER = "AAAAAAAAAA";
    private static final String UPDATED_SUBFOLDER = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SORT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_SORT_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ARTIST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ARTIST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ARTIST_SORT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ARTIST_SORT_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ALBUM_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ALBUM_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ALBUM_SORT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ALBUM_SORT_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ALBUM_ARTIST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ALBUM_ARTIST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ALBUM_ARTIST_SORT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ALBUM_ARTIST_SORT_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_ALBUM_RELEASED_YEAR = 1;
    private static final Integer UPDATED_ALBUM_RELEASED_YEAR = 2;
    private static final Integer SMALLER_ALBUM_RELEASED_YEAR = 1 - 1;

    private static final String DEFAULT_GENRE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_GENRE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_GENRE_SORT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_GENRE_SORT_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_TRACK_NUMBER = 1;
    private static final Integer UPDATED_TRACK_NUMBER = 2;
    private static final Integer SMALLER_TRACK_NUMBER = 1 - 1;

    private static final Integer DEFAULT_PLAYBACK_LENGTH = 1;
    private static final Integer UPDATED_PLAYBACK_LENGTH = 2;
    private static final Integer SMALLER_PLAYBACK_LENGTH = 1 - 1;

    private static final Integer DEFAULT_BIT_RATE = 1;
    private static final Integer UPDATED_BIT_RATE = 2;
    private static final Integer SMALLER_BIT_RATE = 1 - 1;

    private static final Instant DEFAULT_CREATED_ON = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_ON = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Boolean DEFAULT_TAG_VERSION_1 = false;
    private static final Boolean UPDATED_TAG_VERSION_1 = true;

    private static final Boolean DEFAULT_TAG_VERSION_2 = false;
    private static final Boolean UPDATED_TAG_VERSION_2 = true;

    private static final String ENTITY_API_URL = "/api/tracks";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/tracks/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TrackRepository trackRepository;

    @Mock
    private TrackRepository trackRepositoryMock;

    @Autowired
    private TrackMapper trackMapper;

    @Mock
    private TrackService trackServiceMock;

    @Autowired
    private TrackSearchRepository trackSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTrackMockMvc;

    private Track track;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Track createEntity(EntityManager em) {
        Track track = new Track()
            .filePath(DEFAULT_FILE_PATH)
            .subfolder(DEFAULT_SUBFOLDER)
            .name(DEFAULT_NAME)
            .sortName(DEFAULT_SORT_NAME)
            //            .artistName(DEFAULT_ARTIST_NAME)
            //            .artistSortName(DEFAULT_ARTIST_SORT_NAME)
            //            .albumName(DEFAULT_ALBUM_NAME)
            //            .albumSortName(DEFAULT_ALBUM_SORT_NAME)
            //            .albumArtistName(DEFAULT_ALBUM_ARTIST_NAME)
            //            .albumArtistSortName(DEFAULT_ALBUM_ARTIST_SORT_NAME)
            //            .albumReleasedYear(DEFAULT_ALBUM_RELEASED_YEAR)
            //            .genreName(DEFAULT_GENRE_NAME)
            //            .genreSortName(DEFAULT_GENRE_SORT_NAME)
            .trackNumber(DEFAULT_TRACK_NUMBER)
            .playbackLength(DEFAULT_PLAYBACK_LENGTH)
            .bitRate(DEFAULT_BIT_RATE)
            .createdOn(DEFAULT_CREATED_ON)
            .tagVersion1(DEFAULT_TAG_VERSION_1)
            .tagVersion2(DEFAULT_TAG_VERSION_2);
        // Add required entity
        TrackType trackType;
        if (TestUtil.findAll(em, TrackType.class).isEmpty()) {
            trackType = TrackTypeResourceIT.createEntity(em);
            em.persist(trackType);
            em.flush();
        } else {
            trackType = TestUtil.findAll(em, TrackType.class).get(0);
        }
        track.setType(trackType);
        return track;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Track createUpdatedEntity(EntityManager em) {
        Track track = new Track()
            .filePath(UPDATED_FILE_PATH)
            .subfolder(UPDATED_SUBFOLDER)
            .name(UPDATED_NAME)
            .sortName(UPDATED_SORT_NAME)
            //            .artistName(UPDATED_ARTIST_NAME)
            //            .artistSortName(UPDATED_ARTIST_SORT_NAME)
            //            .albumName(UPDATED_ALBUM_NAME)
            //            .albumSortName(UPDATED_ALBUM_SORT_NAME)
            //            .albumArtistName(UPDATED_ALBUM_ARTIST_NAME)
            //            .albumArtistSortName(UPDATED_ALBUM_ARTIST_SORT_NAME)
            //            .albumReleasedYear(UPDATED_ALBUM_RELEASED_YEAR)
            //            .genreName(UPDATED_GENRE_NAME)
            //            .genreSortName(UPDATED_GENRE_SORT_NAME)
            .trackNumber(UPDATED_TRACK_NUMBER)
            .playbackLength(UPDATED_PLAYBACK_LENGTH)
            .bitRate(UPDATED_BIT_RATE)
            .createdOn(UPDATED_CREATED_ON)
            .tagVersion1(UPDATED_TAG_VERSION_1)
            .tagVersion2(UPDATED_TAG_VERSION_2);
        // Add required entity
        TrackType trackType;
        if (TestUtil.findAll(em, TrackType.class).isEmpty()) {
            trackType = TrackTypeResourceIT.createUpdatedEntity(em);
            em.persist(trackType);
            em.flush();
        } else {
            trackType = TestUtil.findAll(em, TrackType.class).get(0);
        }
        track.setType(trackType);
        return track;
    }

    @AfterEach
    public void cleanupElasticSearchRepository() {
        trackSearchRepository.deleteAll();
        assertThat(trackSearchRepository.count()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        track = createEntity(em);
    }

    @Test
    @Transactional
    void createTrack() throws Exception {
        int databaseSizeBeforeCreate = trackRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(trackSearchRepository.findAll());
        // Create the Track
        TrackDTO trackDTO = trackMapper.toDto(track);
        restTrackMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(trackDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Track in the database
        List<Track> trackList = trackRepository.findAll();
        assertThat(trackList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(trackSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        Track testTrack = trackList.get(trackList.size() - 1);
        assertThat(testTrack.getFilePath()).isEqualTo(DEFAULT_FILE_PATH);
        assertThat(testTrack.getSubfolder()).isEqualTo(DEFAULT_SUBFOLDER);
        assertThat(testTrack.getName()).isEqualTo(DEFAULT_NAME);
        //        assertThat(testTrack.getSortName()).isEqualTo(DEFAULT_SORT_NAME);
        //        assertThat(testTrack.getArtistName()).isEqualTo(DEFAULT_ARTIST_NAME);
        //        assertThat(testTrack.getArtistSortName()).isEqualTo(DEFAULT_ARTIST_SORT_NAME);
        //        assertThat(testTrack.getAlbumName()).isEqualTo(DEFAULT_ALBUM_NAME);
        //        assertThat(testTrack.getAlbumSortName()).isEqualTo(DEFAULT_ALBUM_SORT_NAME);
        //        assertThat(testTrack.getAlbumArtistName()).isEqualTo(DEFAULT_ALBUM_ARTIST_NAME);
        //        assertThat(testTrack.getAlbumArtistSortName()).isEqualTo(DEFAULT_ALBUM_ARTIST_SORT_NAME);
        //        assertThat(testTrack.getAlbumReleasedYear()).isEqualTo(DEFAULT_ALBUM_RELEASED_YEAR);
        //        assertThat(testTrack.getGenreName()).isEqualTo(DEFAULT_GENRE_NAME);
        //        assertThat(testTrack.getGenreSortName()).isEqualTo(DEFAULT_GENRE_SORT_NAME);
        assertThat(testTrack.getTrackNumber()).isEqualTo(DEFAULT_TRACK_NUMBER);
        assertThat(testTrack.getPlaybackLength()).isEqualTo(DEFAULT_PLAYBACK_LENGTH);
        assertThat(testTrack.getBitRate()).isEqualTo(DEFAULT_BIT_RATE);
        assertThat(testTrack.getCreatedOn()).isEqualTo(DEFAULT_CREATED_ON);
        assertThat(testTrack.getTagVersion1()).isEqualTo(DEFAULT_TAG_VERSION_1);
        assertThat(testTrack.getTagVersion2()).isEqualTo(DEFAULT_TAG_VERSION_2);
    }

    @Test
    @Transactional
    void createTrackWithExistingId() throws Exception {
        // Create the Track with an existing ID
        track.setId(1L);
        TrackDTO trackDTO = trackMapper.toDto(track);

        int databaseSizeBeforeCreate = trackRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(trackSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restTrackMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(trackDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Track in the database
        List<Track> trackList = trackRepository.findAll();
        assertThat(trackList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(trackSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkFilePathIsRequired() throws Exception {
        int databaseSizeBeforeTest = trackRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(trackSearchRepository.findAll());
        // set the field null
        track.setFilePath(null);

        // Create the Track, which fails.
        TrackDTO trackDTO = trackMapper.toDto(track);

        restTrackMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(trackDTO))
            )
            .andExpect(status().isBadRequest());

        List<Track> trackList = trackRepository.findAll();
        assertThat(trackList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(trackSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkSubfolderIsRequired() throws Exception {
        int databaseSizeBeforeTest = trackRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(trackSearchRepository.findAll());
        // set the field null
        track.setSubfolder(null);

        // Create the Track, which fails.
        TrackDTO trackDTO = trackMapper.toDto(track);

        restTrackMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(trackDTO))
            )
            .andExpect(status().isBadRequest());

        List<Track> trackList = trackRepository.findAll();
        assertThat(trackList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(trackSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = trackRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(trackSearchRepository.findAll());
        // set the field null
        track.setName(null);

        // Create the Track, which fails.
        TrackDTO trackDTO = trackMapper.toDto(track);

        restTrackMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(trackDTO))
            )
            .andExpect(status().isBadRequest());

        List<Track> trackList = trackRepository.findAll();
        assertThat(trackList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(trackSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkSortNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = trackRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(trackSearchRepository.findAll());
        // set the field null
        track.setSortName(null);

        // Create the Track, which fails.
        TrackDTO trackDTO = trackMapper.toDto(track);

        restTrackMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(trackDTO))
            )
            .andExpect(status().isBadRequest());

        List<Track> trackList = trackRepository.findAll();
        assertThat(trackList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(trackSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkArtistNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = trackRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(trackSearchRepository.findAll());
        // set the field null
        //      track.setArtistName(null);

        // Create the Track, which fails.
        TrackDTO trackDTO = trackMapper.toDto(track);

        restTrackMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(trackDTO))
            )
            .andExpect(status().isBadRequest());

        List<Track> trackList = trackRepository.findAll();
        assertThat(trackList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(trackSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkArtistSortNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = trackRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(trackSearchRepository.findAll());
        // set the field null
        //    track.setArtistSortName(null);

        // Create the Track, which fails.
        TrackDTO trackDTO = trackMapper.toDto(track);

        restTrackMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(trackDTO))
            )
            .andExpect(status().isBadRequest());

        List<Track> trackList = trackRepository.findAll();
        assertThat(trackList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(trackSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkAlbumNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = trackRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(trackSearchRepository.findAll());
        // set the field null
        //      track.setAlbumName(null);

        // Create the Track, which fails.
        TrackDTO trackDTO = trackMapper.toDto(track);

        restTrackMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(trackDTO))
            )
            .andExpect(status().isBadRequest());

        List<Track> trackList = trackRepository.findAll();
        assertThat(trackList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(trackSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkAlbumSortNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = trackRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(trackSearchRepository.findAll());
        // set the field null
        //    track.setAlbumSortName(null);

        // Create the Track, which fails.
        TrackDTO trackDTO = trackMapper.toDto(track);

        restTrackMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(trackDTO))
            )
            .andExpect(status().isBadRequest());

        List<Track> trackList = trackRepository.findAll();
        assertThat(trackList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(trackSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkAlbumArtistNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = trackRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(trackSearchRepository.findAll());
        // set the field null
        //   track.setAlbumArtistName(null);

        // Create the Track, which fails.
        TrackDTO trackDTO = trackMapper.toDto(track);

        restTrackMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(trackDTO))
            )
            .andExpect(status().isBadRequest());

        List<Track> trackList = trackRepository.findAll();
        assertThat(trackList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(trackSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkGenreNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = trackRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(trackSearchRepository.findAll());
        // set the field null
        //track.setGenreName(null);

        // Create the Track, which fails.
        TrackDTO trackDTO = trackMapper.toDto(track);

        restTrackMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(trackDTO))
            )
            .andExpect(status().isBadRequest());

        List<Track> trackList = trackRepository.findAll();
        assertThat(trackList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(trackSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkGenreSortNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = trackRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(trackSearchRepository.findAll());
        // set the field null
        //   track.setGenreSortName(null);

        // Create the Track, which fails.
        TrackDTO trackDTO = trackMapper.toDto(track);

        restTrackMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(trackDTO))
            )
            .andExpect(status().isBadRequest());

        List<Track> trackList = trackRepository.findAll();
        assertThat(trackList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(trackSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkCreatedOnIsRequired() throws Exception {
        int databaseSizeBeforeTest = trackRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(trackSearchRepository.findAll());
        // set the field null
        track.setCreatedOn(null);

        // Create the Track, which fails.
        TrackDTO trackDTO = trackMapper.toDto(track);

        restTrackMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(trackDTO))
            )
            .andExpect(status().isBadRequest());

        List<Track> trackList = trackRepository.findAll();
        assertThat(trackList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(trackSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllTracks() throws Exception {
        // Initialize the database
        trackRepository.saveAndFlush(track);

        // Get all the trackList
        restTrackMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(track.getId().intValue())))
            .andExpect(jsonPath("$.[*].filePath").value(hasItem(DEFAULT_FILE_PATH)))
            .andExpect(jsonPath("$.[*].subfolder").value(hasItem(DEFAULT_SUBFOLDER)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].sortName").value(hasItem(DEFAULT_SORT_NAME)))
            //            .andExpect(jsonPath("$.[*].artistName").value(hasItem(DEFAULT_ARTIST_NAME)))
            //            .andExpect(jsonPath("$.[*].artistSortName").value(hasItem(DEFAULT_ARTIST_SORT_NAME)))
            //            .andExpect(jsonPath("$.[*].albumName").value(hasItem(DEFAULT_ALBUM_NAME)))
            //            .andExpect(jsonPath("$.[*].albumSortName").value(hasItem(DEFAULT_ALBUM_SORT_NAME)))
            //            .andExpect(jsonPath("$.[*].albumArtistName").value(hasItem(DEFAULT_ALBUM_ARTIST_NAME)))
            //            .andExpect(jsonPath("$.[*].albumArtistSortName").value(hasItem(DEFAULT_ALBUM_ARTIST_SORT_NAME)))
            //            .andExpect(jsonPath("$.[*].albumReleasedYear").value(hasItem(DEFAULT_ALBUM_RELEASED_YEAR)))
            //            .andExpect(jsonPath("$.[*].genreName").value(hasItem(DEFAULT_GENRE_NAME)))
            //            .andExpect(jsonPath("$.[*].genreSortName").value(hasItem(DEFAULT_GENRE_SORT_NAME)))
            .andExpect(jsonPath("$.[*].trackNumber").value(hasItem(DEFAULT_TRACK_NUMBER)))
            .andExpect(jsonPath("$.[*].playbackLength").value(hasItem(DEFAULT_PLAYBACK_LENGTH)))
            .andExpect(jsonPath("$.[*].bitRate").value(hasItem(DEFAULT_BIT_RATE)))
            .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON.toString())))
            .andExpect(jsonPath("$.[*].tagVersion1").value(hasItem(DEFAULT_TAG_VERSION_1.booleanValue())))
            .andExpect(jsonPath("$.[*].tagVersion2").value(hasItem(DEFAULT_TAG_VERSION_2.booleanValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTracksWithEagerRelationshipsIsEnabled() throws Exception {
        when(trackServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTrackMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(trackServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTracksWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(trackServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTrackMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(trackRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getTrack() throws Exception {
        // Initialize the database
        trackRepository.saveAndFlush(track);

        // Get the track
        restTrackMockMvc
            .perform(get(ENTITY_API_URL_ID, track.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(track.getId().intValue()))
            .andExpect(jsonPath("$.filePath").value(DEFAULT_FILE_PATH))
            .andExpect(jsonPath("$.subfolder").value(DEFAULT_SUBFOLDER))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.sortName").value(DEFAULT_SORT_NAME))
            //            .andExpect(jsonPath("$.artistName").value(DEFAULT_ARTIST_NAME))
            //            .andExpect(jsonPath("$.artistSortName").value(DEFAULT_ARTIST_SORT_NAME))
            //            .andExpect(jsonPath("$.albumName").value(DEFAULT_ALBUM_NAME))
            //            .andExpect(jsonPath("$.albumSortName").value(DEFAULT_ALBUM_SORT_NAME))
            //            .andExpect(jsonPath("$.albumArtistName").value(DEFAULT_ALBUM_ARTIST_NAME))
            //            .andExpect(jsonPath("$.albumArtistSortName").value(DEFAULT_ALBUM_ARTIST_SORT_NAME))
            //            .andExpect(jsonPath("$.albumReleasedYear").value(DEFAULT_ALBUM_RELEASED_YEAR))
            //            .andExpect(jsonPath("$.genreName").value(DEFAULT_GENRE_NAME))
            //            .andExpect(jsonPath("$.genreSortName").value(DEFAULT_GENRE_SORT_NAME))
            .andExpect(jsonPath("$.trackNumber").value(DEFAULT_TRACK_NUMBER))
            .andExpect(jsonPath("$.playbackLength").value(DEFAULT_PLAYBACK_LENGTH))
            .andExpect(jsonPath("$.bitRate").value(DEFAULT_BIT_RATE))
            .andExpect(jsonPath("$.createdOn").value(DEFAULT_CREATED_ON.toString()))
            .andExpect(jsonPath("$.tagVersion1").value(DEFAULT_TAG_VERSION_1.booleanValue()))
            .andExpect(jsonPath("$.tagVersion2").value(DEFAULT_TAG_VERSION_2.booleanValue()));
    }

    @Test
    @Transactional
    void getTracksByIdFiltering() throws Exception {
        // Initialize the database
        trackRepository.saveAndFlush(track);

        Long id = track.getId();

        defaultTrackShouldBeFound("id.equals=" + id);
        defaultTrackShouldNotBeFound("id.notEquals=" + id);

        defaultTrackShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTrackShouldNotBeFound("id.greaterThan=" + id);

        defaultTrackShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTrackShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTracksByFilePathIsEqualToSomething() throws Exception {
        // Initialize the database
        trackRepository.saveAndFlush(track);

        // Get all the trackList where filePath equals to DEFAULT_FILE_PATH
        defaultTrackShouldBeFound("filePath.equals=" + DEFAULT_FILE_PATH);

        // Get all the trackList where filePath equals to UPDATED_FILE_PATH
        defaultTrackShouldNotBeFound("filePath.equals=" + UPDATED_FILE_PATH);
    }

    @Test
    @Transactional
    void getAllTracksByFilePathIsInShouldWork() throws Exception {
        // Initialize the database
        trackRepository.saveAndFlush(track);

        // Get all the trackList where filePath in DEFAULT_FILE_PATH or UPDATED_FILE_PATH
        defaultTrackShouldBeFound("filePath.in=" + DEFAULT_FILE_PATH + "," + UPDATED_FILE_PATH);

        // Get all the trackList where filePath equals to UPDATED_FILE_PATH
        defaultTrackShouldNotBeFound("filePath.in=" + UPDATED_FILE_PATH);
    }

    @Test
    @Transactional
    void getAllTracksByFilePathIsNullOrNotNull() throws Exception {
        // Initialize the database
        trackRepository.saveAndFlush(track);

        // Get all the trackList where filePath is not null
        defaultTrackShouldBeFound("filePath.specified=true");

        // Get all the trackList where filePath is null
        defaultTrackShouldNotBeFound("filePath.specified=false");
    }

    @Test
    @Transactional
    void getAllTracksByFilePathContainsSomething() throws Exception {
        // Initialize the database
        trackRepository.saveAndFlush(track);

        // Get all the trackList where filePath contains DEFAULT_FILE_PATH
        defaultTrackShouldBeFound("filePath.contains=" + DEFAULT_FILE_PATH);

        // Get all the trackList where filePath contains UPDATED_FILE_PATH
        defaultTrackShouldNotBeFound("filePath.contains=" + UPDATED_FILE_PATH);
    }

    @Test
    @Transactional
    void getAllTracksByFilePathNotContainsSomething() throws Exception {
        // Initialize the database
        trackRepository.saveAndFlush(track);

        // Get all the trackList where filePath does not contain DEFAULT_FILE_PATH
        defaultTrackShouldNotBeFound("filePath.doesNotContain=" + DEFAULT_FILE_PATH);

        // Get all the trackList where filePath does not contain UPDATED_FILE_PATH
        defaultTrackShouldBeFound("filePath.doesNotContain=" + UPDATED_FILE_PATH);
    }

    @Test
    @Transactional
    void getAllTracksBySubfolderIsEqualToSomething() throws Exception {
        // Initialize the database
        trackRepository.saveAndFlush(track);

        // Get all the trackList where subfolder equals to DEFAULT_SUBFOLDER
        defaultTrackShouldBeFound("subfolder.equals=" + DEFAULT_SUBFOLDER);

        // Get all the trackList where subfolder equals to UPDATED_SUBFOLDER
        defaultTrackShouldNotBeFound("subfolder.equals=" + UPDATED_SUBFOLDER);
    }

    @Test
    @Transactional
    void getAllTracksBySubfolderIsInShouldWork() throws Exception {
        // Initialize the database
        trackRepository.saveAndFlush(track);

        // Get all the trackList where subfolder in DEFAULT_SUBFOLDER or UPDATED_SUBFOLDER
        defaultTrackShouldBeFound("subfolder.in=" + DEFAULT_SUBFOLDER + "," + UPDATED_SUBFOLDER);

        // Get all the trackList where subfolder equals to UPDATED_SUBFOLDER
        defaultTrackShouldNotBeFound("subfolder.in=" + UPDATED_SUBFOLDER);
    }

    @Test
    @Transactional
    void getAllTracksBySubfolderIsNullOrNotNull() throws Exception {
        // Initialize the database
        trackRepository.saveAndFlush(track);

        // Get all the trackList where subfolder is not null
        defaultTrackShouldBeFound("subfolder.specified=true");

        // Get all the trackList where subfolder is null
        defaultTrackShouldNotBeFound("subfolder.specified=false");
    }

    @Test
    @Transactional
    void getAllTracksBySubfolderContainsSomething() throws Exception {
        // Initialize the database
        trackRepository.saveAndFlush(track);

        // Get all the trackList where subfolder contains DEFAULT_SUBFOLDER
        defaultTrackShouldBeFound("subfolder.contains=" + DEFAULT_SUBFOLDER);

        // Get all the trackList where subfolder contains UPDATED_SUBFOLDER
        defaultTrackShouldNotBeFound("subfolder.contains=" + UPDATED_SUBFOLDER);
    }

    @Test
    @Transactional
    void getAllTracksBySubfolderNotContainsSomething() throws Exception {
        // Initialize the database
        trackRepository.saveAndFlush(track);

        // Get all the trackList where subfolder does not contain DEFAULT_SUBFOLDER
        defaultTrackShouldNotBeFound("subfolder.doesNotContain=" + DEFAULT_SUBFOLDER);

        // Get all the trackList where subfolder does not contain UPDATED_SUBFOLDER
        defaultTrackShouldBeFound("subfolder.doesNotContain=" + UPDATED_SUBFOLDER);
    }

    @Test
    @Transactional
    void getAllTracksByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        trackRepository.saveAndFlush(track);

        // Get all the trackList where name equals to DEFAULT_NAME
        defaultTrackShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the trackList where name equals to UPDATED_NAME
        defaultTrackShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTracksByNameIsInShouldWork() throws Exception {
        // Initialize the database
        trackRepository.saveAndFlush(track);

        // Get all the trackList where name in DEFAULT_NAME or UPDATED_NAME
        defaultTrackShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the trackList where name equals to UPDATED_NAME
        defaultTrackShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTracksByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        trackRepository.saveAndFlush(track);

        // Get all the trackList where name is not null
        defaultTrackShouldBeFound("name.specified=true");

        // Get all the trackList where name is null
        defaultTrackShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllTracksByNameContainsSomething() throws Exception {
        // Initialize the database
        trackRepository.saveAndFlush(track);

        // Get all the trackList where name contains DEFAULT_NAME
        defaultTrackShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the trackList where name contains UPDATED_NAME
        defaultTrackShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTracksByNameNotContainsSomething() throws Exception {
        // Initialize the database
        trackRepository.saveAndFlush(track);

        // Get all the trackList where name does not contain DEFAULT_NAME
        defaultTrackShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the trackList where name does not contain UPDATED_NAME
        defaultTrackShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTracksBySortNameIsEqualToSomething() throws Exception {
        // Initialize the database
        trackRepository.saveAndFlush(track);

        // Get all the trackList where sortName equals to DEFAULT_SORT_NAME
        defaultTrackShouldBeFound("sortName.equals=" + DEFAULT_SORT_NAME);

        // Get all the trackList where sortName equals to UPDATED_SORT_NAME
        defaultTrackShouldNotBeFound("sortName.equals=" + UPDATED_SORT_NAME);
    }

    @Test
    @Transactional
    void getAllTracksBySortNameIsInShouldWork() throws Exception {
        // Initialize the database
        trackRepository.saveAndFlush(track);

        // Get all the trackList where sortName in DEFAULT_SORT_NAME or UPDATED_SORT_NAME
        defaultTrackShouldBeFound("sortName.in=" + DEFAULT_SORT_NAME + "," + UPDATED_SORT_NAME);

        // Get all the trackList where sortName equals to UPDATED_SORT_NAME
        defaultTrackShouldNotBeFound("sortName.in=" + UPDATED_SORT_NAME);
    }

    @Test
    @Transactional
    void getAllTracksBySortNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        trackRepository.saveAndFlush(track);

        // Get all the trackList where sortName is not null
        defaultTrackShouldBeFound("sortName.specified=true");

        // Get all the trackList where sortName is null
        defaultTrackShouldNotBeFound("sortName.specified=false");
    }

    @Test
    @Transactional
    void getAllTracksBySortNameContainsSomething() throws Exception {
        // Initialize the database
        trackRepository.saveAndFlush(track);

        // Get all the trackList where sortName contains DEFAULT_SORT_NAME
        defaultTrackShouldBeFound("sortName.contains=" + DEFAULT_SORT_NAME);

        // Get all the trackList where sortName contains UPDATED_SORT_NAME
        defaultTrackShouldNotBeFound("sortName.contains=" + UPDATED_SORT_NAME);
    }

    @Test
    @Transactional
    void getAllTracksBySortNameNotContainsSomething() throws Exception {
        // Initialize the database
        trackRepository.saveAndFlush(track);

        // Get all the trackList where sortName does not contain DEFAULT_SORT_NAME
        defaultTrackShouldNotBeFound("sortName.doesNotContain=" + DEFAULT_SORT_NAME);

        // Get all the trackList where sortName does not contain UPDATED_SORT_NAME
        defaultTrackShouldBeFound("sortName.doesNotContain=" + UPDATED_SORT_NAME);
    }

    @Test
    @Transactional
    void getAllTracksByArtistNameIsEqualToSomething() throws Exception {
        // Initialize the database
        trackRepository.saveAndFlush(track);

        // Get all the trackList where artistName equals to DEFAULT_ARTIST_NAME
        defaultTrackShouldBeFound("artistName.equals=" + DEFAULT_ARTIST_NAME);

        // Get all the trackList where artistName equals to UPDATED_ARTIST_NAME
        defaultTrackShouldNotBeFound("artistName.equals=" + UPDATED_ARTIST_NAME);
    }

    @Test
    @Transactional
    void getAllTracksByArtistNameIsInShouldWork() throws Exception {
        // Initialize the database
        trackRepository.saveAndFlush(track);

        // Get all the trackList where artistName in DEFAULT_ARTIST_NAME or UPDATED_ARTIST_NAME
        defaultTrackShouldBeFound("artistName.in=" + DEFAULT_ARTIST_NAME + "," + UPDATED_ARTIST_NAME);

        // Get all the trackList where artistName equals to UPDATED_ARTIST_NAME
        defaultTrackShouldNotBeFound("artistName.in=" + UPDATED_ARTIST_NAME);
    }

    @Test
    @Transactional
    void getAllTracksByArtistNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        trackRepository.saveAndFlush(track);

        // Get all the trackList where artistName is not null
        defaultTrackShouldBeFound("artistName.specified=true");

        // Get all the trackList where artistName is null
        defaultTrackShouldNotBeFound("artistName.specified=false");
    }

    @Test
    @Transactional
    void getAllTracksByArtistNameContainsSomething() throws Exception {
        // Initialize the database
        trackRepository.saveAndFlush(track);

        // Get all the trackList where artistName contains DEFAULT_ARTIST_NAME
        defaultTrackShouldBeFound("artistName.contains=" + DEFAULT_ARTIST_NAME);

        // Get all the trackList where artistName contains UPDATED_ARTIST_NAME
        defaultTrackShouldNotBeFound("artistName.contains=" + UPDATED_ARTIST_NAME);
    }

    @Test
    @Transactional
    void getAllTracksByArtistNameNotContainsSomething() throws Exception {
        // Initialize the database
        trackRepository.saveAndFlush(track);

        // Get all the trackList where artistName does not contain DEFAULT_ARTIST_NAME
        defaultTrackShouldNotBeFound("artistName.doesNotContain=" + DEFAULT_ARTIST_NAME);

        // Get all the trackList where artistName does not contain UPDATED_ARTIST_NAME
        defaultTrackShouldBeFound("artistName.doesNotContain=" + UPDATED_ARTIST_NAME);
    }

    @Test
    @Transactional
    void getAllTracksByArtistSortNameIsEqualToSomething() throws Exception {
        // Initialize the database
        trackRepository.saveAndFlush(track);

        // Get all the trackList where artistSortName equals to DEFAULT_ARTIST_SORT_NAME
        defaultTrackShouldBeFound("artistSortName.equals=" + DEFAULT_ARTIST_SORT_NAME);

        // Get all the trackList where artistSortName equals to UPDATED_ARTIST_SORT_NAME
        defaultTrackShouldNotBeFound("artistSortName.equals=" + UPDATED_ARTIST_SORT_NAME);
    }

    @Test
    @Transactional
    void getAllTracksByArtistSortNameIsInShouldWork() throws Exception {
        // Initialize the database
        trackRepository.saveAndFlush(track);

        // Get all the trackList where artistSortName in DEFAULT_ARTIST_SORT_NAME or UPDATED_ARTIST_SORT_NAME
        defaultTrackShouldBeFound("artistSortName.in=" + DEFAULT_ARTIST_SORT_NAME + "," + UPDATED_ARTIST_SORT_NAME);

        // Get all the trackList where artistSortName equals to UPDATED_ARTIST_SORT_NAME
        defaultTrackShouldNotBeFound("artistSortName.in=" + UPDATED_ARTIST_SORT_NAME);
    }

    @Test
    @Transactional
    void getAllTracksByArtistSortNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        trackRepository.saveAndFlush(track);

        // Get all the trackList where artistSortName is not null
        defaultTrackShouldBeFound("artistSortName.specified=true");

        // Get all the trackList where artistSortName is null
        defaultTrackShouldNotBeFound("artistSortName.specified=false");
    }

    @Test
    @Transactional
    void getAllTracksByArtistSortNameContainsSomething() throws Exception {
        // Initialize the database
        trackRepository.saveAndFlush(track);

        // Get all the trackList where artistSortName contains DEFAULT_ARTIST_SORT_NAME
        defaultTrackShouldBeFound("artistSortName.contains=" + DEFAULT_ARTIST_SORT_NAME);

        // Get all the trackList where artistSortName contains UPDATED_ARTIST_SORT_NAME
        defaultTrackShouldNotBeFound("artistSortName.contains=" + UPDATED_ARTIST_SORT_NAME);
    }

    @Test
    @Transactional
    void getAllTracksByArtistSortNameNotContainsSomething() throws Exception {
        // Initialize the database
        trackRepository.saveAndFlush(track);

        // Get all the trackList where artistSortName does not contain DEFAULT_ARTIST_SORT_NAME
        defaultTrackShouldNotBeFound("artistSortName.doesNotContain=" + DEFAULT_ARTIST_SORT_NAME);

        // Get all the trackList where artistSortName does not contain UPDATED_ARTIST_SORT_NAME
        defaultTrackShouldBeFound("artistSortName.doesNotContain=" + UPDATED_ARTIST_SORT_NAME);
    }

    @Test
    @Transactional
    void getAllTracksByAlbumNameIsEqualToSomething() throws Exception {
        // Initialize the database
        trackRepository.saveAndFlush(track);

        // Get all the trackList where albumName equals to DEFAULT_ALBUM_NAME
        defaultTrackShouldBeFound("albumName.equals=" + DEFAULT_ALBUM_NAME);

        // Get all the trackList where albumName equals to UPDATED_ALBUM_NAME
        defaultTrackShouldNotBeFound("albumName.equals=" + UPDATED_ALBUM_NAME);
    }

    @Test
    @Transactional
    void getAllTracksByAlbumNameIsInShouldWork() throws Exception {
        // Initialize the database
        trackRepository.saveAndFlush(track);

        // Get all the trackList where albumName in DEFAULT_ALBUM_NAME or UPDATED_ALBUM_NAME
        defaultTrackShouldBeFound("albumName.in=" + DEFAULT_ALBUM_NAME + "," + UPDATED_ALBUM_NAME);

        // Get all the trackList where albumName equals to UPDATED_ALBUM_NAME
        defaultTrackShouldNotBeFound("albumName.in=" + UPDATED_ALBUM_NAME);
    }

    @Test
    @Transactional
    void getAllTracksByAlbumNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        trackRepository.saveAndFlush(track);

        // Get all the trackList where albumName is not null
        defaultTrackShouldBeFound("albumName.specified=true");

        // Get all the trackList where albumName is null
        defaultTrackShouldNotBeFound("albumName.specified=false");
    }

    @Test
    @Transactional
    void getAllTracksByAlbumNameContainsSomething() throws Exception {
        // Initialize the database
        trackRepository.saveAndFlush(track);

        // Get all the trackList where albumName contains DEFAULT_ALBUM_NAME
        defaultTrackShouldBeFound("albumName.contains=" + DEFAULT_ALBUM_NAME);

        // Get all the trackList where albumName contains UPDATED_ALBUM_NAME
        defaultTrackShouldNotBeFound("albumName.contains=" + UPDATED_ALBUM_NAME);
    }

    @Test
    @Transactional
    void getAllTracksByAlbumNameNotContainsSomething() throws Exception {
        // Initialize the database
        trackRepository.saveAndFlush(track);

        // Get all the trackList where albumName does not contain DEFAULT_ALBUM_NAME
        defaultTrackShouldNotBeFound("albumName.doesNotContain=" + DEFAULT_ALBUM_NAME);

        // Get all the trackList where albumName does not contain UPDATED_ALBUM_NAME
        defaultTrackShouldBeFound("albumName.doesNotContain=" + UPDATED_ALBUM_NAME);
    }

    @Test
    @Transactional
    void getAllTracksByAlbumSortNameIsEqualToSomething() throws Exception {
        // Initialize the database
        trackRepository.saveAndFlush(track);

        // Get all the trackList where albumSortName equals to DEFAULT_ALBUM_SORT_NAME
        defaultTrackShouldBeFound("albumSortName.equals=" + DEFAULT_ALBUM_SORT_NAME);

        // Get all the trackList where albumSortName equals to UPDATED_ALBUM_SORT_NAME
        defaultTrackShouldNotBeFound("albumSortName.equals=" + UPDATED_ALBUM_SORT_NAME);
    }

    @Test
    @Transactional
    void getAllTracksByAlbumSortNameIsInShouldWork() throws Exception {
        // Initialize the database
        trackRepository.saveAndFlush(track);

        // Get all the trackList where albumSortName in DEFAULT_ALBUM_SORT_NAME or UPDATED_ALBUM_SORT_NAME
        defaultTrackShouldBeFound("albumSortName.in=" + DEFAULT_ALBUM_SORT_NAME + "," + UPDATED_ALBUM_SORT_NAME);

        // Get all the trackList where albumSortName equals to UPDATED_ALBUM_SORT_NAME
        defaultTrackShouldNotBeFound("albumSortName.in=" + UPDATED_ALBUM_SORT_NAME);
    }

    @Test
    @Transactional
    void getAllTracksByAlbumSortNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        trackRepository.saveAndFlush(track);

        // Get all the trackList where albumSortName is not null
        defaultTrackShouldBeFound("albumSortName.specified=true");

        // Get all the trackList where albumSortName is null
        defaultTrackShouldNotBeFound("albumSortName.specified=false");
    }

    @Test
    @Transactional
    void getAllTracksByAlbumSortNameContainsSomething() throws Exception {
        // Initialize the database
        trackRepository.saveAndFlush(track);

        // Get all the trackList where albumSortName contains DEFAULT_ALBUM_SORT_NAME
        defaultTrackShouldBeFound("albumSortName.contains=" + DEFAULT_ALBUM_SORT_NAME);

        // Get all the trackList where albumSortName contains UPDATED_ALBUM_SORT_NAME
        defaultTrackShouldNotBeFound("albumSortName.contains=" + UPDATED_ALBUM_SORT_NAME);
    }

    @Test
    @Transactional
    void getAllTracksByAlbumSortNameNotContainsSomething() throws Exception {
        // Initialize the database
        trackRepository.saveAndFlush(track);

        // Get all the trackList where albumSortName does not contain DEFAULT_ALBUM_SORT_NAME
        defaultTrackShouldNotBeFound("albumSortName.doesNotContain=" + DEFAULT_ALBUM_SORT_NAME);

        // Get all the trackList where albumSortName does not contain UPDATED_ALBUM_SORT_NAME
        defaultTrackShouldBeFound("albumSortName.doesNotContain=" + UPDATED_ALBUM_SORT_NAME);
    }

    @Test
    @Transactional
    void getAllTracksByAlbumArtistNameIsEqualToSomething() throws Exception {
        // Initialize the database
        trackRepository.saveAndFlush(track);

        // Get all the trackList where albumArtistName equals to DEFAULT_ALBUM_ARTIST_NAME
        defaultTrackShouldBeFound("albumArtistName.equals=" + DEFAULT_ALBUM_ARTIST_NAME);

        // Get all the trackList where albumArtistName equals to UPDATED_ALBUM_ARTIST_NAME
        defaultTrackShouldNotBeFound("albumArtistName.equals=" + UPDATED_ALBUM_ARTIST_NAME);
    }

    @Test
    @Transactional
    void getAllTracksByAlbumArtistNameIsInShouldWork() throws Exception {
        // Initialize the database
        trackRepository.saveAndFlush(track);

        // Get all the trackList where albumArtistName in DEFAULT_ALBUM_ARTIST_NAME or UPDATED_ALBUM_ARTIST_NAME
        defaultTrackShouldBeFound("albumArtistName.in=" + DEFAULT_ALBUM_ARTIST_NAME + "," + UPDATED_ALBUM_ARTIST_NAME);

        // Get all the trackList where albumArtistName equals to UPDATED_ALBUM_ARTIST_NAME
        defaultTrackShouldNotBeFound("albumArtistName.in=" + UPDATED_ALBUM_ARTIST_NAME);
    }

    @Test
    @Transactional
    void getAllTracksByAlbumArtistNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        trackRepository.saveAndFlush(track);

        // Get all the trackList where albumArtistName is not null
        defaultTrackShouldBeFound("albumArtistName.specified=true");

        // Get all the trackList where albumArtistName is null
        defaultTrackShouldNotBeFound("albumArtistName.specified=false");
    }

    @Test
    @Transactional
    void getAllTracksByAlbumArtistNameContainsSomething() throws Exception {
        // Initialize the database
        trackRepository.saveAndFlush(track);

        // Get all the trackList where albumArtistName contains DEFAULT_ALBUM_ARTIST_NAME
        defaultTrackShouldBeFound("albumArtistName.contains=" + DEFAULT_ALBUM_ARTIST_NAME);

        // Get all the trackList where albumArtistName contains UPDATED_ALBUM_ARTIST_NAME
        defaultTrackShouldNotBeFound("albumArtistName.contains=" + UPDATED_ALBUM_ARTIST_NAME);
    }

    @Test
    @Transactional
    void getAllTracksByAlbumArtistNameNotContainsSomething() throws Exception {
        // Initialize the database
        trackRepository.saveAndFlush(track);

        // Get all the trackList where albumArtistName does not contain DEFAULT_ALBUM_ARTIST_NAME
        defaultTrackShouldNotBeFound("albumArtistName.doesNotContain=" + DEFAULT_ALBUM_ARTIST_NAME);

        // Get all the trackList where albumArtistName does not contain UPDATED_ALBUM_ARTIST_NAME
        defaultTrackShouldBeFound("albumArtistName.doesNotContain=" + UPDATED_ALBUM_ARTIST_NAME);
    }

    @Test
    @Transactional
    void getAllTracksByAlbumArtistSortNameIsEqualToSomething() throws Exception {
        // Initialize the database
        trackRepository.saveAndFlush(track);

        // Get all the trackList where albumArtistSortName equals to DEFAULT_ALBUM_ARTIST_SORT_NAME
        defaultTrackShouldBeFound("albumArtistSortName.equals=" + DEFAULT_ALBUM_ARTIST_SORT_NAME);

        // Get all the trackList where albumArtistSortName equals to UPDATED_ALBUM_ARTIST_SORT_NAME
        defaultTrackShouldNotBeFound("albumArtistSortName.equals=" + UPDATED_ALBUM_ARTIST_SORT_NAME);
    }

    @Test
    @Transactional
    void getAllTracksByAlbumArtistSortNameIsInShouldWork() throws Exception {
        // Initialize the database
        trackRepository.saveAndFlush(track);

        // Get all the trackList where albumArtistSortName in DEFAULT_ALBUM_ARTIST_SORT_NAME or UPDATED_ALBUM_ARTIST_SORT_NAME
        defaultTrackShouldBeFound("albumArtistSortName.in=" + DEFAULT_ALBUM_ARTIST_SORT_NAME + "," + UPDATED_ALBUM_ARTIST_SORT_NAME);

        // Get all the trackList where albumArtistSortName equals to UPDATED_ALBUM_ARTIST_SORT_NAME
        defaultTrackShouldNotBeFound("albumArtistSortName.in=" + UPDATED_ALBUM_ARTIST_SORT_NAME);
    }

    @Test
    @Transactional
    void getAllTracksByAlbumArtistSortNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        trackRepository.saveAndFlush(track);

        // Get all the trackList where albumArtistSortName is not null
        defaultTrackShouldBeFound("albumArtistSortName.specified=true");

        // Get all the trackList where albumArtistSortName is null
        defaultTrackShouldNotBeFound("albumArtistSortName.specified=false");
    }

    @Test
    @Transactional
    void getAllTracksByAlbumArtistSortNameContainsSomething() throws Exception {
        // Initialize the database
        trackRepository.saveAndFlush(track);

        // Get all the trackList where albumArtistSortName contains DEFAULT_ALBUM_ARTIST_SORT_NAME
        defaultTrackShouldBeFound("albumArtistSortName.contains=" + DEFAULT_ALBUM_ARTIST_SORT_NAME);

        // Get all the trackList where albumArtistSortName contains UPDATED_ALBUM_ARTIST_SORT_NAME
        defaultTrackShouldNotBeFound("albumArtistSortName.contains=" + UPDATED_ALBUM_ARTIST_SORT_NAME);
    }

    @Test
    @Transactional
    void getAllTracksByAlbumArtistSortNameNotContainsSomething() throws Exception {
        // Initialize the database
        trackRepository.saveAndFlush(track);

        // Get all the trackList where albumArtistSortName does not contain DEFAULT_ALBUM_ARTIST_SORT_NAME
        defaultTrackShouldNotBeFound("albumArtistSortName.doesNotContain=" + DEFAULT_ALBUM_ARTIST_SORT_NAME);

        // Get all the trackList where albumArtistSortName does not contain UPDATED_ALBUM_ARTIST_SORT_NAME
        defaultTrackShouldBeFound("albumArtistSortName.doesNotContain=" + UPDATED_ALBUM_ARTIST_SORT_NAME);
    }

    @Test
    @Transactional
    void getAllTracksByAlbumReleasedYearIsEqualToSomething() throws Exception {
        // Initialize the database
        trackRepository.saveAndFlush(track);

        // Get all the trackList where albumReleasedYear equals to DEFAULT_ALBUM_RELEASED_YEAR
        defaultTrackShouldBeFound("albumReleasedYear.equals=" + DEFAULT_ALBUM_RELEASED_YEAR);

        // Get all the trackList where albumReleasedYear equals to UPDATED_ALBUM_RELEASED_YEAR
        defaultTrackShouldNotBeFound("albumReleasedYear.equals=" + UPDATED_ALBUM_RELEASED_YEAR);
    }

    @Test
    @Transactional
    void getAllTracksByAlbumReleasedYearIsInShouldWork() throws Exception {
        // Initialize the database
        trackRepository.saveAndFlush(track);

        // Get all the trackList where albumReleasedYear in DEFAULT_ALBUM_RELEASED_YEAR or UPDATED_ALBUM_RELEASED_YEAR
        defaultTrackShouldBeFound("albumReleasedYear.in=" + DEFAULT_ALBUM_RELEASED_YEAR + "," + UPDATED_ALBUM_RELEASED_YEAR);

        // Get all the trackList where albumReleasedYear equals to UPDATED_ALBUM_RELEASED_YEAR
        defaultTrackShouldNotBeFound("albumReleasedYear.in=" + UPDATED_ALBUM_RELEASED_YEAR);
    }

    @Test
    @Transactional
    void getAllTracksByAlbumReleasedYearIsNullOrNotNull() throws Exception {
        // Initialize the database
        trackRepository.saveAndFlush(track);

        // Get all the trackList where albumReleasedYear is not null
        defaultTrackShouldBeFound("albumReleasedYear.specified=true");

        // Get all the trackList where albumReleasedYear is null
        defaultTrackShouldNotBeFound("albumReleasedYear.specified=false");
    }

    @Test
    @Transactional
    void getAllTracksByAlbumReleasedYearIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        trackRepository.saveAndFlush(track);

        // Get all the trackList where albumReleasedYear is greater than or equal to DEFAULT_ALBUM_RELEASED_YEAR
        defaultTrackShouldBeFound("albumReleasedYear.greaterThanOrEqual=" + DEFAULT_ALBUM_RELEASED_YEAR);

        // Get all the trackList where albumReleasedYear is greater than or equal to UPDATED_ALBUM_RELEASED_YEAR
        defaultTrackShouldNotBeFound("albumReleasedYear.greaterThanOrEqual=" + UPDATED_ALBUM_RELEASED_YEAR);
    }

    @Test
    @Transactional
    void getAllTracksByAlbumReleasedYearIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        trackRepository.saveAndFlush(track);

        // Get all the trackList where albumReleasedYear is less than or equal to DEFAULT_ALBUM_RELEASED_YEAR
        defaultTrackShouldBeFound("albumReleasedYear.lessThanOrEqual=" + DEFAULT_ALBUM_RELEASED_YEAR);

        // Get all the trackList where albumReleasedYear is less than or equal to SMALLER_ALBUM_RELEASED_YEAR
        defaultTrackShouldNotBeFound("albumReleasedYear.lessThanOrEqual=" + SMALLER_ALBUM_RELEASED_YEAR);
    }

    @Test
    @Transactional
    void getAllTracksByAlbumReleasedYearIsLessThanSomething() throws Exception {
        // Initialize the database
        trackRepository.saveAndFlush(track);

        // Get all the trackList where albumReleasedYear is less than DEFAULT_ALBUM_RELEASED_YEAR
        defaultTrackShouldNotBeFound("albumReleasedYear.lessThan=" + DEFAULT_ALBUM_RELEASED_YEAR);

        // Get all the trackList where albumReleasedYear is less than UPDATED_ALBUM_RELEASED_YEAR
        defaultTrackShouldBeFound("albumReleasedYear.lessThan=" + UPDATED_ALBUM_RELEASED_YEAR);
    }

    @Test
    @Transactional
    void getAllTracksByAlbumReleasedYearIsGreaterThanSomething() throws Exception {
        // Initialize the database
        trackRepository.saveAndFlush(track);

        // Get all the trackList where albumReleasedYear is greater than DEFAULT_ALBUM_RELEASED_YEAR
        defaultTrackShouldNotBeFound("albumReleasedYear.greaterThan=" + DEFAULT_ALBUM_RELEASED_YEAR);

        // Get all the trackList where albumReleasedYear is greater than SMALLER_ALBUM_RELEASED_YEAR
        defaultTrackShouldBeFound("albumReleasedYear.greaterThan=" + SMALLER_ALBUM_RELEASED_YEAR);
    }

    @Test
    @Transactional
    void getAllTracksByGenreNameIsEqualToSomething() throws Exception {
        // Initialize the database
        trackRepository.saveAndFlush(track);

        // Get all the trackList where genreName equals to DEFAULT_GENRE_NAME
        defaultTrackShouldBeFound("genreName.equals=" + DEFAULT_GENRE_NAME);

        // Get all the trackList where genreName equals to UPDATED_GENRE_NAME
        defaultTrackShouldNotBeFound("genreName.equals=" + UPDATED_GENRE_NAME);
    }

    @Test
    @Transactional
    void getAllTracksByGenreNameIsInShouldWork() throws Exception {
        // Initialize the database
        trackRepository.saveAndFlush(track);

        // Get all the trackList where genreName in DEFAULT_GENRE_NAME or UPDATED_GENRE_NAME
        defaultTrackShouldBeFound("genreName.in=" + DEFAULT_GENRE_NAME + "," + UPDATED_GENRE_NAME);

        // Get all the trackList where genreName equals to UPDATED_GENRE_NAME
        defaultTrackShouldNotBeFound("genreName.in=" + UPDATED_GENRE_NAME);
    }

    @Test
    @Transactional
    void getAllTracksByGenreNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        trackRepository.saveAndFlush(track);

        // Get all the trackList where genreName is not null
        defaultTrackShouldBeFound("genreName.specified=true");

        // Get all the trackList where genreName is null
        defaultTrackShouldNotBeFound("genreName.specified=false");
    }

    @Test
    @Transactional
    void getAllTracksByGenreNameContainsSomething() throws Exception {
        // Initialize the database
        trackRepository.saveAndFlush(track);

        // Get all the trackList where genreName contains DEFAULT_GENRE_NAME
        defaultTrackShouldBeFound("genreName.contains=" + DEFAULT_GENRE_NAME);

        // Get all the trackList where genreName contains UPDATED_GENRE_NAME
        defaultTrackShouldNotBeFound("genreName.contains=" + UPDATED_GENRE_NAME);
    }

    @Test
    @Transactional
    void getAllTracksByGenreNameNotContainsSomething() throws Exception {
        // Initialize the database
        trackRepository.saveAndFlush(track);

        // Get all the trackList where genreName does not contain DEFAULT_GENRE_NAME
        defaultTrackShouldNotBeFound("genreName.doesNotContain=" + DEFAULT_GENRE_NAME);

        // Get all the trackList where genreName does not contain UPDATED_GENRE_NAME
        defaultTrackShouldBeFound("genreName.doesNotContain=" + UPDATED_GENRE_NAME);
    }

    @Test
    @Transactional
    void getAllTracksByGenreSortNameIsEqualToSomething() throws Exception {
        // Initialize the database
        trackRepository.saveAndFlush(track);

        // Get all the trackList where genreSortName equals to DEFAULT_GENRE_SORT_NAME
        defaultTrackShouldBeFound("genreSortName.equals=" + DEFAULT_GENRE_SORT_NAME);

        // Get all the trackList where genreSortName equals to UPDATED_GENRE_SORT_NAME
        defaultTrackShouldNotBeFound("genreSortName.equals=" + UPDATED_GENRE_SORT_NAME);
    }

    @Test
    @Transactional
    void getAllTracksByGenreSortNameIsInShouldWork() throws Exception {
        // Initialize the database
        trackRepository.saveAndFlush(track);

        // Get all the trackList where genreSortName in DEFAULT_GENRE_SORT_NAME or UPDATED_GENRE_SORT_NAME
        defaultTrackShouldBeFound("genreSortName.in=" + DEFAULT_GENRE_SORT_NAME + "," + UPDATED_GENRE_SORT_NAME);

        // Get all the trackList where genreSortName equals to UPDATED_GENRE_SORT_NAME
        defaultTrackShouldNotBeFound("genreSortName.in=" + UPDATED_GENRE_SORT_NAME);
    }

    @Test
    @Transactional
    void getAllTracksByGenreSortNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        trackRepository.saveAndFlush(track);

        // Get all the trackList where genreSortName is not null
        defaultTrackShouldBeFound("genreSortName.specified=true");

        // Get all the trackList where genreSortName is null
        defaultTrackShouldNotBeFound("genreSortName.specified=false");
    }

    @Test
    @Transactional
    void getAllTracksByGenreSortNameContainsSomething() throws Exception {
        // Initialize the database
        trackRepository.saveAndFlush(track);

        // Get all the trackList where genreSortName contains DEFAULT_GENRE_SORT_NAME
        defaultTrackShouldBeFound("genreSortName.contains=" + DEFAULT_GENRE_SORT_NAME);

        // Get all the trackList where genreSortName contains UPDATED_GENRE_SORT_NAME
        defaultTrackShouldNotBeFound("genreSortName.contains=" + UPDATED_GENRE_SORT_NAME);
    }

    @Test
    @Transactional
    void getAllTracksByGenreSortNameNotContainsSomething() throws Exception {
        // Initialize the database
        trackRepository.saveAndFlush(track);

        // Get all the trackList where genreSortName does not contain DEFAULT_GENRE_SORT_NAME
        defaultTrackShouldNotBeFound("genreSortName.doesNotContain=" + DEFAULT_GENRE_SORT_NAME);

        // Get all the trackList where genreSortName does not contain UPDATED_GENRE_SORT_NAME
        defaultTrackShouldBeFound("genreSortName.doesNotContain=" + UPDATED_GENRE_SORT_NAME);
    }

    @Test
    @Transactional
    void getAllTracksByTrackNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        trackRepository.saveAndFlush(track);

        // Get all the trackList where trackNumber equals to DEFAULT_TRACK_NUMBER
        defaultTrackShouldBeFound("trackNumber.equals=" + DEFAULT_TRACK_NUMBER);

        // Get all the trackList where trackNumber equals to UPDATED_TRACK_NUMBER
        defaultTrackShouldNotBeFound("trackNumber.equals=" + UPDATED_TRACK_NUMBER);
    }

    @Test
    @Transactional
    void getAllTracksByTrackNumberIsInShouldWork() throws Exception {
        // Initialize the database
        trackRepository.saveAndFlush(track);

        // Get all the trackList where trackNumber in DEFAULT_TRACK_NUMBER or UPDATED_TRACK_NUMBER
        defaultTrackShouldBeFound("trackNumber.in=" + DEFAULT_TRACK_NUMBER + "," + UPDATED_TRACK_NUMBER);

        // Get all the trackList where trackNumber equals to UPDATED_TRACK_NUMBER
        defaultTrackShouldNotBeFound("trackNumber.in=" + UPDATED_TRACK_NUMBER);
    }

    @Test
    @Transactional
    void getAllTracksByTrackNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        trackRepository.saveAndFlush(track);

        // Get all the trackList where trackNumber is not null
        defaultTrackShouldBeFound("trackNumber.specified=true");

        // Get all the trackList where trackNumber is null
        defaultTrackShouldNotBeFound("trackNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllTracksByTrackNumberIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        trackRepository.saveAndFlush(track);

        // Get all the trackList where trackNumber is greater than or equal to DEFAULT_TRACK_NUMBER
        defaultTrackShouldBeFound("trackNumber.greaterThanOrEqual=" + DEFAULT_TRACK_NUMBER);

        // Get all the trackList where trackNumber is greater than or equal to UPDATED_TRACK_NUMBER
        defaultTrackShouldNotBeFound("trackNumber.greaterThanOrEqual=" + UPDATED_TRACK_NUMBER);
    }

    @Test
    @Transactional
    void getAllTracksByTrackNumberIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        trackRepository.saveAndFlush(track);

        // Get all the trackList where trackNumber is less than or equal to DEFAULT_TRACK_NUMBER
        defaultTrackShouldBeFound("trackNumber.lessThanOrEqual=" + DEFAULT_TRACK_NUMBER);

        // Get all the trackList where trackNumber is less than or equal to SMALLER_TRACK_NUMBER
        defaultTrackShouldNotBeFound("trackNumber.lessThanOrEqual=" + SMALLER_TRACK_NUMBER);
    }

    @Test
    @Transactional
    void getAllTracksByTrackNumberIsLessThanSomething() throws Exception {
        // Initialize the database
        trackRepository.saveAndFlush(track);

        // Get all the trackList where trackNumber is less than DEFAULT_TRACK_NUMBER
        defaultTrackShouldNotBeFound("trackNumber.lessThan=" + DEFAULT_TRACK_NUMBER);

        // Get all the trackList where trackNumber is less than UPDATED_TRACK_NUMBER
        defaultTrackShouldBeFound("trackNumber.lessThan=" + UPDATED_TRACK_NUMBER);
    }

    @Test
    @Transactional
    void getAllTracksByTrackNumberIsGreaterThanSomething() throws Exception {
        // Initialize the database
        trackRepository.saveAndFlush(track);

        // Get all the trackList where trackNumber is greater than DEFAULT_TRACK_NUMBER
        defaultTrackShouldNotBeFound("trackNumber.greaterThan=" + DEFAULT_TRACK_NUMBER);

        // Get all the trackList where trackNumber is greater than SMALLER_TRACK_NUMBER
        defaultTrackShouldBeFound("trackNumber.greaterThan=" + SMALLER_TRACK_NUMBER);
    }

    @Test
    @Transactional
    void getAllTracksByPlaybackLengthIsEqualToSomething() throws Exception {
        // Initialize the database
        trackRepository.saveAndFlush(track);

        // Get all the trackList where playbackLength equals to DEFAULT_PLAYBACK_LENGTH
        defaultTrackShouldBeFound("playbackLength.equals=" + DEFAULT_PLAYBACK_LENGTH);

        // Get all the trackList where playbackLength equals to UPDATED_PLAYBACK_LENGTH
        defaultTrackShouldNotBeFound("playbackLength.equals=" + UPDATED_PLAYBACK_LENGTH);
    }

    @Test
    @Transactional
    void getAllTracksByPlaybackLengthIsInShouldWork() throws Exception {
        // Initialize the database
        trackRepository.saveAndFlush(track);

        // Get all the trackList where playbackLength in DEFAULT_PLAYBACK_LENGTH or UPDATED_PLAYBACK_LENGTH
        defaultTrackShouldBeFound("playbackLength.in=" + DEFAULT_PLAYBACK_LENGTH + "," + UPDATED_PLAYBACK_LENGTH);

        // Get all the trackList where playbackLength equals to UPDATED_PLAYBACK_LENGTH
        defaultTrackShouldNotBeFound("playbackLength.in=" + UPDATED_PLAYBACK_LENGTH);
    }

    @Test
    @Transactional
    void getAllTracksByPlaybackLengthIsNullOrNotNull() throws Exception {
        // Initialize the database
        trackRepository.saveAndFlush(track);

        // Get all the trackList where playbackLength is not null
        defaultTrackShouldBeFound("playbackLength.specified=true");

        // Get all the trackList where playbackLength is null
        defaultTrackShouldNotBeFound("playbackLength.specified=false");
    }

    @Test
    @Transactional
    void getAllTracksByPlaybackLengthIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        trackRepository.saveAndFlush(track);

        // Get all the trackList where playbackLength is greater than or equal to DEFAULT_PLAYBACK_LENGTH
        defaultTrackShouldBeFound("playbackLength.greaterThanOrEqual=" + DEFAULT_PLAYBACK_LENGTH);

        // Get all the trackList where playbackLength is greater than or equal to UPDATED_PLAYBACK_LENGTH
        defaultTrackShouldNotBeFound("playbackLength.greaterThanOrEqual=" + UPDATED_PLAYBACK_LENGTH);
    }

    @Test
    @Transactional
    void getAllTracksByPlaybackLengthIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        trackRepository.saveAndFlush(track);

        // Get all the trackList where playbackLength is less than or equal to DEFAULT_PLAYBACK_LENGTH
        defaultTrackShouldBeFound("playbackLength.lessThanOrEqual=" + DEFAULT_PLAYBACK_LENGTH);

        // Get all the trackList where playbackLength is less than or equal to SMALLER_PLAYBACK_LENGTH
        defaultTrackShouldNotBeFound("playbackLength.lessThanOrEqual=" + SMALLER_PLAYBACK_LENGTH);
    }

    @Test
    @Transactional
    void getAllTracksByPlaybackLengthIsLessThanSomething() throws Exception {
        // Initialize the database
        trackRepository.saveAndFlush(track);

        // Get all the trackList where playbackLength is less than DEFAULT_PLAYBACK_LENGTH
        defaultTrackShouldNotBeFound("playbackLength.lessThan=" + DEFAULT_PLAYBACK_LENGTH);

        // Get all the trackList where playbackLength is less than UPDATED_PLAYBACK_LENGTH
        defaultTrackShouldBeFound("playbackLength.lessThan=" + UPDATED_PLAYBACK_LENGTH);
    }

    @Test
    @Transactional
    void getAllTracksByPlaybackLengthIsGreaterThanSomething() throws Exception {
        // Initialize the database
        trackRepository.saveAndFlush(track);

        // Get all the trackList where playbackLength is greater than DEFAULT_PLAYBACK_LENGTH
        defaultTrackShouldNotBeFound("playbackLength.greaterThan=" + DEFAULT_PLAYBACK_LENGTH);

        // Get all the trackList where playbackLength is greater than SMALLER_PLAYBACK_LENGTH
        defaultTrackShouldBeFound("playbackLength.greaterThan=" + SMALLER_PLAYBACK_LENGTH);
    }

    @Test
    @Transactional
    void getAllTracksByBitRateIsEqualToSomething() throws Exception {
        // Initialize the database
        trackRepository.saveAndFlush(track);

        // Get all the trackList where bitRate equals to DEFAULT_BIT_RATE
        defaultTrackShouldBeFound("bitRate.equals=" + DEFAULT_BIT_RATE);

        // Get all the trackList where bitRate equals to UPDATED_BIT_RATE
        defaultTrackShouldNotBeFound("bitRate.equals=" + UPDATED_BIT_RATE);
    }

    @Test
    @Transactional
    void getAllTracksByBitRateIsInShouldWork() throws Exception {
        // Initialize the database
        trackRepository.saveAndFlush(track);

        // Get all the trackList where bitRate in DEFAULT_BIT_RATE or UPDATED_BIT_RATE
        defaultTrackShouldBeFound("bitRate.in=" + DEFAULT_BIT_RATE + "," + UPDATED_BIT_RATE);

        // Get all the trackList where bitRate equals to UPDATED_BIT_RATE
        defaultTrackShouldNotBeFound("bitRate.in=" + UPDATED_BIT_RATE);
    }

    @Test
    @Transactional
    void getAllTracksByBitRateIsNullOrNotNull() throws Exception {
        // Initialize the database
        trackRepository.saveAndFlush(track);

        // Get all the trackList where bitRate is not null
        defaultTrackShouldBeFound("bitRate.specified=true");

        // Get all the trackList where bitRate is null
        defaultTrackShouldNotBeFound("bitRate.specified=false");
    }

    @Test
    @Transactional
    void getAllTracksByBitRateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        trackRepository.saveAndFlush(track);

        // Get all the trackList where bitRate is greater than or equal to DEFAULT_BIT_RATE
        defaultTrackShouldBeFound("bitRate.greaterThanOrEqual=" + DEFAULT_BIT_RATE);

        // Get all the trackList where bitRate is greater than or equal to UPDATED_BIT_RATE
        defaultTrackShouldNotBeFound("bitRate.greaterThanOrEqual=" + UPDATED_BIT_RATE);
    }

    @Test
    @Transactional
    void getAllTracksByBitRateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        trackRepository.saveAndFlush(track);

        // Get all the trackList where bitRate is less than or equal to DEFAULT_BIT_RATE
        defaultTrackShouldBeFound("bitRate.lessThanOrEqual=" + DEFAULT_BIT_RATE);

        // Get all the trackList where bitRate is less than or equal to SMALLER_BIT_RATE
        defaultTrackShouldNotBeFound("bitRate.lessThanOrEqual=" + SMALLER_BIT_RATE);
    }

    @Test
    @Transactional
    void getAllTracksByBitRateIsLessThanSomething() throws Exception {
        // Initialize the database
        trackRepository.saveAndFlush(track);

        // Get all the trackList where bitRate is less than DEFAULT_BIT_RATE
        defaultTrackShouldNotBeFound("bitRate.lessThan=" + DEFAULT_BIT_RATE);

        // Get all the trackList where bitRate is less than UPDATED_BIT_RATE
        defaultTrackShouldBeFound("bitRate.lessThan=" + UPDATED_BIT_RATE);
    }

    @Test
    @Transactional
    void getAllTracksByBitRateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        trackRepository.saveAndFlush(track);

        // Get all the trackList where bitRate is greater than DEFAULT_BIT_RATE
        defaultTrackShouldNotBeFound("bitRate.greaterThan=" + DEFAULT_BIT_RATE);

        // Get all the trackList where bitRate is greater than SMALLER_BIT_RATE
        defaultTrackShouldBeFound("bitRate.greaterThan=" + SMALLER_BIT_RATE);
    }

    @Test
    @Transactional
    void getAllTracksByCreatedOnIsEqualToSomething() throws Exception {
        // Initialize the database
        trackRepository.saveAndFlush(track);

        // Get all the trackList where createdOn equals to DEFAULT_CREATED_ON
        defaultTrackShouldBeFound("createdOn.equals=" + DEFAULT_CREATED_ON);

        // Get all the trackList where createdOn equals to UPDATED_CREATED_ON
        defaultTrackShouldNotBeFound("createdOn.equals=" + UPDATED_CREATED_ON);
    }

    @Test
    @Transactional
    void getAllTracksByCreatedOnIsInShouldWork() throws Exception {
        // Initialize the database
        trackRepository.saveAndFlush(track);

        // Get all the trackList where createdOn in DEFAULT_CREATED_ON or UPDATED_CREATED_ON
        defaultTrackShouldBeFound("createdOn.in=" + DEFAULT_CREATED_ON + "," + UPDATED_CREATED_ON);

        // Get all the trackList where createdOn equals to UPDATED_CREATED_ON
        defaultTrackShouldNotBeFound("createdOn.in=" + UPDATED_CREATED_ON);
    }

    @Test
    @Transactional
    void getAllTracksByCreatedOnIsNullOrNotNull() throws Exception {
        // Initialize the database
        trackRepository.saveAndFlush(track);

        // Get all the trackList where createdOn is not null
        defaultTrackShouldBeFound("createdOn.specified=true");

        // Get all the trackList where createdOn is null
        defaultTrackShouldNotBeFound("createdOn.specified=false");
    }

    @Test
    @Transactional
    void getAllTracksByTagVersion1IsEqualToSomething() throws Exception {
        // Initialize the database
        trackRepository.saveAndFlush(track);

        // Get all the trackList where tagVersion1 equals to DEFAULT_TAG_VERSION_1
        defaultTrackShouldBeFound("tagVersion1.equals=" + DEFAULT_TAG_VERSION_1);

        // Get all the trackList where tagVersion1 equals to UPDATED_TAG_VERSION_1
        defaultTrackShouldNotBeFound("tagVersion1.equals=" + UPDATED_TAG_VERSION_1);
    }

    @Test
    @Transactional
    void getAllTracksByTagVersion1IsInShouldWork() throws Exception {
        // Initialize the database
        trackRepository.saveAndFlush(track);

        // Get all the trackList where tagVersion1 in DEFAULT_TAG_VERSION_1 or UPDATED_TAG_VERSION_1
        defaultTrackShouldBeFound("tagVersion1.in=" + DEFAULT_TAG_VERSION_1 + "," + UPDATED_TAG_VERSION_1);

        // Get all the trackList where tagVersion1 equals to UPDATED_TAG_VERSION_1
        defaultTrackShouldNotBeFound("tagVersion1.in=" + UPDATED_TAG_VERSION_1);
    }

    @Test
    @Transactional
    void getAllTracksByTagVersion1IsNullOrNotNull() throws Exception {
        // Initialize the database
        trackRepository.saveAndFlush(track);

        // Get all the trackList where tagVersion1 is not null
        defaultTrackShouldBeFound("tagVersion1.specified=true");

        // Get all the trackList where tagVersion1 is null
        defaultTrackShouldNotBeFound("tagVersion1.specified=false");
    }

    @Test
    @Transactional
    void getAllTracksByTagVersion2IsEqualToSomething() throws Exception {
        // Initialize the database
        trackRepository.saveAndFlush(track);

        // Get all the trackList where tagVersion2 equals to DEFAULT_TAG_VERSION_2
        defaultTrackShouldBeFound("tagVersion2.equals=" + DEFAULT_TAG_VERSION_2);

        // Get all the trackList where tagVersion2 equals to UPDATED_TAG_VERSION_2
        defaultTrackShouldNotBeFound("tagVersion2.equals=" + UPDATED_TAG_VERSION_2);
    }

    @Test
    @Transactional
    void getAllTracksByTagVersion2IsInShouldWork() throws Exception {
        // Initialize the database
        trackRepository.saveAndFlush(track);

        // Get all the trackList where tagVersion2 in DEFAULT_TAG_VERSION_2 or UPDATED_TAG_VERSION_2
        defaultTrackShouldBeFound("tagVersion2.in=" + DEFAULT_TAG_VERSION_2 + "," + UPDATED_TAG_VERSION_2);

        // Get all the trackList where tagVersion2 equals to UPDATED_TAG_VERSION_2
        defaultTrackShouldNotBeFound("tagVersion2.in=" + UPDATED_TAG_VERSION_2);
    }

    @Test
    @Transactional
    void getAllTracksByTagVersion2IsNullOrNotNull() throws Exception {
        // Initialize the database
        trackRepository.saveAndFlush(track);

        // Get all the trackList where tagVersion2 is not null
        defaultTrackShouldBeFound("tagVersion2.specified=true");

        // Get all the trackList where tagVersion2 is null
        defaultTrackShouldNotBeFound("tagVersion2.specified=false");
    }

    @Test
    @Transactional
    void getAllTracksByTypeIsEqualToSomething() throws Exception {
        TrackType type;
        if (TestUtil.findAll(em, TrackType.class).isEmpty()) {
            trackRepository.saveAndFlush(track);
            type = TrackTypeResourceIT.createEntity(em);
        } else {
            type = TestUtil.findAll(em, TrackType.class).get(0);
        }
        em.persist(type);
        em.flush();
        track.setType(type);
        trackRepository.saveAndFlush(track);
        Long typeId = type.getId();
        // Get all the trackList where type equals to typeId
        defaultTrackShouldBeFound("typeId.equals=" + typeId);

        // Get all the trackList where type equals to (typeId + 1)
        defaultTrackShouldNotBeFound("typeId.equals=" + (typeId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTrackShouldBeFound(String filter) throws Exception {
        restTrackMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(track.getId().intValue())))
            .andExpect(jsonPath("$.[*].filePath").value(hasItem(DEFAULT_FILE_PATH)))
            .andExpect(jsonPath("$.[*].subfolder").value(hasItem(DEFAULT_SUBFOLDER)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].sortName").value(hasItem(DEFAULT_SORT_NAME)))
            .andExpect(jsonPath("$.[*].artistName").value(hasItem(DEFAULT_ARTIST_NAME)))
            .andExpect(jsonPath("$.[*].artistSortName").value(hasItem(DEFAULT_ARTIST_SORT_NAME)))
            .andExpect(jsonPath("$.[*].albumName").value(hasItem(DEFAULT_ALBUM_NAME)))
            .andExpect(jsonPath("$.[*].albumSortName").value(hasItem(DEFAULT_ALBUM_SORT_NAME)))
            .andExpect(jsonPath("$.[*].albumArtistName").value(hasItem(DEFAULT_ALBUM_ARTIST_NAME)))
            .andExpect(jsonPath("$.[*].albumArtistSortName").value(hasItem(DEFAULT_ALBUM_ARTIST_SORT_NAME)))
            .andExpect(jsonPath("$.[*].albumReleasedYear").value(hasItem(DEFAULT_ALBUM_RELEASED_YEAR)))
            .andExpect(jsonPath("$.[*].genreName").value(hasItem(DEFAULT_GENRE_NAME)))
            .andExpect(jsonPath("$.[*].genreSortName").value(hasItem(DEFAULT_GENRE_SORT_NAME)))
            .andExpect(jsonPath("$.[*].trackNumber").value(hasItem(DEFAULT_TRACK_NUMBER)))
            .andExpect(jsonPath("$.[*].playbackLength").value(hasItem(DEFAULT_PLAYBACK_LENGTH)))
            .andExpect(jsonPath("$.[*].bitRate").value(hasItem(DEFAULT_BIT_RATE)))
            .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON.toString())))
            .andExpect(jsonPath("$.[*].tagVersion1").value(hasItem(DEFAULT_TAG_VERSION_1.booleanValue())))
            .andExpect(jsonPath("$.[*].tagVersion2").value(hasItem(DEFAULT_TAG_VERSION_2.booleanValue())));

        // Check, that the count call also returns 1
        restTrackMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTrackShouldNotBeFound(String filter) throws Exception {
        restTrackMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTrackMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTrack() throws Exception {
        // Get the track
        restTrackMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTrack() throws Exception {
        // Initialize the database
        trackRepository.saveAndFlush(track);

        int databaseSizeBeforeUpdate = trackRepository.findAll().size();
        trackSearchRepository.save(track);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(trackSearchRepository.findAll());

        // Update the track
        Track updatedTrack = trackRepository.findById(track.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTrack are not directly saved in db
        em.detach(updatedTrack);
        updatedTrack
            .filePath(UPDATED_FILE_PATH)
            .subfolder(UPDATED_SUBFOLDER)
            .name(UPDATED_NAME)
            .sortName(UPDATED_SORT_NAME)
            //            .artistName(UPDATED_ARTIST_NAME)
            //            .artistSortName(UPDATED_ARTIST_SORT_NAME)
            //            .albumName(UPDATED_ALBUM_NAME)
            //            .albumSortName(UPDATED_ALBUM_SORT_NAME)
            //            .albumArtistName(UPDATED_ALBUM_ARTIST_NAME)
            //            .albumArtistSortName(UPDATED_ALBUM_ARTIST_SORT_NAME)
            //            .albumReleasedYear(UPDATED_ALBUM_RELEASED_YEAR)
            //            .genreName(UPDATED_GENRE_NAME)
            //            .genreSortName(UPDATED_GENRE_SORT_NAME)
            .trackNumber(UPDATED_TRACK_NUMBER)
            .playbackLength(UPDATED_PLAYBACK_LENGTH)
            .bitRate(UPDATED_BIT_RATE)
            .createdOn(UPDATED_CREATED_ON)
            .tagVersion1(UPDATED_TAG_VERSION_1)
            .tagVersion2(UPDATED_TAG_VERSION_2);
        TrackDTO trackDTO = trackMapper.toDto(updatedTrack);

        restTrackMockMvc
            .perform(
                put(ENTITY_API_URL_ID, trackDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(trackDTO))
            )
            .andExpect(status().isOk());

        // Validate the Track in the database
        List<Track> trackList = trackRepository.findAll();
        assertThat(trackList).hasSize(databaseSizeBeforeUpdate);
        Track testTrack = trackList.get(trackList.size() - 1);
        assertThat(testTrack.getFilePath()).isEqualTo(UPDATED_FILE_PATH);
        assertThat(testTrack.getSubfolder()).isEqualTo(UPDATED_SUBFOLDER);
        assertThat(testTrack.getName()).isEqualTo(UPDATED_NAME);
        //        assertThat(testTrack.getSortName()).isEqualTo(UPDATED_SORT_NAME);
        //        assertThat(testTrack.getArtistName()).isEqualTo(UPDATED_ARTIST_NAME);
        //        assertThat(testTrack.getArtistSortName()).isEqualTo(UPDATED_ARTIST_SORT_NAME);
        //        assertThat(testTrack.getAlbumName()).isEqualTo(UPDATED_ALBUM_NAME);
        //        assertThat(testTrack.getAlbumSortName()).isEqualTo(UPDATED_ALBUM_SORT_NAME);
        //        assertThat(testTrack.getAlbumArtistName()).isEqualTo(UPDATED_ALBUM_ARTIST_NAME);
        //        assertThat(testTrack.getAlbumArtistSortName()).isEqualTo(UPDATED_ALBUM_ARTIST_SORT_NAME);
        //        assertThat(testTrack.getAlbumReleasedYear()).isEqualTo(UPDATED_ALBUM_RELEASED_YEAR);
        //        assertThat(testTrack.getGenreName()).isEqualTo(UPDATED_GENRE_NAME);
        //        assertThat(testTrack.getGenreSortName()).isEqualTo(UPDATED_GENRE_SORT_NAME);
        assertThat(testTrack.getTrackNumber()).isEqualTo(UPDATED_TRACK_NUMBER);
        assertThat(testTrack.getPlaybackLength()).isEqualTo(UPDATED_PLAYBACK_LENGTH);
        assertThat(testTrack.getBitRate()).isEqualTo(UPDATED_BIT_RATE);
        assertThat(testTrack.getCreatedOn()).isEqualTo(UPDATED_CREATED_ON);
        assertThat(testTrack.getTagVersion1()).isEqualTo(UPDATED_TAG_VERSION_1);
        assertThat(testTrack.getTagVersion2()).isEqualTo(UPDATED_TAG_VERSION_2);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(trackSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<Track> trackSearchList = IterableUtils.toList(trackSearchRepository.findAll());
                Track testTrackSearch = trackSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testTrackSearch.getFilePath()).isEqualTo(UPDATED_FILE_PATH);
                assertThat(testTrackSearch.getSubfolder()).isEqualTo(UPDATED_SUBFOLDER);
                assertThat(testTrackSearch.getName()).isEqualTo(UPDATED_NAME);
                assertThat(testTrackSearch.getSortName()).isEqualTo(UPDATED_SORT_NAME);
                //                assertThat(testTrackSearch.getArtistName()).isEqualTo(UPDATED_ARTIST_NAME);
                //                assertThat(testTrackSearch.getArtistSortName()).isEqualTo(UPDATED_ARTIST_SORT_NAME);
                //                assertThat(testTrackSearch.getAlbumName()).isEqualTo(UPDATED_ALBUM_NAME);
                //                assertThat(testTrackSearch.getAlbumSortName()).isEqualTo(UPDATED_ALBUM_SORT_NAME);
                //                assertThat(testTrackSearch.getAlbumArtistName()).isEqualTo(UPDATED_ALBUM_ARTIST_NAME);
                //                assertThat(testTrackSearch.getAlbumArtistSortName()).isEqualTo(UPDATED_ALBUM_ARTIST_SORT_NAME);
                //                assertThat(testTrackSearch.getAlbumReleasedYear()).isEqualTo(UPDATED_ALBUM_RELEASED_YEAR);
                //                assertThat(testTrackSearch.getGenreName()).isEqualTo(UPDATED_GENRE_NAME);
                //                assertThat(testTrackSearch.getGenreSortName()).isEqualTo(UPDATED_GENRE_SORT_NAME);
                assertThat(testTrackSearch.getTrackNumber()).isEqualTo(UPDATED_TRACK_NUMBER);
                assertThat(testTrackSearch.getPlaybackLength()).isEqualTo(UPDATED_PLAYBACK_LENGTH);
                assertThat(testTrackSearch.getBitRate()).isEqualTo(UPDATED_BIT_RATE);
                assertThat(testTrackSearch.getCreatedOn()).isEqualTo(UPDATED_CREATED_ON);
                assertThat(testTrackSearch.getTagVersion1()).isEqualTo(UPDATED_TAG_VERSION_1);
                assertThat(testTrackSearch.getTagVersion2()).isEqualTo(UPDATED_TAG_VERSION_2);
            });
    }

    @Test
    @Transactional
    void putNonExistingTrack() throws Exception {
        int databaseSizeBeforeUpdate = trackRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(trackSearchRepository.findAll());
        track.setId(longCount.incrementAndGet());

        // Create the Track
        TrackDTO trackDTO = trackMapper.toDto(track);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTrackMockMvc
            .perform(
                put(ENTITY_API_URL_ID, trackDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(trackDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Track in the database
        List<Track> trackList = trackRepository.findAll();
        assertThat(trackList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(trackSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchTrack() throws Exception {
        int databaseSizeBeforeUpdate = trackRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(trackSearchRepository.findAll());
        track.setId(longCount.incrementAndGet());

        // Create the Track
        TrackDTO trackDTO = trackMapper.toDto(track);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrackMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(trackDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Track in the database
        List<Track> trackList = trackRepository.findAll();
        assertThat(trackList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(trackSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTrack() throws Exception {
        int databaseSizeBeforeUpdate = trackRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(trackSearchRepository.findAll());
        track.setId(longCount.incrementAndGet());

        // Create the Track
        TrackDTO trackDTO = trackMapper.toDto(track);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrackMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(trackDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Track in the database
        List<Track> trackList = trackRepository.findAll();
        assertThat(trackList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(trackSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateTrackWithPatch() throws Exception {
        // Initialize the database
        trackRepository.saveAndFlush(track);

        int databaseSizeBeforeUpdate = trackRepository.findAll().size();

        // Update the track using partial update
        Track partialUpdatedTrack = new Track();
        partialUpdatedTrack.setId(track.getId());

        partialUpdatedTrack
            .filePath(UPDATED_FILE_PATH)
            .subfolder(UPDATED_SUBFOLDER)
            .name(UPDATED_NAME)
            .sortName(UPDATED_SORT_NAME)
            //            .artistName(UPDATED_ARTIST_NAME)
            //            .artistSortName(UPDATED_ARTIST_SORT_NAME)
            //            .albumArtistSortName(UPDATED_ALBUM_ARTIST_SORT_NAME)
            //            .albumReleasedYear(UPDATED_ALBUM_RELEASED_YEAR)
            //            .genreSortName(UPDATED_GENRE_SORT_NAME)
            .playbackLength(UPDATED_PLAYBACK_LENGTH)
            .createdOn(UPDATED_CREATED_ON)
            .tagVersion1(UPDATED_TAG_VERSION_1)
            .tagVersion2(UPDATED_TAG_VERSION_2);

        restTrackMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTrack.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTrack))
            )
            .andExpect(status().isOk());

        // Validate the Track in the database
        List<Track> trackList = trackRepository.findAll();
        assertThat(trackList).hasSize(databaseSizeBeforeUpdate);
        Track testTrack = trackList.get(trackList.size() - 1);
        assertThat(testTrack.getFilePath()).isEqualTo(UPDATED_FILE_PATH);
        assertThat(testTrack.getSubfolder()).isEqualTo(UPDATED_SUBFOLDER);
        assertThat(testTrack.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTrack.getSortName()).isEqualTo(UPDATED_SORT_NAME);
        //        assertThat(testTrack.getArtistName()).isEqualTo(UPDATED_ARTIST_NAME);
        //        assertThat(testTrack.getArtistSortName()).isEqualTo(UPDATED_ARTIST_SORT_NAME);
        //        assertThat(testTrack.getAlbumName()).isEqualTo(DEFAULT_ALBUM_NAME);
        //        assertThat(testTrack.getAlbumSortName()).isEqualTo(DEFAULT_ALBUM_SORT_NAME);
        //        assertThat(testTrack.getAlbumArtistName()).isEqualTo(DEFAULT_ALBUM_ARTIST_NAME);
        //        assertThat(testTrack.getAlbumArtistSortName()).isEqualTo(UPDATED_ALBUM_ARTIST_SORT_NAME);
        //        assertThat(testTrack.getAlbumReleasedYear()).isEqualTo(UPDATED_ALBUM_RELEASED_YEAR);
        //        assertThat(testTrack.getGenreName()).isEqualTo(DEFAULT_GENRE_NAME);
        //        assertThat(testTrack.getGenreSortName()).isEqualTo(UPDATED_GENRE_SORT_NAME);
        assertThat(testTrack.getTrackNumber()).isEqualTo(DEFAULT_TRACK_NUMBER);
        assertThat(testTrack.getPlaybackLength()).isEqualTo(UPDATED_PLAYBACK_LENGTH);
        assertThat(testTrack.getBitRate()).isEqualTo(DEFAULT_BIT_RATE);
        assertThat(testTrack.getCreatedOn()).isEqualTo(UPDATED_CREATED_ON);
        assertThat(testTrack.getTagVersion1()).isEqualTo(UPDATED_TAG_VERSION_1);
        assertThat(testTrack.getTagVersion2()).isEqualTo(UPDATED_TAG_VERSION_2);
    }

    @Test
    @Transactional
    void fullUpdateTrackWithPatch() throws Exception {
        // Initialize the database
        trackRepository.saveAndFlush(track);

        int databaseSizeBeforeUpdate = trackRepository.findAll().size();

        // Update the track using partial update
        Track partialUpdatedTrack = new Track();
        partialUpdatedTrack.setId(track.getId());

        partialUpdatedTrack
            .filePath(UPDATED_FILE_PATH)
            .subfolder(UPDATED_SUBFOLDER)
            .name(UPDATED_NAME)
            .sortName(UPDATED_SORT_NAME)
            //            .artistName(UPDATED_ARTIST_NAME)
            //            .artistSortName(UPDATED_ARTIST_SORT_NAME)
            //            .albumName(UPDATED_ALBUM_NAME)
            //            .albumSortName(UPDATED_ALBUM_SORT_NAME)
            //            .albumArtistName(UPDATED_ALBUM_ARTIST_NAME)
            //            .albumArtistSortName(UPDATED_ALBUM_ARTIST_SORT_NAME)
            //            .albumReleasedYear(UPDATED_ALBUM_RELEASED_YEAR)
            //            .genreName(UPDATED_GENRE_NAME)
            //            .genreSortName(UPDATED_GENRE_SORT_NAME)
            .trackNumber(UPDATED_TRACK_NUMBER)
            .playbackLength(UPDATED_PLAYBACK_LENGTH)
            .bitRate(UPDATED_BIT_RATE)
            .createdOn(UPDATED_CREATED_ON)
            .tagVersion1(UPDATED_TAG_VERSION_1)
            .tagVersion2(UPDATED_TAG_VERSION_2);

        restTrackMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTrack.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTrack))
            )
            .andExpect(status().isOk());

        // Validate the Track in the database
        List<Track> trackList = trackRepository.findAll();
        assertThat(trackList).hasSize(databaseSizeBeforeUpdate);
        Track testTrack = trackList.get(trackList.size() - 1);
        assertThat(testTrack.getFilePath()).isEqualTo(UPDATED_FILE_PATH);
        assertThat(testTrack.getSubfolder()).isEqualTo(UPDATED_SUBFOLDER);
        assertThat(testTrack.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTrack.getSortName()).isEqualTo(UPDATED_SORT_NAME);
        //        assertThat(testTrack.getArtistName()).isEqualTo(UPDATED_ARTIST_NAME);
        //        assertThat(testTrack.getArtistSortName()).isEqualTo(UPDATED_ARTIST_SORT_NAME);
        //        assertThat(testTrack.getAlbumName()).isEqualTo(UPDATED_ALBUM_NAME);
        //        assertThat(testTrack.getAlbumSortName()).isEqualTo(UPDATED_ALBUM_SORT_NAME);
        //        assertThat(testTrack.getAlbumArtistName()).isEqualTo(UPDATED_ALBUM_ARTIST_NAME);
        //        assertThat(testTrack.getAlbumArtistSortName()).isEqualTo(UPDATED_ALBUM_ARTIST_SORT_NAME);
        //        assertThat(testTrack.getAlbumReleasedYear()).isEqualTo(UPDATED_ALBUM_RELEASED_YEAR);
        //        assertThat(testTrack.getGenreName()).isEqualTo(UPDATED_GENRE_NAME);
        //        assertThat(testTrack.getGenreSortName()).isEqualTo(UPDATED_GENRE_SORT_NAME);
        assertThat(testTrack.getTrackNumber()).isEqualTo(UPDATED_TRACK_NUMBER);
        assertThat(testTrack.getPlaybackLength()).isEqualTo(UPDATED_PLAYBACK_LENGTH);
        assertThat(testTrack.getBitRate()).isEqualTo(UPDATED_BIT_RATE);
        assertThat(testTrack.getCreatedOn()).isEqualTo(UPDATED_CREATED_ON);
        assertThat(testTrack.getTagVersion1()).isEqualTo(UPDATED_TAG_VERSION_1);
        assertThat(testTrack.getTagVersion2()).isEqualTo(UPDATED_TAG_VERSION_2);
    }

    @Test
    @Transactional
    void patchNonExistingTrack() throws Exception {
        int databaseSizeBeforeUpdate = trackRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(trackSearchRepository.findAll());
        track.setId(longCount.incrementAndGet());

        // Create the Track
        TrackDTO trackDTO = trackMapper.toDto(track);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTrackMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, trackDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(trackDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Track in the database
        List<Track> trackList = trackRepository.findAll();
        assertThat(trackList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(trackSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTrack() throws Exception {
        int databaseSizeBeforeUpdate = trackRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(trackSearchRepository.findAll());
        track.setId(longCount.incrementAndGet());

        // Create the Track
        TrackDTO trackDTO = trackMapper.toDto(track);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrackMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(trackDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Track in the database
        List<Track> trackList = trackRepository.findAll();
        assertThat(trackList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(trackSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTrack() throws Exception {
        int databaseSizeBeforeUpdate = trackRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(trackSearchRepository.findAll());
        track.setId(longCount.incrementAndGet());

        // Create the Track
        TrackDTO trackDTO = trackMapper.toDto(track);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrackMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(trackDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Track in the database
        List<Track> trackList = trackRepository.findAll();
        assertThat(trackList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(trackSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteTrack() throws Exception {
        // Initialize the database
        trackRepository.saveAndFlush(track);
        trackRepository.save(track);
        trackSearchRepository.save(track);

        int databaseSizeBeforeDelete = trackRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(trackSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the track
        restTrackMockMvc
            .perform(delete(ENTITY_API_URL_ID, track.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Track> trackList = trackRepository.findAll();
        assertThat(trackList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(trackSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchTrack() throws Exception {
        // Initialize the database
        track = trackRepository.saveAndFlush(track);
        trackSearchRepository.save(track);

        // Search the track
        restTrackMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + track.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(track.getId().intValue())))
            .andExpect(jsonPath("$.[*].filePath").value(hasItem(DEFAULT_FILE_PATH)))
            .andExpect(jsonPath("$.[*].subfolder").value(hasItem(DEFAULT_SUBFOLDER)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].sortName").value(hasItem(DEFAULT_SORT_NAME)))
            .andExpect(jsonPath("$.[*].artistName").value(hasItem(DEFAULT_ARTIST_NAME)))
            .andExpect(jsonPath("$.[*].artistSortName").value(hasItem(DEFAULT_ARTIST_SORT_NAME)))
            .andExpect(jsonPath("$.[*].albumName").value(hasItem(DEFAULT_ALBUM_NAME)))
            .andExpect(jsonPath("$.[*].albumSortName").value(hasItem(DEFAULT_ALBUM_SORT_NAME)))
            .andExpect(jsonPath("$.[*].albumArtistName").value(hasItem(DEFAULT_ALBUM_ARTIST_NAME)))
            .andExpect(jsonPath("$.[*].albumArtistSortName").value(hasItem(DEFAULT_ALBUM_ARTIST_SORT_NAME)))
            .andExpect(jsonPath("$.[*].albumReleasedYear").value(hasItem(DEFAULT_ALBUM_RELEASED_YEAR)))
            .andExpect(jsonPath("$.[*].genreName").value(hasItem(DEFAULT_GENRE_NAME)))
            .andExpect(jsonPath("$.[*].genreSortName").value(hasItem(DEFAULT_GENRE_SORT_NAME)))
            .andExpect(jsonPath("$.[*].trackNumber").value(hasItem(DEFAULT_TRACK_NUMBER)))
            .andExpect(jsonPath("$.[*].playbackLength").value(hasItem(DEFAULT_PLAYBACK_LENGTH)))
            .andExpect(jsonPath("$.[*].bitRate").value(hasItem(DEFAULT_BIT_RATE)))
            .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON.toString())))
            .andExpect(jsonPath("$.[*].tagVersion1").value(hasItem(DEFAULT_TAG_VERSION_1.booleanValue())))
            .andExpect(jsonPath("$.[*].tagVersion2").value(hasItem(DEFAULT_TAG_VERSION_2.booleanValue())));
    }
}
