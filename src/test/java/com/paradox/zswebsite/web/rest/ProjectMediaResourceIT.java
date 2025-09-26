package com.paradox.zswebsite.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.paradox.zswebsite.IntegrationTest;
import com.paradox.zswebsite.domain.ProjectMedia;
import com.paradox.zswebsite.domain.enumeration.UnifiedMediaType;
import com.paradox.zswebsite.repository.ProjectMediaRepository;
import com.paradox.zswebsite.service.ProjectMediaService;
import com.paradox.zswebsite.service.dto.ProjectMediaDTO;
import com.paradox.zswebsite.service.mapper.ProjectMediaMapper;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
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
 * Integration tests for the {@link ProjectMediaResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ProjectMediaResourceIT {

    private static final String DEFAULT_MEDIA_URL = "AAAAAAAAAA";
    private static final String UPDATED_MEDIA_URL = "BBBBBBBBBB";

    private static final UnifiedMediaType DEFAULT_PROJECT_MEDIA_TYPE = UnifiedMediaType.IMAGE;
    private static final UnifiedMediaType UPDATED_PROJECT_MEDIA_TYPE = UnifiedMediaType.VIDEO;

    private static final String DEFAULT_CAPTION = "AAAAAAAAAA";
    private static final String UPDATED_CAPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/project-medias";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ProjectMediaRepository projectMediaRepository;

    @Mock
    private ProjectMediaRepository projectMediaRepositoryMock;

    @Autowired
    private ProjectMediaMapper projectMediaMapper;

    @Mock
    private ProjectMediaService projectMediaServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProjectMediaMockMvc;

    private ProjectMedia projectMedia;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProjectMedia createEntity(EntityManager em) {
        ProjectMedia projectMedia = new ProjectMedia()
            .mediaUrl(DEFAULT_MEDIA_URL)
            .projectMediaType(DEFAULT_PROJECT_MEDIA_TYPE)
            .caption(DEFAULT_CAPTION);
        return projectMedia;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProjectMedia createUpdatedEntity(EntityManager em) {
        ProjectMedia projectMedia = new ProjectMedia()
            .mediaUrl(UPDATED_MEDIA_URL)
            .projectMediaType(UPDATED_PROJECT_MEDIA_TYPE)
            .caption(UPDATED_CAPTION);
        return projectMedia;
    }

    @BeforeEach
    public void initTest() {
        projectMedia = createEntity(em);
    }

    @Test
    @Transactional
    void createProjectMedia() throws Exception {
        int databaseSizeBeforeCreate = projectMediaRepository.findAll().size();
        // Create the ProjectMedia
        ProjectMediaDTO projectMediaDTO = projectMediaMapper.toDto(projectMedia);
        restProjectMediaMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(projectMediaDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ProjectMedia in the database
        List<ProjectMedia> projectMediaList = projectMediaRepository.findAll();
        assertThat(projectMediaList).hasSize(databaseSizeBeforeCreate + 1);
        ProjectMedia testProjectMedia = projectMediaList.get(projectMediaList.size() - 1);
        assertThat(testProjectMedia.getMediaUrl()).isEqualTo(DEFAULT_MEDIA_URL);
        assertThat(testProjectMedia.getProjectMediaType()).isEqualTo(DEFAULT_PROJECT_MEDIA_TYPE);
        assertThat(testProjectMedia.getCaption()).isEqualTo(DEFAULT_CAPTION);
    }

    @Test
    @Transactional
    void createProjectMediaWithExistingId() throws Exception {
        // Create the ProjectMedia with an existing ID
        projectMedia.setId(1L);
        ProjectMediaDTO projectMediaDTO = projectMediaMapper.toDto(projectMedia);

        int databaseSizeBeforeCreate = projectMediaRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProjectMediaMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(projectMediaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProjectMedia in the database
        List<ProjectMedia> projectMediaList = projectMediaRepository.findAll();
        assertThat(projectMediaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkMediaUrlIsRequired() throws Exception {
        int databaseSizeBeforeTest = projectMediaRepository.findAll().size();
        // set the field null
        projectMedia.setMediaUrl(null);

        // Create the ProjectMedia, which fails.
        ProjectMediaDTO projectMediaDTO = projectMediaMapper.toDto(projectMedia);

        restProjectMediaMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(projectMediaDTO))
            )
            .andExpect(status().isBadRequest());

        List<ProjectMedia> projectMediaList = projectMediaRepository.findAll();
        assertThat(projectMediaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkProjectMediaTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = projectMediaRepository.findAll().size();
        // set the field null
        projectMedia.setProjectMediaType(null);

        // Create the ProjectMedia, which fails.
        ProjectMediaDTO projectMediaDTO = projectMediaMapper.toDto(projectMedia);

        restProjectMediaMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(projectMediaDTO))
            )
            .andExpect(status().isBadRequest());

        List<ProjectMedia> projectMediaList = projectMediaRepository.findAll();
        assertThat(projectMediaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProjectMedias() throws Exception {
        // Initialize the database
        projectMediaRepository.saveAndFlush(projectMedia);

        // Get all the projectMediaList
        restProjectMediaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(projectMedia.getId().intValue())))
            .andExpect(jsonPath("$.[*].mediaUrl").value(hasItem(DEFAULT_MEDIA_URL)))
            .andExpect(jsonPath("$.[*].projectMediaType").value(hasItem(DEFAULT_PROJECT_MEDIA_TYPE.toString())))
            .andExpect(jsonPath("$.[*].caption").value(hasItem(DEFAULT_CAPTION)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProjectMediasWithEagerRelationshipsIsEnabled() throws Exception {
        when(projectMediaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProjectMediaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(projectMediaServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProjectMediasWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(projectMediaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProjectMediaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(projectMediaRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getProjectMedia() throws Exception {
        // Initialize the database
        projectMediaRepository.saveAndFlush(projectMedia);

        // Get the projectMedia
        restProjectMediaMockMvc
            .perform(get(ENTITY_API_URL_ID, projectMedia.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(projectMedia.getId().intValue()))
            .andExpect(jsonPath("$.mediaUrl").value(DEFAULT_MEDIA_URL))
            .andExpect(jsonPath("$.projectMediaType").value(DEFAULT_PROJECT_MEDIA_TYPE.toString()))
            .andExpect(jsonPath("$.caption").value(DEFAULT_CAPTION));
    }

    @Test
    @Transactional
    void getNonExistingProjectMedia() throws Exception {
        // Get the projectMedia
        restProjectMediaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProjectMedia() throws Exception {
        // Initialize the database
        projectMediaRepository.saveAndFlush(projectMedia);

        int databaseSizeBeforeUpdate = projectMediaRepository.findAll().size();

        // Update the projectMedia
        ProjectMedia updatedProjectMedia = projectMediaRepository.findById(projectMedia.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedProjectMedia are not directly saved in db
        em.detach(updatedProjectMedia);
        updatedProjectMedia.mediaUrl(UPDATED_MEDIA_URL).projectMediaType(UPDATED_PROJECT_MEDIA_TYPE).caption(UPDATED_CAPTION);
        ProjectMediaDTO projectMediaDTO = projectMediaMapper.toDto(updatedProjectMedia);

        restProjectMediaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, projectMediaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(projectMediaDTO))
            )
            .andExpect(status().isOk());

        // Validate the ProjectMedia in the database
        List<ProjectMedia> projectMediaList = projectMediaRepository.findAll();
        assertThat(projectMediaList).hasSize(databaseSizeBeforeUpdate);
        ProjectMedia testProjectMedia = projectMediaList.get(projectMediaList.size() - 1);
        assertThat(testProjectMedia.getMediaUrl()).isEqualTo(UPDATED_MEDIA_URL);
        assertThat(testProjectMedia.getProjectMediaType()).isEqualTo(UPDATED_PROJECT_MEDIA_TYPE);
        assertThat(testProjectMedia.getCaption()).isEqualTo(UPDATED_CAPTION);
    }

    @Test
    @Transactional
    void putNonExistingProjectMedia() throws Exception {
        int databaseSizeBeforeUpdate = projectMediaRepository.findAll().size();
        projectMedia.setId(longCount.incrementAndGet());

        // Create the ProjectMedia
        ProjectMediaDTO projectMediaDTO = projectMediaMapper.toDto(projectMedia);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProjectMediaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, projectMediaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(projectMediaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProjectMedia in the database
        List<ProjectMedia> projectMediaList = projectMediaRepository.findAll();
        assertThat(projectMediaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProjectMedia() throws Exception {
        int databaseSizeBeforeUpdate = projectMediaRepository.findAll().size();
        projectMedia.setId(longCount.incrementAndGet());

        // Create the ProjectMedia
        ProjectMediaDTO projectMediaDTO = projectMediaMapper.toDto(projectMedia);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProjectMediaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(projectMediaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProjectMedia in the database
        List<ProjectMedia> projectMediaList = projectMediaRepository.findAll();
        assertThat(projectMediaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProjectMedia() throws Exception {
        int databaseSizeBeforeUpdate = projectMediaRepository.findAll().size();
        projectMedia.setId(longCount.incrementAndGet());

        // Create the ProjectMedia
        ProjectMediaDTO projectMediaDTO = projectMediaMapper.toDto(projectMedia);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProjectMediaMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(projectMediaDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProjectMedia in the database
        List<ProjectMedia> projectMediaList = projectMediaRepository.findAll();
        assertThat(projectMediaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProjectMediaWithPatch() throws Exception {
        // Initialize the database
        projectMediaRepository.saveAndFlush(projectMedia);

        int databaseSizeBeforeUpdate = projectMediaRepository.findAll().size();

        // Update the projectMedia using partial update
        ProjectMedia partialUpdatedProjectMedia = new ProjectMedia();
        partialUpdatedProjectMedia.setId(projectMedia.getId());

        partialUpdatedProjectMedia.mediaUrl(UPDATED_MEDIA_URL).caption(UPDATED_CAPTION);

        restProjectMediaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProjectMedia.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProjectMedia))
            )
            .andExpect(status().isOk());

        // Validate the ProjectMedia in the database
        List<ProjectMedia> projectMediaList = projectMediaRepository.findAll();
        assertThat(projectMediaList).hasSize(databaseSizeBeforeUpdate);
        ProjectMedia testProjectMedia = projectMediaList.get(projectMediaList.size() - 1);
        assertThat(testProjectMedia.getMediaUrl()).isEqualTo(UPDATED_MEDIA_URL);
        assertThat(testProjectMedia.getProjectMediaType()).isEqualTo(DEFAULT_PROJECT_MEDIA_TYPE);
        assertThat(testProjectMedia.getCaption()).isEqualTo(UPDATED_CAPTION);
    }

    @Test
    @Transactional
    void fullUpdateProjectMediaWithPatch() throws Exception {
        // Initialize the database
        projectMediaRepository.saveAndFlush(projectMedia);

        int databaseSizeBeforeUpdate = projectMediaRepository.findAll().size();

        // Update the projectMedia using partial update
        ProjectMedia partialUpdatedProjectMedia = new ProjectMedia();
        partialUpdatedProjectMedia.setId(projectMedia.getId());

        partialUpdatedProjectMedia.mediaUrl(UPDATED_MEDIA_URL).projectMediaType(UPDATED_PROJECT_MEDIA_TYPE).caption(UPDATED_CAPTION);

        restProjectMediaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProjectMedia.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProjectMedia))
            )
            .andExpect(status().isOk());

        // Validate the ProjectMedia in the database
        List<ProjectMedia> projectMediaList = projectMediaRepository.findAll();
        assertThat(projectMediaList).hasSize(databaseSizeBeforeUpdate);
        ProjectMedia testProjectMedia = projectMediaList.get(projectMediaList.size() - 1);
        assertThat(testProjectMedia.getMediaUrl()).isEqualTo(UPDATED_MEDIA_URL);
        assertThat(testProjectMedia.getProjectMediaType()).isEqualTo(UPDATED_PROJECT_MEDIA_TYPE);
        assertThat(testProjectMedia.getCaption()).isEqualTo(UPDATED_CAPTION);
    }

    @Test
    @Transactional
    void patchNonExistingProjectMedia() throws Exception {
        int databaseSizeBeforeUpdate = projectMediaRepository.findAll().size();
        projectMedia.setId(longCount.incrementAndGet());

        // Create the ProjectMedia
        ProjectMediaDTO projectMediaDTO = projectMediaMapper.toDto(projectMedia);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProjectMediaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, projectMediaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(projectMediaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProjectMedia in the database
        List<ProjectMedia> projectMediaList = projectMediaRepository.findAll();
        assertThat(projectMediaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProjectMedia() throws Exception {
        int databaseSizeBeforeUpdate = projectMediaRepository.findAll().size();
        projectMedia.setId(longCount.incrementAndGet());

        // Create the ProjectMedia
        ProjectMediaDTO projectMediaDTO = projectMediaMapper.toDto(projectMedia);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProjectMediaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(projectMediaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProjectMedia in the database
        List<ProjectMedia> projectMediaList = projectMediaRepository.findAll();
        assertThat(projectMediaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProjectMedia() throws Exception {
        int databaseSizeBeforeUpdate = projectMediaRepository.findAll().size();
        projectMedia.setId(longCount.incrementAndGet());

        // Create the ProjectMedia
        ProjectMediaDTO projectMediaDTO = projectMediaMapper.toDto(projectMedia);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProjectMediaMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(projectMediaDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProjectMedia in the database
        List<ProjectMedia> projectMediaList = projectMediaRepository.findAll();
        assertThat(projectMediaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProjectMedia() throws Exception {
        // Initialize the database
        projectMediaRepository.saveAndFlush(projectMedia);

        int databaseSizeBeforeDelete = projectMediaRepository.findAll().size();

        // Delete the projectMedia
        restProjectMediaMockMvc
            .perform(delete(ENTITY_API_URL_ID, projectMedia.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ProjectMedia> projectMediaList = projectMediaRepository.findAll();
        assertThat(projectMediaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
