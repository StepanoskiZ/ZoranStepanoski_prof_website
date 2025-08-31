package com.paradox.zswebsite.web.rest;

import static com.paradox.zswebsite.domain.ProjectImageAsserts.*;
import static com.paradox.zswebsite.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paradox.zswebsite.IntegrationTest;
import com.paradox.zswebsite.domain.ProjectImage;
import com.paradox.zswebsite.repository.ProjectImageRepository;
import com.paradox.zswebsite.service.dto.ProjectImageDTO;
import com.paradox.zswebsite.service.mapper.ProjectImageMapper;
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
 * Integration tests for the {@link ProjectImageResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProjectImageResourceIT {

    private static final String DEFAULT_IMAGE_URL = "AAAAAAAAAA";
    private static final String UPDATED_IMAGE_URL = "BBBBBBBBBB";

    private static final String DEFAULT_CAPTION = "AAAAAAAAAA";
    private static final String UPDATED_CAPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/project-images";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ProjectImageRepository projectImageRepository;

    @Autowired
    private ProjectImageMapper projectImageMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProjectImageMockMvc;

    private ProjectImage projectImage;

    private ProjectImage insertedProjectImage;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProjectImage createEntity() {
        return new ProjectImage().imageUrl(DEFAULT_IMAGE_URL).caption(DEFAULT_CAPTION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProjectImage createUpdatedEntity() {
        return new ProjectImage().imageUrl(UPDATED_IMAGE_URL).caption(UPDATED_CAPTION);
    }

    @BeforeEach
    void initTest() {
        projectImage = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedProjectImage != null) {
            projectImageRepository.delete(insertedProjectImage);
            insertedProjectImage = null;
        }
    }

    @Test
    @Transactional
    void createProjectImage() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ProjectImage
        ProjectImageDTO projectImageDTO = projectImageMapper.toDto(projectImage);
        var returnedProjectImageDTO = om.readValue(
            restProjectImageMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(projectImageDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ProjectImageDTO.class
        );

        // Validate the ProjectImage in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedProjectImage = projectImageMapper.toEntity(returnedProjectImageDTO);
        assertProjectImageUpdatableFieldsEquals(returnedProjectImage, getPersistedProjectImage(returnedProjectImage));

        insertedProjectImage = returnedProjectImage;
    }

    @Test
    @Transactional
    void createProjectImageWithExistingId() throws Exception {
        // Create the ProjectImage with an existing ID
        projectImage.setId(1L);
        ProjectImageDTO projectImageDTO = projectImageMapper.toDto(projectImage);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProjectImageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(projectImageDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ProjectImage in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkImageUrlIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        projectImage.setImageUrl(null);

        // Create the ProjectImage, which fails.
        ProjectImageDTO projectImageDTO = projectImageMapper.toDto(projectImage);

        restProjectImageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(projectImageDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProjectImages() throws Exception {
        // Initialize the database
        insertedProjectImage = projectImageRepository.saveAndFlush(projectImage);

        // Get all the projectImageList
        restProjectImageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(projectImage.getId().intValue())))
            .andExpect(jsonPath("$.[*].imageUrl").value(hasItem(DEFAULT_IMAGE_URL)))
            .andExpect(jsonPath("$.[*].caption").value(hasItem(DEFAULT_CAPTION)));
    }

    @Test
    @Transactional
    void getProjectImage() throws Exception {
        // Initialize the database
        insertedProjectImage = projectImageRepository.saveAndFlush(projectImage);

        // Get the projectImage
        restProjectImageMockMvc
            .perform(get(ENTITY_API_URL_ID, projectImage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(projectImage.getId().intValue()))
            .andExpect(jsonPath("$.imageUrl").value(DEFAULT_IMAGE_URL))
            .andExpect(jsonPath("$.caption").value(DEFAULT_CAPTION));
    }

    @Test
    @Transactional
    void getNonExistingProjectImage() throws Exception {
        // Get the projectImage
        restProjectImageMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProjectImage() throws Exception {
        // Initialize the database
        insertedProjectImage = projectImageRepository.saveAndFlush(projectImage);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the projectImage
        ProjectImage updatedProjectImage = projectImageRepository.findById(projectImage.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedProjectImage are not directly saved in db
        em.detach(updatedProjectImage);
        updatedProjectImage.imageUrl(UPDATED_IMAGE_URL).caption(UPDATED_CAPTION);
        ProjectImageDTO projectImageDTO = projectImageMapper.toDto(updatedProjectImage);

        restProjectImageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, projectImageDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(projectImageDTO))
            )
            .andExpect(status().isOk());

        // Validate the ProjectImage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedProjectImageToMatchAllProperties(updatedProjectImage);
    }

    @Test
    @Transactional
    void putNonExistingProjectImage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        projectImage.setId(longCount.incrementAndGet());

        // Create the ProjectImage
        ProjectImageDTO projectImageDTO = projectImageMapper.toDto(projectImage);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProjectImageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, projectImageDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(projectImageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProjectImage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProjectImage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        projectImage.setId(longCount.incrementAndGet());

        // Create the ProjectImage
        ProjectImageDTO projectImageDTO = projectImageMapper.toDto(projectImage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProjectImageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(projectImageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProjectImage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProjectImage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        projectImage.setId(longCount.incrementAndGet());

        // Create the ProjectImage
        ProjectImageDTO projectImageDTO = projectImageMapper.toDto(projectImage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProjectImageMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(projectImageDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProjectImage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProjectImageWithPatch() throws Exception {
        // Initialize the database
        insertedProjectImage = projectImageRepository.saveAndFlush(projectImage);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the projectImage using partial update
        ProjectImage partialUpdatedProjectImage = new ProjectImage();
        partialUpdatedProjectImage.setId(projectImage.getId());

        partialUpdatedProjectImage.imageUrl(UPDATED_IMAGE_URL).caption(UPDATED_CAPTION);

        restProjectImageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProjectImage.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProjectImage))
            )
            .andExpect(status().isOk());

        // Validate the ProjectImage in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProjectImageUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedProjectImage, projectImage),
            getPersistedProjectImage(projectImage)
        );
    }

    @Test
    @Transactional
    void fullUpdateProjectImageWithPatch() throws Exception {
        // Initialize the database
        insertedProjectImage = projectImageRepository.saveAndFlush(projectImage);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the projectImage using partial update
        ProjectImage partialUpdatedProjectImage = new ProjectImage();
        partialUpdatedProjectImage.setId(projectImage.getId());

        partialUpdatedProjectImage.imageUrl(UPDATED_IMAGE_URL).caption(UPDATED_CAPTION);

        restProjectImageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProjectImage.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProjectImage))
            )
            .andExpect(status().isOk());

        // Validate the ProjectImage in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProjectImageUpdatableFieldsEquals(partialUpdatedProjectImage, getPersistedProjectImage(partialUpdatedProjectImage));
    }

    @Test
    @Transactional
    void patchNonExistingProjectImage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        projectImage.setId(longCount.incrementAndGet());

        // Create the ProjectImage
        ProjectImageDTO projectImageDTO = projectImageMapper.toDto(projectImage);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProjectImageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, projectImageDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(projectImageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProjectImage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProjectImage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        projectImage.setId(longCount.incrementAndGet());

        // Create the ProjectImage
        ProjectImageDTO projectImageDTO = projectImageMapper.toDto(projectImage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProjectImageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(projectImageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProjectImage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProjectImage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        projectImage.setId(longCount.incrementAndGet());

        // Create the ProjectImage
        ProjectImageDTO projectImageDTO = projectImageMapper.toDto(projectImage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProjectImageMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(projectImageDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProjectImage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProjectImage() throws Exception {
        // Initialize the database
        insertedProjectImage = projectImageRepository.saveAndFlush(projectImage);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the projectImage
        restProjectImageMockMvc
            .perform(delete(ENTITY_API_URL_ID, projectImage.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return projectImageRepository.count();
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

    protected ProjectImage getPersistedProjectImage(ProjectImage projectImage) {
        return projectImageRepository.findById(projectImage.getId()).orElseThrow();
    }

    protected void assertPersistedProjectImageToMatchAllProperties(ProjectImage expectedProjectImage) {
        assertProjectImageAllPropertiesEquals(expectedProjectImage, getPersistedProjectImage(expectedProjectImage));
    }

    protected void assertPersistedProjectImageToMatchUpdatableProperties(ProjectImage expectedProjectImage) {
        assertProjectImageAllUpdatablePropertiesEquals(expectedProjectImage, getPersistedProjectImage(expectedProjectImage));
    }
}
