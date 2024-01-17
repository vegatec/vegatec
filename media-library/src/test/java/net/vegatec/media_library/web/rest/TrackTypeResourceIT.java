package net.vegatec.media_library.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import net.vegatec.media_library.IntegrationTest;
import net.vegatec.media_library.domain.TrackType;
import net.vegatec.media_library.repository.TrackTypeRepository;
import net.vegatec.media_library.repository.search.TrackTypeSearchRepository;
import net.vegatec.media_library.service.dto.TrackTypeDTO;
import net.vegatec.media_library.service.mapper.TrackTypeMapper;
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
 * Integration tests for the {@link TrackTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TrackTypeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_CREDITS_NEEDED = 1;
    private static final Integer UPDATED_CREDITS_NEEDED = 2;

    private static final Integer DEFAULT_VIP_CREDITS_NEEDED = 1;
    private static final Integer UPDATED_VIP_CREDITS_NEEDED = 2;

    private static final String ENTITY_API_URL = "/api/track-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/track-types/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TrackTypeRepository trackTypeRepository;

    @Autowired
    private TrackTypeMapper trackTypeMapper;

    @Autowired
    private TrackTypeSearchRepository trackTypeSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTrackTypeMockMvc;

    private TrackType trackType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TrackType createEntity(EntityManager em) {
        TrackType trackType = new TrackType()
            .name(DEFAULT_NAME)
            .creditsNeeded(DEFAULT_CREDITS_NEEDED)
            .vipCreditsNeeded(DEFAULT_VIP_CREDITS_NEEDED);
        return trackType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TrackType createUpdatedEntity(EntityManager em) {
        TrackType trackType = new TrackType()
            .name(UPDATED_NAME)
            .creditsNeeded(UPDATED_CREDITS_NEEDED)
            .vipCreditsNeeded(UPDATED_VIP_CREDITS_NEEDED);
        return trackType;
    }

    @AfterEach
    public void cleanupElasticSearchRepository() {
        trackTypeSearchRepository.deleteAll();
        assertThat(trackTypeSearchRepository.count()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        trackType = createEntity(em);
    }

    @Test
    @Transactional
    void createTrackType() throws Exception {
        int databaseSizeBeforeCreate = trackTypeRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(trackTypeSearchRepository.findAll());
        // Create the TrackType
        TrackTypeDTO trackTypeDTO = trackTypeMapper.toDto(trackType);
        restTrackTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(trackTypeDTO))
            )
            .andExpect(status().isCreated());

        // Validate the TrackType in the database
        List<TrackType> trackTypeList = trackTypeRepository.findAll();
        assertThat(trackTypeList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(trackTypeSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        TrackType testTrackType = trackTypeList.get(trackTypeList.size() - 1);
        assertThat(testTrackType.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testTrackType.getCreditsNeeded()).isEqualTo(DEFAULT_CREDITS_NEEDED);
        assertThat(testTrackType.getVipCreditsNeeded()).isEqualTo(DEFAULT_VIP_CREDITS_NEEDED);
    }

    @Test
    @Transactional
    void createTrackTypeWithExistingId() throws Exception {
        // Create the TrackType with an existing ID
        trackType.setId(1L);
        TrackTypeDTO trackTypeDTO = trackTypeMapper.toDto(trackType);

        int databaseSizeBeforeCreate = trackTypeRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(trackTypeSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restTrackTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(trackTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TrackType in the database
        List<TrackType> trackTypeList = trackTypeRepository.findAll();
        assertThat(trackTypeList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(trackTypeSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = trackTypeRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(trackTypeSearchRepository.findAll());
        // set the field null
        trackType.setName(null);

        // Create the TrackType, which fails.
        TrackTypeDTO trackTypeDTO = trackTypeMapper.toDto(trackType);

        restTrackTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(trackTypeDTO))
            )
            .andExpect(status().isBadRequest());

        List<TrackType> trackTypeList = trackTypeRepository.findAll();
        assertThat(trackTypeList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(trackTypeSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllTrackTypes() throws Exception {
        // Initialize the database
        trackTypeRepository.saveAndFlush(trackType);

        // Get all the trackTypeList
        restTrackTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(trackType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].creditsNeeded").value(hasItem(DEFAULT_CREDITS_NEEDED)))
            .andExpect(jsonPath("$.[*].vipCreditsNeeded").value(hasItem(DEFAULT_VIP_CREDITS_NEEDED)));
    }

    @Test
    @Transactional
    void getTrackType() throws Exception {
        // Initialize the database
        trackTypeRepository.saveAndFlush(trackType);

        // Get the trackType
        restTrackTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, trackType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(trackType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.creditsNeeded").value(DEFAULT_CREDITS_NEEDED))
            .andExpect(jsonPath("$.vipCreditsNeeded").value(DEFAULT_VIP_CREDITS_NEEDED));
    }

    @Test
    @Transactional
    void getNonExistingTrackType() throws Exception {
        // Get the trackType
        restTrackTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTrackType() throws Exception {
        // Initialize the database
        trackTypeRepository.saveAndFlush(trackType);

        int databaseSizeBeforeUpdate = trackTypeRepository.findAll().size();
        trackTypeSearchRepository.save(trackType);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(trackTypeSearchRepository.findAll());

        // Update the trackType
        TrackType updatedTrackType = trackTypeRepository.findById(trackType.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTrackType are not directly saved in db
        em.detach(updatedTrackType);
        updatedTrackType.name(UPDATED_NAME).creditsNeeded(UPDATED_CREDITS_NEEDED).vipCreditsNeeded(UPDATED_VIP_CREDITS_NEEDED);
        TrackTypeDTO trackTypeDTO = trackTypeMapper.toDto(updatedTrackType);

        restTrackTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, trackTypeDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(trackTypeDTO))
            )
            .andExpect(status().isOk());

        // Validate the TrackType in the database
        List<TrackType> trackTypeList = trackTypeRepository.findAll();
        assertThat(trackTypeList).hasSize(databaseSizeBeforeUpdate);
        TrackType testTrackType = trackTypeList.get(trackTypeList.size() - 1);
        assertThat(testTrackType.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTrackType.getCreditsNeeded()).isEqualTo(UPDATED_CREDITS_NEEDED);
        assertThat(testTrackType.getVipCreditsNeeded()).isEqualTo(UPDATED_VIP_CREDITS_NEEDED);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(trackTypeSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<TrackType> trackTypeSearchList = IterableUtils.toList(trackTypeSearchRepository.findAll());
                TrackType testTrackTypeSearch = trackTypeSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testTrackTypeSearch.getName()).isEqualTo(UPDATED_NAME);
                assertThat(testTrackTypeSearch.getCreditsNeeded()).isEqualTo(UPDATED_CREDITS_NEEDED);
                assertThat(testTrackTypeSearch.getVipCreditsNeeded()).isEqualTo(UPDATED_VIP_CREDITS_NEEDED);
            });
    }

    @Test
    @Transactional
    void putNonExistingTrackType() throws Exception {
        int databaseSizeBeforeUpdate = trackTypeRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(trackTypeSearchRepository.findAll());
        trackType.setId(longCount.incrementAndGet());

        // Create the TrackType
        TrackTypeDTO trackTypeDTO = trackTypeMapper.toDto(trackType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTrackTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, trackTypeDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(trackTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TrackType in the database
        List<TrackType> trackTypeList = trackTypeRepository.findAll();
        assertThat(trackTypeList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(trackTypeSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchTrackType() throws Exception {
        int databaseSizeBeforeUpdate = trackTypeRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(trackTypeSearchRepository.findAll());
        trackType.setId(longCount.incrementAndGet());

        // Create the TrackType
        TrackTypeDTO trackTypeDTO = trackTypeMapper.toDto(trackType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrackTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(trackTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TrackType in the database
        List<TrackType> trackTypeList = trackTypeRepository.findAll();
        assertThat(trackTypeList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(trackTypeSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTrackType() throws Exception {
        int databaseSizeBeforeUpdate = trackTypeRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(trackTypeSearchRepository.findAll());
        trackType.setId(longCount.incrementAndGet());

        // Create the TrackType
        TrackTypeDTO trackTypeDTO = trackTypeMapper.toDto(trackType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrackTypeMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(trackTypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TrackType in the database
        List<TrackType> trackTypeList = trackTypeRepository.findAll();
        assertThat(trackTypeList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(trackTypeSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateTrackTypeWithPatch() throws Exception {
        // Initialize the database
        trackTypeRepository.saveAndFlush(trackType);

        int databaseSizeBeforeUpdate = trackTypeRepository.findAll().size();

        // Update the trackType using partial update
        TrackType partialUpdatedTrackType = new TrackType();
        partialUpdatedTrackType.setId(trackType.getId());

        partialUpdatedTrackType.name(UPDATED_NAME).creditsNeeded(UPDATED_CREDITS_NEEDED);

        restTrackTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTrackType.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTrackType))
            )
            .andExpect(status().isOk());

        // Validate the TrackType in the database
        List<TrackType> trackTypeList = trackTypeRepository.findAll();
        assertThat(trackTypeList).hasSize(databaseSizeBeforeUpdate);
        TrackType testTrackType = trackTypeList.get(trackTypeList.size() - 1);
        assertThat(testTrackType.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTrackType.getCreditsNeeded()).isEqualTo(UPDATED_CREDITS_NEEDED);
        assertThat(testTrackType.getVipCreditsNeeded()).isEqualTo(DEFAULT_VIP_CREDITS_NEEDED);
    }

    @Test
    @Transactional
    void fullUpdateTrackTypeWithPatch() throws Exception {
        // Initialize the database
        trackTypeRepository.saveAndFlush(trackType);

        int databaseSizeBeforeUpdate = trackTypeRepository.findAll().size();

        // Update the trackType using partial update
        TrackType partialUpdatedTrackType = new TrackType();
        partialUpdatedTrackType.setId(trackType.getId());

        partialUpdatedTrackType.name(UPDATED_NAME).creditsNeeded(UPDATED_CREDITS_NEEDED).vipCreditsNeeded(UPDATED_VIP_CREDITS_NEEDED);

        restTrackTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTrackType.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTrackType))
            )
            .andExpect(status().isOk());

        // Validate the TrackType in the database
        List<TrackType> trackTypeList = trackTypeRepository.findAll();
        assertThat(trackTypeList).hasSize(databaseSizeBeforeUpdate);
        TrackType testTrackType = trackTypeList.get(trackTypeList.size() - 1);
        assertThat(testTrackType.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTrackType.getCreditsNeeded()).isEqualTo(UPDATED_CREDITS_NEEDED);
        assertThat(testTrackType.getVipCreditsNeeded()).isEqualTo(UPDATED_VIP_CREDITS_NEEDED);
    }

    @Test
    @Transactional
    void patchNonExistingTrackType() throws Exception {
        int databaseSizeBeforeUpdate = trackTypeRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(trackTypeSearchRepository.findAll());
        trackType.setId(longCount.incrementAndGet());

        // Create the TrackType
        TrackTypeDTO trackTypeDTO = trackTypeMapper.toDto(trackType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTrackTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, trackTypeDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(trackTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TrackType in the database
        List<TrackType> trackTypeList = trackTypeRepository.findAll();
        assertThat(trackTypeList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(trackTypeSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTrackType() throws Exception {
        int databaseSizeBeforeUpdate = trackTypeRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(trackTypeSearchRepository.findAll());
        trackType.setId(longCount.incrementAndGet());

        // Create the TrackType
        TrackTypeDTO trackTypeDTO = trackTypeMapper.toDto(trackType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrackTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(trackTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TrackType in the database
        List<TrackType> trackTypeList = trackTypeRepository.findAll();
        assertThat(trackTypeList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(trackTypeSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTrackType() throws Exception {
        int databaseSizeBeforeUpdate = trackTypeRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(trackTypeSearchRepository.findAll());
        trackType.setId(longCount.incrementAndGet());

        // Create the TrackType
        TrackTypeDTO trackTypeDTO = trackTypeMapper.toDto(trackType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrackTypeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(trackTypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TrackType in the database
        List<TrackType> trackTypeList = trackTypeRepository.findAll();
        assertThat(trackTypeList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(trackTypeSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteTrackType() throws Exception {
        // Initialize the database
        trackTypeRepository.saveAndFlush(trackType);
        trackTypeRepository.save(trackType);
        trackTypeSearchRepository.save(trackType);

        int databaseSizeBeforeDelete = trackTypeRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(trackTypeSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the trackType
        restTrackTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, trackType.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TrackType> trackTypeList = trackTypeRepository.findAll();
        assertThat(trackTypeList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(trackTypeSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchTrackType() throws Exception {
        // Initialize the database
        trackType = trackTypeRepository.saveAndFlush(trackType);
        trackTypeSearchRepository.save(trackType);

        // Search the trackType
        restTrackTypeMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + trackType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(trackType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].creditsNeeded").value(hasItem(DEFAULT_CREDITS_NEEDED)))
            .andExpect(jsonPath("$.[*].vipCreditsNeeded").value(hasItem(DEFAULT_VIP_CREDITS_NEEDED)));
    }
}
