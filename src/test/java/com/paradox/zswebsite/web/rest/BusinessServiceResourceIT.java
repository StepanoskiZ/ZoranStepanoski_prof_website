package com.paradox.zswebsite.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.paradox.zswebsite.IntegrationTest;
import com.paradox.zswebsite.domain.BusinessService;
import com.paradox.zswebsite.repository.BusinessServiceRepository;
import com.paradox.zswebsite.service.dto.BusinessServiceDTO;
import com.paradox.zswebsite.service.mapper.BusinessServiceMapper;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
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
    private BusinessServiceRepository businessServiceRepository;

    @Autowired
    private BusinessServiceMapper businessServiceMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBusinessServiceMockMvc;

    private BusinessService businessService;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BusinessService createEntity(EntityManager em) {
        BusinessService businessService = new BusinessService().title(DEFAULT_TITLE).description(DEFAULT_DESCRIPTION).icon(DEFAULT_ICON);
        return businessService;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BusinessService createUpdatedEntity(EntityManager em) {
        BusinessService businessService = new BusinessService().title(UPDATED_TITLE).description(UPDATED_DESCRIPTION).icon(UPDATED_ICON);
        return businessService;
    }

    @BeforeEach
    public void initTest() {
        businessService = createEntity(em);
    }

    @Test
    @Transactional
    void createBusinessService() throws Exception {
        int databaseSizeBeforeCreate = businessServiceRepository.findAll().size();
        // Create the BusinessService
        BusinessServiceDTO businessServiceDTO = businessServiceMapper.toDto(businessService);
        restBusinessServiceMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(businessServiceDTO))
            )
            .andExpect(status().isCreated());

        // Validate the BusinessService in the database
        List<BusinessService> businessServiceList = businessServiceRepository.findAll();
        assertThat(businessServiceList).hasSize(databaseSizeBeforeCreate + 1);
        BusinessService testBusinessService = businessServiceList.get(businessServiceList.size() - 1);
        assertThat(testBusinessService.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testBusinessService.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testBusinessService.getIcon()).isEqualTo(DEFAULT_ICON);
    }

    @Test
    @Transactional
    void createBusinessServiceWithExistingId() throws Exception {
        // Create the BusinessService with an existing ID
        businessService.setId(1L);
        BusinessServiceDTO businessServiceDTO = businessServiceMapper.toDto(businessService);

        int databaseSizeBeforeCreate = businessServiceRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBusinessServiceMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(businessServiceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BusinessService in the database
        List<BusinessService> businessServiceList = businessServiceRepository.findAll();
        assertThat(businessServiceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = businessServiceRepository.findAll().size();
        // set the field null
        businessService.setTitle(null);

        // Create the BusinessService, which fails.
        BusinessServiceDTO businessServiceDTO = businessServiceMapper.toDto(businessService);

        restBusinessServiceMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(businessServiceDTO))
            )
            .andExpect(status().isBadRequest());

        List<BusinessService> businessServiceList = businessServiceRepository.findAll();
        assertThat(businessServiceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllBusinessServices() throws Exception {
        // Initialize the database
        businessServiceRepository.saveAndFlush(businessService);

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
        businessServiceRepository.saveAndFlush(businessService);

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
        businessServiceRepository.saveAndFlush(businessService);

        int databaseSizeBeforeUpdate = businessServiceRepository.findAll().size();

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
                    .content(TestUtil.convertObjectToJsonBytes(businessServiceDTO))
            )
            .andExpect(status().isOk());

        // Validate the BusinessService in the database
        List<BusinessService> businessServiceList = businessServiceRepository.findAll();
        assertThat(businessServiceList).hasSize(databaseSizeBeforeUpdate);
        BusinessService testBusinessService = businessServiceList.get(businessServiceList.size() - 1);
        assertThat(testBusinessService.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testBusinessService.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testBusinessService.getIcon()).isEqualTo(UPDATED_ICON);
    }

    @Test
    @Transactional
    void putNonExistingBusinessService() throws Exception {
        int databaseSizeBeforeUpdate = businessServiceRepository.findAll().size();
        businessService.setId(longCount.incrementAndGet());

        // Create the BusinessService
        BusinessServiceDTO businessServiceDTO = businessServiceMapper.toDto(businessService);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBusinessServiceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, businessServiceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(businessServiceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BusinessService in the database
        List<BusinessService> businessServiceList = businessServiceRepository.findAll();
        assertThat(businessServiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBusinessService() throws Exception {
        int databaseSizeBeforeUpdate = businessServiceRepository.findAll().size();
        businessService.setId(longCount.incrementAndGet());

        // Create the BusinessService
        BusinessServiceDTO businessServiceDTO = businessServiceMapper.toDto(businessService);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBusinessServiceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(businessServiceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BusinessService in the database
        List<BusinessService> businessServiceList = businessServiceRepository.findAll();
        assertThat(businessServiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBusinessService() throws Exception {
        int databaseSizeBeforeUpdate = businessServiceRepository.findAll().size();
        businessService.setId(longCount.incrementAndGet());

        // Create the BusinessService
        BusinessServiceDTO businessServiceDTO = businessServiceMapper.toDto(businessService);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBusinessServiceMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(businessServiceDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the BusinessService in the database
        List<BusinessService> businessServiceList = businessServiceRepository.findAll();
        assertThat(businessServiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBusinessServiceWithPatch() throws Exception {
        // Initialize the database
        businessServiceRepository.saveAndFlush(businessService);

        int databaseSizeBeforeUpdate = businessServiceRepository.findAll().size();

        // Update the businessService using partial update
        BusinessService partialUpdatedBusinessService = new BusinessService();
        partialUpdatedBusinessService.setId(businessService.getId());

        partialUpdatedBusinessService.title(UPDATED_TITLE);

        restBusinessServiceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBusinessService.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBusinessService))
            )
            .andExpect(status().isOk());

        // Validate the BusinessService in the database
        List<BusinessService> businessServiceList = businessServiceRepository.findAll();
        assertThat(businessServiceList).hasSize(databaseSizeBeforeUpdate);
        BusinessService testBusinessService = businessServiceList.get(businessServiceList.size() - 1);
        assertThat(testBusinessService.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testBusinessService.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testBusinessService.getIcon()).isEqualTo(DEFAULT_ICON);
    }

    @Test
    @Transactional
    void fullUpdateBusinessServiceWithPatch() throws Exception {
        // Initialize the database
        businessServiceRepository.saveAndFlush(businessService);

        int databaseSizeBeforeUpdate = businessServiceRepository.findAll().size();

        // Update the businessService using partial update
        BusinessService partialUpdatedBusinessService = new BusinessService();
        partialUpdatedBusinessService.setId(businessService.getId());

        partialUpdatedBusinessService.title(UPDATED_TITLE).description(UPDATED_DESCRIPTION).icon(UPDATED_ICON);

        restBusinessServiceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBusinessService.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBusinessService))
            )
            .andExpect(status().isOk());

        // Validate the BusinessService in the database
        List<BusinessService> businessServiceList = businessServiceRepository.findAll();
        assertThat(businessServiceList).hasSize(databaseSizeBeforeUpdate);
        BusinessService testBusinessService = businessServiceList.get(businessServiceList.size() - 1);
        assertThat(testBusinessService.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testBusinessService.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testBusinessService.getIcon()).isEqualTo(UPDATED_ICON);
    }

    @Test
    @Transactional
    void patchNonExistingBusinessService() throws Exception {
        int databaseSizeBeforeUpdate = businessServiceRepository.findAll().size();
        businessService.setId(longCount.incrementAndGet());

        // Create the BusinessService
        BusinessServiceDTO businessServiceDTO = businessServiceMapper.toDto(businessService);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBusinessServiceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, businessServiceDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(businessServiceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BusinessService in the database
        List<BusinessService> businessServiceList = businessServiceRepository.findAll();
        assertThat(businessServiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBusinessService() throws Exception {
        int databaseSizeBeforeUpdate = businessServiceRepository.findAll().size();
        businessService.setId(longCount.incrementAndGet());

        // Create the BusinessService
        BusinessServiceDTO businessServiceDTO = businessServiceMapper.toDto(businessService);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBusinessServiceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(businessServiceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BusinessService in the database
        List<BusinessService> businessServiceList = businessServiceRepository.findAll();
        assertThat(businessServiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBusinessService() throws Exception {
        int databaseSizeBeforeUpdate = businessServiceRepository.findAll().size();
        businessService.setId(longCount.incrementAndGet());

        // Create the BusinessService
        BusinessServiceDTO businessServiceDTO = businessServiceMapper.toDto(businessService);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBusinessServiceMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(businessServiceDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the BusinessService in the database
        List<BusinessService> businessServiceList = businessServiceRepository.findAll();
        assertThat(businessServiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBusinessService() throws Exception {
        // Initialize the database
        businessServiceRepository.saveAndFlush(businessService);

        int databaseSizeBeforeDelete = businessServiceRepository.findAll().size();

        // Delete the businessService
        restBusinessServiceMockMvc
            .perform(delete(ENTITY_API_URL_ID, businessService.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<BusinessService> businessServiceList = businessServiceRepository.findAll();
        assertThat(businessServiceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
