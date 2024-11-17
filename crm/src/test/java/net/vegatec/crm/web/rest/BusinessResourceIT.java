package net.vegatec.crm.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import net.vegatec.crm.IntegrationTest;
import net.vegatec.crm.query.domain.Business;
import net.vegatec.crm.query.domain.Product;
import net.vegatec.crm.query.repository.BusinessRepository;
import net.vegatec.crm.query.service.dto.BusinessDTO;
import net.vegatec.crm.query.service.mapper.BusinessMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link BusinessResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BusinessResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_OWNER = "AAAAAAAAAA";
    private static final String UPDATED_OWNER = "BBBBBBBBBB";

    private static final String DEFAULT_CONTACT = "AAAAAAAAAA";
    private static final String UPDATED_CONTACT = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/businesses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BusinessRepository businessRepository;

    @Autowired
    private BusinessMapper businessMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBusinessMockMvc;

    private Business business;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Business createEntity(EntityManager em) {
        Business business = new Business()
            .name(DEFAULT_NAME)
            .address(DEFAULT_ADDRESS)
            .owner(DEFAULT_OWNER)
            .contact(DEFAULT_CONTACT)
            .phone(DEFAULT_PHONE);
        return business;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Business createUpdatedEntity(EntityManager em) {
        Business business = new Business()
            .name(UPDATED_NAME)
            .address(UPDATED_ADDRESS)
            .owner(UPDATED_OWNER)
            .contact(UPDATED_CONTACT)
            .phone(UPDATED_PHONE);
        return business;
    }

    @BeforeEach
    public void initTest() {
        business = createEntity(em);
    }

    @Test
    @Transactional
    void createBusiness() throws Exception {
        int databaseSizeBeforeCreate = businessRepository.findAll().size();
        // Create the Business
        BusinessDTO businessDTO = businessMapper.toDto(business);
        restBusinessMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(businessDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Business in the database
        List<Business> businessList = businessRepository.findAll();
        assertThat(businessList).hasSize(databaseSizeBeforeCreate + 1);
        Business testBusiness = businessList.get(businessList.size() - 1);
        assertThat(testBusiness.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testBusiness.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testBusiness.getOwner()).isEqualTo(DEFAULT_OWNER);
        assertThat(testBusiness.getContact()).isEqualTo(DEFAULT_CONTACT);
        assertThat(testBusiness.getPhone()).isEqualTo(DEFAULT_PHONE);
    }

    @Test
    @Transactional
    void createBusinessWithExistingId() throws Exception {
        // Create the Business with an existing ID
        business.setId(1L);
        BusinessDTO businessDTO = businessMapper.toDto(business);

        int databaseSizeBeforeCreate = businessRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBusinessMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(businessDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Business in the database
        List<Business> businessList = businessRepository.findAll();
        assertThat(businessList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = businessRepository.findAll().size();
        // set the field null
        business.setName(null);

        // Create the Business, which fails.
        BusinessDTO businessDTO = businessMapper.toDto(business);

        restBusinessMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(businessDTO))
            )
            .andExpect(status().isBadRequest());

        List<Business> businessList = businessRepository.findAll();
        assertThat(businessList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAddressIsRequired() throws Exception {
        int databaseSizeBeforeTest = businessRepository.findAll().size();
        // set the field null
        business.setAddress(null);

        // Create the Business, which fails.
        BusinessDTO businessDTO = businessMapper.toDto(business);

        restBusinessMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(businessDTO))
            )
            .andExpect(status().isBadRequest());

        List<Business> businessList = businessRepository.findAll();
        assertThat(businessList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkOwnerIsRequired() throws Exception {
        int databaseSizeBeforeTest = businessRepository.findAll().size();
        // set the field null
        business.setOwner(null);

        // Create the Business, which fails.
        BusinessDTO businessDTO = businessMapper.toDto(business);

        restBusinessMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(businessDTO))
            )
            .andExpect(status().isBadRequest());

        List<Business> businessList = businessRepository.findAll();
        assertThat(businessList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllBusinesses() throws Exception {
        // Initialize the database
        businessRepository.saveAndFlush(business);

        // Get all the businessList
        restBusinessMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(business.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].owner").value(hasItem(DEFAULT_OWNER)))
            .andExpect(jsonPath("$.[*].contact").value(hasItem(DEFAULT_CONTACT)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)));
    }

    @Test
    @Transactional
    void getBusiness() throws Exception {
        // Initialize the database
        businessRepository.saveAndFlush(business);

        // Get the business
        restBusinessMockMvc
            .perform(get(ENTITY_API_URL_ID, business.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(business.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.owner").value(DEFAULT_OWNER))
            .andExpect(jsonPath("$.contact").value(DEFAULT_CONTACT))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE));
    }

    @Test
    @Transactional
    void getBusinessesByIdFiltering() throws Exception {
        // Initialize the database
        businessRepository.saveAndFlush(business);

        Long id = business.getId();

        defaultBusinessShouldBeFound("id.equals=" + id);
        defaultBusinessShouldNotBeFound("id.notEquals=" + id);

        defaultBusinessShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultBusinessShouldNotBeFound("id.greaterThan=" + id);

        defaultBusinessShouldBeFound("id.lessThanOrEqual=" + id);
        defaultBusinessShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllBusinessesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        businessRepository.saveAndFlush(business);

        // Get all the businessList where name equals to DEFAULT_NAME
        defaultBusinessShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the businessList where name equals to UPDATED_NAME
        defaultBusinessShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllBusinessesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        businessRepository.saveAndFlush(business);

        // Get all the businessList where name in DEFAULT_NAME or UPDATED_NAME
        defaultBusinessShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the businessList where name equals to UPDATED_NAME
        defaultBusinessShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllBusinessesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        businessRepository.saveAndFlush(business);

        // Get all the businessList where name is not null
        defaultBusinessShouldBeFound("name.specified=true");

        // Get all the businessList where name is null
        defaultBusinessShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllBusinessesByNameContainsSomething() throws Exception {
        // Initialize the database
        businessRepository.saveAndFlush(business);

        // Get all the businessList where name contains DEFAULT_NAME
        defaultBusinessShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the businessList where name contains UPDATED_NAME
        defaultBusinessShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllBusinessesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        businessRepository.saveAndFlush(business);

        // Get all the businessList where name does not contain DEFAULT_NAME
        defaultBusinessShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the businessList where name does not contain UPDATED_NAME
        defaultBusinessShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllBusinessesByAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        businessRepository.saveAndFlush(business);

        // Get all the businessList where address equals to DEFAULT_ADDRESS
        defaultBusinessShouldBeFound("address.equals=" + DEFAULT_ADDRESS);

        // Get all the businessList where address equals to UPDATED_ADDRESS
        defaultBusinessShouldNotBeFound("address.equals=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllBusinessesByAddressIsInShouldWork() throws Exception {
        // Initialize the database
        businessRepository.saveAndFlush(business);

        // Get all the businessList where address in DEFAULT_ADDRESS or UPDATED_ADDRESS
        defaultBusinessShouldBeFound("address.in=" + DEFAULT_ADDRESS + "," + UPDATED_ADDRESS);

        // Get all the businessList where address equals to UPDATED_ADDRESS
        defaultBusinessShouldNotBeFound("address.in=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllBusinessesByAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        businessRepository.saveAndFlush(business);

        // Get all the businessList where address is not null
        defaultBusinessShouldBeFound("address.specified=true");

        // Get all the businessList where address is null
        defaultBusinessShouldNotBeFound("address.specified=false");
    }

    @Test
    @Transactional
    void getAllBusinessesByAddressContainsSomething() throws Exception {
        // Initialize the database
        businessRepository.saveAndFlush(business);

        // Get all the businessList where address contains DEFAULT_ADDRESS
        defaultBusinessShouldBeFound("address.contains=" + DEFAULT_ADDRESS);

        // Get all the businessList where address contains UPDATED_ADDRESS
        defaultBusinessShouldNotBeFound("address.contains=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllBusinessesByAddressNotContainsSomething() throws Exception {
        // Initialize the database
        businessRepository.saveAndFlush(business);

        // Get all the businessList where address does not contain DEFAULT_ADDRESS
        defaultBusinessShouldNotBeFound("address.doesNotContain=" + DEFAULT_ADDRESS);

        // Get all the businessList where address does not contain UPDATED_ADDRESS
        defaultBusinessShouldBeFound("address.doesNotContain=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllBusinessesByOwnerIsEqualToSomething() throws Exception {
        // Initialize the database
        businessRepository.saveAndFlush(business);

        // Get all the businessList where owner equals to DEFAULT_OWNER
        defaultBusinessShouldBeFound("owner.equals=" + DEFAULT_OWNER);

        // Get all the businessList where owner equals to UPDATED_OWNER
        defaultBusinessShouldNotBeFound("owner.equals=" + UPDATED_OWNER);
    }

    @Test
    @Transactional
    void getAllBusinessesByOwnerIsInShouldWork() throws Exception {
        // Initialize the database
        businessRepository.saveAndFlush(business);

        // Get all the businessList where owner in DEFAULT_OWNER or UPDATED_OWNER
        defaultBusinessShouldBeFound("owner.in=" + DEFAULT_OWNER + "," + UPDATED_OWNER);

        // Get all the businessList where owner equals to UPDATED_OWNER
        defaultBusinessShouldNotBeFound("owner.in=" + UPDATED_OWNER);
    }

    @Test
    @Transactional
    void getAllBusinessesByOwnerIsNullOrNotNull() throws Exception {
        // Initialize the database
        businessRepository.saveAndFlush(business);

        // Get all the businessList where owner is not null
        defaultBusinessShouldBeFound("owner.specified=true");

        // Get all the businessList where owner is null
        defaultBusinessShouldNotBeFound("owner.specified=false");
    }

    @Test
    @Transactional
    void getAllBusinessesByOwnerContainsSomething() throws Exception {
        // Initialize the database
        businessRepository.saveAndFlush(business);

        // Get all the businessList where owner contains DEFAULT_OWNER
        defaultBusinessShouldBeFound("owner.contains=" + DEFAULT_OWNER);

        // Get all the businessList where owner contains UPDATED_OWNER
        defaultBusinessShouldNotBeFound("owner.contains=" + UPDATED_OWNER);
    }

    @Test
    @Transactional
    void getAllBusinessesByOwnerNotContainsSomething() throws Exception {
        // Initialize the database
        businessRepository.saveAndFlush(business);

        // Get all the businessList where owner does not contain DEFAULT_OWNER
        defaultBusinessShouldNotBeFound("owner.doesNotContain=" + DEFAULT_OWNER);

        // Get all the businessList where owner does not contain UPDATED_OWNER
        defaultBusinessShouldBeFound("owner.doesNotContain=" + UPDATED_OWNER);
    }

    @Test
    @Transactional
    void getAllBusinessesByContactIsEqualToSomething() throws Exception {
        // Initialize the database
        businessRepository.saveAndFlush(business);

        // Get all the businessList where contact equals to DEFAULT_CONTACT
        defaultBusinessShouldBeFound("contact.equals=" + DEFAULT_CONTACT);

        // Get all the businessList where contact equals to UPDATED_CONTACT
        defaultBusinessShouldNotBeFound("contact.equals=" + UPDATED_CONTACT);
    }

    @Test
    @Transactional
    void getAllBusinessesByContactIsInShouldWork() throws Exception {
        // Initialize the database
        businessRepository.saveAndFlush(business);

        // Get all the businessList where contact in DEFAULT_CONTACT or UPDATED_CONTACT
        defaultBusinessShouldBeFound("contact.in=" + DEFAULT_CONTACT + "," + UPDATED_CONTACT);

        // Get all the businessList where contact equals to UPDATED_CONTACT
        defaultBusinessShouldNotBeFound("contact.in=" + UPDATED_CONTACT);
    }

    @Test
    @Transactional
    void getAllBusinessesByContactIsNullOrNotNull() throws Exception {
        // Initialize the database
        businessRepository.saveAndFlush(business);

        // Get all the businessList where contact is not null
        defaultBusinessShouldBeFound("contact.specified=true");

        // Get all the businessList where contact is null
        defaultBusinessShouldNotBeFound("contact.specified=false");
    }

    @Test
    @Transactional
    void getAllBusinessesByContactContainsSomething() throws Exception {
        // Initialize the database
        businessRepository.saveAndFlush(business);

        // Get all the businessList where contact contains DEFAULT_CONTACT
        defaultBusinessShouldBeFound("contact.contains=" + DEFAULT_CONTACT);

        // Get all the businessList where contact contains UPDATED_CONTACT
        defaultBusinessShouldNotBeFound("contact.contains=" + UPDATED_CONTACT);
    }

    @Test
    @Transactional
    void getAllBusinessesByContactNotContainsSomething() throws Exception {
        // Initialize the database
        businessRepository.saveAndFlush(business);

        // Get all the businessList where contact does not contain DEFAULT_CONTACT
        defaultBusinessShouldNotBeFound("contact.doesNotContain=" + DEFAULT_CONTACT);

        // Get all the businessList where contact does not contain UPDATED_CONTACT
        defaultBusinessShouldBeFound("contact.doesNotContain=" + UPDATED_CONTACT);
    }

    @Test
    @Transactional
    void getAllBusinessesByPhoneIsEqualToSomething() throws Exception {
        // Initialize the database
        businessRepository.saveAndFlush(business);

        // Get all the businessList where phone equals to DEFAULT_PHONE
        defaultBusinessShouldBeFound("phone.equals=" + DEFAULT_PHONE);

        // Get all the businessList where phone equals to UPDATED_PHONE
        defaultBusinessShouldNotBeFound("phone.equals=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllBusinessesByPhoneIsInShouldWork() throws Exception {
        // Initialize the database
        businessRepository.saveAndFlush(business);

        // Get all the businessList where phone in DEFAULT_PHONE or UPDATED_PHONE
        defaultBusinessShouldBeFound("phone.in=" + DEFAULT_PHONE + "," + UPDATED_PHONE);

        // Get all the businessList where phone equals to UPDATED_PHONE
        defaultBusinessShouldNotBeFound("phone.in=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllBusinessesByPhoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        businessRepository.saveAndFlush(business);

        // Get all the businessList where phone is not null
        defaultBusinessShouldBeFound("phone.specified=true");

        // Get all the businessList where phone is null
        defaultBusinessShouldNotBeFound("phone.specified=false");
    }

    @Test
    @Transactional
    void getAllBusinessesByPhoneContainsSomething() throws Exception {
        // Initialize the database
        businessRepository.saveAndFlush(business);

        // Get all the businessList where phone contains DEFAULT_PHONE
        defaultBusinessShouldBeFound("phone.contains=" + DEFAULT_PHONE);

        // Get all the businessList where phone contains UPDATED_PHONE
        defaultBusinessShouldNotBeFound("phone.contains=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllBusinessesByPhoneNotContainsSomething() throws Exception {
        // Initialize the database
        businessRepository.saveAndFlush(business);

        // Get all the businessList where phone does not contain DEFAULT_PHONE
        defaultBusinessShouldNotBeFound("phone.doesNotContain=" + DEFAULT_PHONE);

        // Get all the businessList where phone does not contain UPDATED_PHONE
        defaultBusinessShouldBeFound("phone.doesNotContain=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllBusinessesByProductsIsEqualToSomething() throws Exception {
        Product products;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            businessRepository.saveAndFlush(business);
            products = ProductResourceIT.createEntity(em);
        } else {
            products = TestUtil.findAll(em, Product.class).get(0);
        }
        em.persist(products);
        em.flush();
        business.addProduct(products);
        businessRepository.saveAndFlush(business);
        Long productsId = products.getId();
        // Get all the businessList where products equals to productsId
        defaultBusinessShouldBeFound("productsId.equals=" + productsId);

        // Get all the businessList where products equals to (productsId + 1)
        defaultBusinessShouldNotBeFound("productsId.equals=" + (productsId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultBusinessShouldBeFound(String filter) throws Exception {
        restBusinessMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(business.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].owner").value(hasItem(DEFAULT_OWNER)))
            .andExpect(jsonPath("$.[*].contact").value(hasItem(DEFAULT_CONTACT)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)));

        // Check, that the count call also returns 1
        restBusinessMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultBusinessShouldNotBeFound(String filter) throws Exception {
        restBusinessMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restBusinessMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingBusiness() throws Exception {
        // Get the business
        restBusinessMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingBusiness() throws Exception {
        // Initialize the database
        businessRepository.saveAndFlush(business);

        int databaseSizeBeforeUpdate = businessRepository.findAll().size();

        // Update the business
        Business updatedBusiness = businessRepository.findById(business.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedBusiness are not directly saved in db
        em.detach(updatedBusiness);
        updatedBusiness.name(UPDATED_NAME).address(UPDATED_ADDRESS).owner(UPDATED_OWNER).contact(UPDATED_CONTACT).phone(UPDATED_PHONE);
        BusinessDTO businessDTO = businessMapper.toDto(updatedBusiness);

        restBusinessMockMvc
            .perform(
                put(ENTITY_API_URL_ID, businessDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(businessDTO))
            )
            .andExpect(status().isOk());

        // Validate the Business in the database
        List<Business> businessList = businessRepository.findAll();
        assertThat(businessList).hasSize(databaseSizeBeforeUpdate);
        Business testBusiness = businessList.get(businessList.size() - 1);
        assertThat(testBusiness.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testBusiness.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testBusiness.getOwner()).isEqualTo(UPDATED_OWNER);
        assertThat(testBusiness.getContact()).isEqualTo(UPDATED_CONTACT);
        assertThat(testBusiness.getPhone()).isEqualTo(UPDATED_PHONE);
    }

    @Test
    @Transactional
    void putNonExistingBusiness() throws Exception {
        int databaseSizeBeforeUpdate = businessRepository.findAll().size();
        business.setId(longCount.incrementAndGet());

        // Create the Business
        BusinessDTO businessDTO = businessMapper.toDto(business);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBusinessMockMvc
            .perform(
                put(ENTITY_API_URL_ID, businessDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(businessDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Business in the database
        List<Business> businessList = businessRepository.findAll();
        assertThat(businessList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBusiness() throws Exception {
        int databaseSizeBeforeUpdate = businessRepository.findAll().size();
        business.setId(longCount.incrementAndGet());

        // Create the Business
        BusinessDTO businessDTO = businessMapper.toDto(business);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBusinessMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(businessDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Business in the database
        List<Business> businessList = businessRepository.findAll();
        assertThat(businessList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBusiness() throws Exception {
        int databaseSizeBeforeUpdate = businessRepository.findAll().size();
        business.setId(longCount.incrementAndGet());

        // Create the Business
        BusinessDTO businessDTO = businessMapper.toDto(business);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBusinessMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(businessDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Business in the database
        List<Business> businessList = businessRepository.findAll();
        assertThat(businessList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBusinessWithPatch() throws Exception {
        // Initialize the database
        businessRepository.saveAndFlush(business);

        int databaseSizeBeforeUpdate = businessRepository.findAll().size();

        // Update the business using partial update
        Business partialUpdatedBusiness = new Business();
        partialUpdatedBusiness.setId(business.getId());

        partialUpdatedBusiness.owner(UPDATED_OWNER).phone(UPDATED_PHONE);

        restBusinessMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBusiness.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBusiness))
            )
            .andExpect(status().isOk());

        // Validate the Business in the database
        List<Business> businessList = businessRepository.findAll();
        assertThat(businessList).hasSize(databaseSizeBeforeUpdate);
        Business testBusiness = businessList.get(businessList.size() - 1);
        assertThat(testBusiness.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testBusiness.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testBusiness.getOwner()).isEqualTo(UPDATED_OWNER);
        assertThat(testBusiness.getContact()).isEqualTo(DEFAULT_CONTACT);
        assertThat(testBusiness.getPhone()).isEqualTo(UPDATED_PHONE);
    }

    @Test
    @Transactional
    void fullUpdateBusinessWithPatch() throws Exception {
        // Initialize the database
        businessRepository.saveAndFlush(business);

        int databaseSizeBeforeUpdate = businessRepository.findAll().size();

        // Update the business using partial update
        Business partialUpdatedBusiness = new Business();
        partialUpdatedBusiness.setId(business.getId());

        partialUpdatedBusiness
            .name(UPDATED_NAME)
            .address(UPDATED_ADDRESS)
            .owner(UPDATED_OWNER)
            .contact(UPDATED_CONTACT)
            .phone(UPDATED_PHONE);

        restBusinessMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBusiness.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBusiness))
            )
            .andExpect(status().isOk());

        // Validate the Business in the database
        List<Business> businessList = businessRepository.findAll();
        assertThat(businessList).hasSize(databaseSizeBeforeUpdate);
        Business testBusiness = businessList.get(businessList.size() - 1);
        assertThat(testBusiness.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testBusiness.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testBusiness.getOwner()).isEqualTo(UPDATED_OWNER);
        assertThat(testBusiness.getContact()).isEqualTo(UPDATED_CONTACT);
        assertThat(testBusiness.getPhone()).isEqualTo(UPDATED_PHONE);
    }

    @Test
    @Transactional
    void patchNonExistingBusiness() throws Exception {
        int databaseSizeBeforeUpdate = businessRepository.findAll().size();
        business.setId(longCount.incrementAndGet());

        // Create the Business
        BusinessDTO businessDTO = businessMapper.toDto(business);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBusinessMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, businessDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(businessDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Business in the database
        List<Business> businessList = businessRepository.findAll();
        assertThat(businessList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBusiness() throws Exception {
        int databaseSizeBeforeUpdate = businessRepository.findAll().size();
        business.setId(longCount.incrementAndGet());

        // Create the Business
        BusinessDTO businessDTO = businessMapper.toDto(business);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBusinessMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(businessDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Business in the database
        List<Business> businessList = businessRepository.findAll();
        assertThat(businessList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBusiness() throws Exception {
        int databaseSizeBeforeUpdate = businessRepository.findAll().size();
        business.setId(longCount.incrementAndGet());

        // Create the Business
        BusinessDTO businessDTO = businessMapper.toDto(business);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBusinessMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(businessDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Business in the database
        List<Business> businessList = businessRepository.findAll();
        assertThat(businessList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBusiness() throws Exception {
        // Initialize the database
        businessRepository.saveAndFlush(business);

        int databaseSizeBeforeDelete = businessRepository.findAll().size();

        // Delete the business
        restBusinessMockMvc
            .perform(delete(ENTITY_API_URL_ID, business.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Business> businessList = businessRepository.findAll();
        assertThat(businessList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
