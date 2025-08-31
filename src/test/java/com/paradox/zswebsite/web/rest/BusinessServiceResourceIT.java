package com.paradox.zswebsite.web.rest;

import static com.paradox.zswebsite.domain.BusinessServiceAsserts.*;
import static com.paradox.zswebsite.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paradox.zswebsite.IntegrationTest;
import com.paradox.zswebsite.domain.BusinessService;
import com.paradox.zswebsite.repository.BusinessServiceRepository;
import com.paradox.zswebsite.service.dto.BusinessServiceDTO;
import com.paradox.zswebsite.service.mapper.BusinessServiceMapper;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
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
 * Integration tests for the {@link BusinessServiceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BusinessServiceResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_ICON = "AAAAAAAAAA";
    private static final String UPDATED_ICON = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/business-services";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private BusinessServiceRepository businessServiceRepository;

    @Autowired
    private BusinessServiceMapper businessServiceMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBusinessServiceMockMvc;

    private BusinessService businessService;

    private BusinessService insertedBusinessService;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BusinessService createEntity() {
        return new BusinessService().title(DEFAULT_TITLE).description(DEFAULT_DESCRIPTION).icon(DEFAULT_ICON);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BusinessService createUpdatedEntity() {
        return new BusinessService().title(UPDATED_TITLE).description(UPDATED_DESCRIPTION).icon(UPDATED_ICON);
    }

    @BeforeEach
    void initTest() {
        businessService = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedBusinessService != null) {
            businessServiceRepository.delete(insertedBusinessService);
            insertedBusinessService = null;
        }
    }

    @Test
    @Transactional
    void createBusinessService() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the BusinessService
        BusinessServiceDTO businessServiceDTO = businessServiceMapper.toDto(businessService);
        var returnedBusinessServiceDTO = om.readValue(
            restBusinessServiceMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(businessServiceDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            BusinessServiceDTO.class
        );

        // Validate the BusinessService in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedBusinessService = businessServiceMapper.toEntity(returnedBusinessServiceDTO);
        assertBusinessServiceUpdatableFieldsEquals(returnedBusinessService, getPersistedBusinessService(returnedBusinessService));

        insertedBusinessService = returnedBusinessService;
    }

    @Test
    @Transactional
    void createBusinessServiceWithExistingId() throws Exception {
        // Create the BusinessService with an existing ID
        businessService.setId(1L);
        BusinessServiceDTO businessServiceDTO = businessServiceMapper.toDto(businessService);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBusinessServiceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(businessServiceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the BusinessService in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        businessService.setTitle(null);

        // Create the BusinessService, which fails.
        BusinessServiceDTO businessServiceDTO = businessServiceMapper.toDto(businessService);

        restBusinessServiceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(businessServiceDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllBusinessServices() throws Exception {
        // Initialize the database
        insertedBusinessService = businessServiceRepository.saveAndFlush(businessService);

        // Get all the businessServiceList
        restBusinessServiceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(businessService.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].icon").value(hasItem(DEFAULT_ICON)));
    }

    @Test
    @Transactional
    void getBusinessService() throws Exception {
        // Initialize the database
        insertedBusinessService = businessServiceRepository.saveAndFlush(businessService);

        // Get the businessService
        restBusinessServiceMockMvc
            .perform(get(ENTITY_API_URL_ID, businessService.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(businessService.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.icon").value(DEFAULT_ICON));
    }

    @Test
    @Transactional
    void getNonExistingBusinessService() throws Exception {
        // Get the businessService
        restBusinessServiceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingBusinessService() throws Exception {
        // Initialize the database
        insertedBusinessService = businessServiceRepository.saveAndFlush(businessService);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the businessService
        BusinessService updatedBusinessService = businessServiceRepository.findById(businessService.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedBusinessService are not directly saved in db
        em.detach(updatedBusinessService);
        updatedBusinessService.title(UPDATED_TITLE).description(UPDATED_DESCRIPTION).icon(UPDATED_ICON);
        BusinessServiceDTO businessServiceDTO = businessServiceMapper.toDto(updatedBusinessService);

        restBusinessServiceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, businessServiceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(businessServiceDTO))
            )
            .andExpect(status().isOk());

        // Validate the BusinessService in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedBusinessServiceToMatchAllProperties(updatedBusinessService);
    }

    @Test
    @Transactional
    void putNonExistingBusinessService() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        businessService.setId(longCount.incrementAndGet());

        // Create the BusinessService
        BusinessServiceDTO businessServiceDTO = businessServiceMapper.toDto(businessService);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBusinessServiceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, businessServiceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(businessServiceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BusinessService in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBusinessService() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        businessService.setId(longCount.incrementAndGet());

        // Create the BusinessService
        BusinessServiceDTO businessServiceDTO = businessServiceMapper.toDto(businessService);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBusinessServiceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(businessServiceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BusinessService in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBusinessService() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        businessService.setId(longCount.incrementAndGet());

        // Create the BusinessService
        BusinessServiceDTO businessServiceDTO = businessServiceMapper.toDto(businessService);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBusinessServiceMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(businessServiceDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the BusinessService in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBusinessServiceWithPatch() throws Exception {
        // Initialize the database
        insertedBusinessService = businessServiceRepository.saveAndFlush(businessService);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the businessService using partial update
        BusinessService partialUpdatedBusinessService = new BusinessService();
        partialUpdatedBusinessService.setId(businessService.getId());

        partialUpdatedBusinessService.title(UPDATED_TITLE).description(UPDATED_DESCRIPTION);

        restBusinessServiceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBusinessService.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBusinessService))
            )
            .andExpect(status().isOk());

        // Validate the BusinessService in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBusinessServiceUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedBusinessService, businessService),
            getPersistedBusinessService(businessService)
        );
    }

    @Test
    @Transactional
    void fullUpdateBusinessServiceWithPatch() throws Exception {
        // Initialize the database
        insertedBusinessService = businessServiceRepository.saveAndFlush(businessService);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the businessService using partial update
        BusinessService partialUpdatedBusinessService = new BusinessService();
        partialUpdatedBusinessService.setId(businessService.getId());

        partialUpdatedBusinessService.title(UPDATED_TITLE).description(UPDATED_DESCRIPTION).icon(UPDATED_ICON);

        restBusinessServiceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBusinessService.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBusinessService))
            )
            .andExpect(status().isOk());

        // Validate the BusinessService in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBusinessServiceUpdatableFieldsEquals(
            partialUpdatedBusinessService,
            getPersistedBusinessService(partialUpdatedBusinessService)
        );
    }

    @Test
    @Transactional
    void patchNonExistingBusinessService() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        businessService.setId(longCount.incrementAndGet());

        // Create the BusinessService
        BusinessServiceDTO businessServiceDTO = businessServiceMapper.toDto(businessService);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBusinessServiceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, businessServiceDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(businessServiceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BusinessService in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBusinessService() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        businessService.setId(longCount.incrementAndGet());

        // Create the BusinessService
        BusinessServiceDTO businessServiceDTO = businessServiceMapper.toDto(businessService);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBusinessServiceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(businessServiceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BusinessService in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBusinessService() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        businessService.setId(longCount.incrementAndGet());

        // Create the BusinessService
        BusinessServiceDTO businessServiceDTO = businessServiceMapper.toDto(businessService);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBusinessServiceMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(businessServiceDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the BusinessService in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBusinessService() throws Exception {
        // Initialize the database
        insertedBusinessService = businessServiceRepository.saveAndFlush(businessService);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the businessService
        restBusinessServiceMockMvc
            .perform(delete(ENTITY_API_URL_ID, businessService.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return businessServiceRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected BusinessService getPersistedBusinessService(BusinessService businessService) {
        return businessServiceRepository.findById(businessService.getId()).orElseThrow();
    }

    protected void assertPersistedBusinessServiceToMatchAllProperties(BusinessService expectedBusinessService) {
        assertBusinessServiceAllPropertiesEquals(expectedBusinessService, getPersistedBusinessService(expectedBusinessService));
    }

    protected void assertPersistedBusinessServiceToMatchUpdatableProperties(BusinessService expectedBusinessService) {
        assertBusinessServiceAllUpdatablePropertiesEquals(expectedBusinessService, getPersistedBusinessService(expectedBusinessService));
    }
}
