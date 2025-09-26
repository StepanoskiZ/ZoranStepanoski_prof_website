package com.paradox.zswebsite.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.paradox.zswebsite.IntegrationTest;
import com.paradox.zswebsite.domain.PageContentMedia;
import com.paradox.zswebsite.domain.enumeration.UnifiedMediaType;
import com.paradox.zswebsite.repository.PageContentMediaRepository;
import com.paradox.zswebsite.service.PageContentMediaService;
import com.paradox.zswebsite.service.dto.PageContentMediaDTO;
import com.paradox.zswebsite.service.mapper.PageContentMediaMapper;
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
 * Integration tests for the {@link PageContentMediaResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class PageContentMediaResourceIT {

    private static final String DEFAULT_MEDIA_URL = "AAAAAAAAAA";
    private static final String UPDATED_MEDIA_URL = "BBBBBBBBBB";

    private static final UnifiedMediaType DEFAULT_PAGE_CONTENT_MEDIA_TYPE = UnifiedMediaType.IMAGE;
    private static final UnifiedMediaType UPDATED_PAGE_CONTENT_MEDIA_TYPE = UnifiedMediaType.VIDEO;

    private static final String DEFAULT_CAPTION = "AAAAAAAAAA";
    private static final String UPDATED_CAPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/page-content-medias";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PageContentMediaRepository pageContentMediaRepository;

    @Mock
    private PageContentMediaRepository pageContentMediaRepositoryMock;

    @Autowired
    private PageContentMediaMapper pageContentMediaMapper;

    @Mock
    private PageContentMediaService pageContentMediaServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPageContentMediaMockMvc;

    private PageContentMedia pageContentMedia;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PageContentMedia createEntity(EntityManager em) {
        PageContentMedia pageContentMedia = new PageContentMedia()
            .mediaUrl(DEFAULT_MEDIA_URL)
            .pageContentMediaType(DEFAULT_PAGE_CONTENT_MEDIA_TYPE)
            .caption(DEFAULT_CAPTION);
        return pageContentMedia;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PageContentMedia createUpdatedEntity(EntityManager em) {
        PageContentMedia pageContentMedia = new PageContentMedia()
            .mediaUrl(UPDATED_MEDIA_URL)
            .pageContentMediaType(UPDATED_PAGE_CONTENT_MEDIA_TYPE)
            .caption(UPDATED_CAPTION);
        return pageContentMedia;
    }

    @BeforeEach
    public void initTest() {
        pageContentMedia = createEntity(em);
    }

    @Test
    @Transactional
    void createPageContentMedia() throws Exception {
        int databaseSizeBeforeCreate = pageContentMediaRepository.findAll().size();
        // Create the PageContentMedia
        PageContentMediaDTO pageContentMediaDTO = pageContentMediaMapper.toDto(pageContentMedia);
        restPageContentMediaMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pageContentMediaDTO))
            )
            .andExpect(status().isCreated());

        // Validate the PageContentMedia in the database
        List<PageContentMedia> pageContentMediaList = pageContentMediaRepository.findAll();
        assertThat(pageContentMediaList).hasSize(databaseSizeBeforeCreate + 1);
        PageContentMedia testPageContentMedia = pageContentMediaList.get(pageContentMediaList.size() - 1);
        assertThat(testPageContentMedia.getMediaUrl()).isEqualTo(DEFAULT_MEDIA_URL);
        assertThat(testPageContentMedia.getPageContentMediaType()).isEqualTo(DEFAULT_PAGE_CONTENT_MEDIA_TYPE);
        assertThat(testPageContentMedia.getCaption()).isEqualTo(DEFAULT_CAPTION);
    }

    @Test
    @Transactional
    void createPageContentMediaWithExistingId() throws Exception {
        // Create the PageContentMedia with an existing ID
        pageContentMedia.setId(1L);
        PageContentMediaDTO pageContentMediaDTO = pageContentMediaMapper.toDto(pageContentMedia);

        int databaseSizeBeforeCreate = pageContentMediaRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPageContentMediaMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pageContentMediaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PageContentMedia in the database
        List<PageContentMedia> pageContentMediaList = pageContentMediaRepository.findAll();
        assertThat(pageContentMediaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkMediaUrlIsRequired() throws Exception {
        int databaseSizeBeforeTest = pageContentMediaRepository.findAll().size();
        // set the field null
        pageContentMedia.setMediaUrl(null);

        // Create the PageContentMedia, which fails.
        PageContentMediaDTO pageContentMediaDTO = pageContentMediaMapper.toDto(pageContentMedia);

        restPageContentMediaMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pageContentMediaDTO))
            )
            .andExpect(status().isBadRequest());

        List<PageContentMedia> pageContentMediaList = pageContentMediaRepository.findAll();
        assertThat(pageContentMediaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPageContentMediaTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = pageContentMediaRepository.findAll().size();
        // set the field null
        pageContentMedia.setPageContentMediaType(null);

        // Create the PageContentMedia, which fails.
        PageContentMediaDTO pageContentMediaDTO = pageContentMediaMapper.toDto(pageContentMedia);

        restPageContentMediaMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pageContentMediaDTO))
            )
            .andExpect(status().isBadRequest());

        List<PageContentMedia> pageContentMediaList = pageContentMediaRepository.findAll();
        assertThat(pageContentMediaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPageContentMedias() throws Exception {
        // Initialize the database
        pageContentMediaRepository.saveAndFlush(pageContentMedia);

        // Get all the pageContentMediaList
        restPageContentMediaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pageContentMedia.getId().intValue())))
            .andExpect(jsonPath("$.[*].mediaUrl").value(hasItem(DEFAULT_MEDIA_URL)))
            .andExpect(jsonPath("$.[*].pageContentMediaType").value(hasItem(DEFAULT_PAGE_CONTENT_MEDIA_TYPE.toString())))
            .andExpect(jsonPath("$.[*].caption").value(hasItem(DEFAULT_CAPTION)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPageContentMediasWithEagerRelationshipsIsEnabled() throws Exception {
        when(pageContentMediaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPageContentMediaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(pageContentMediaServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPageContentMediasWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(pageContentMediaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPageContentMediaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(pageContentMediaRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getPageContentMedia() throws Exception {
        // Initialize the database
        pageContentMediaRepository.saveAndFlush(pageContentMedia);

        // Get the pageContentMedia
        restPageContentMediaMockMvc
            .perform(get(ENTITY_API_URL_ID, pageContentMedia.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(pageContentMedia.getId().intValue()))
            .andExpect(jsonPath("$.mediaUrl").value(DEFAULT_MEDIA_URL))
            .andExpect(jsonPath("$.pageContentMediaType").value(DEFAULT_PAGE_CONTENT_MEDIA_TYPE.toString()))
            .andExpect(jsonPath("$.caption").value(DEFAULT_CAPTION));
    }

    @Test
    @Transactional
    void getNonExistingPageContentMedia() throws Exception {
        // Get the pageContentMedia
        restPageContentMediaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPageContentMedia() throws Exception {
        // Initialize the database
        pageContentMediaRepository.saveAndFlush(pageContentMedia);

        int databaseSizeBeforeUpdate = pageContentMediaRepository.findAll().size();

        // Update the pageContentMedia
        PageContentMedia updatedPageContentMedia = pageContentMediaRepository.findById(pageContentMedia.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPageContentMedia are not directly saved in db
        em.detach(updatedPageContentMedia);
        updatedPageContentMedia.mediaUrl(UPDATED_MEDIA_URL).pageContentMediaType(UPDATED_PAGE_CONTENT_MEDIA_TYPE).caption(UPDATED_CAPTION);
        PageContentMediaDTO pageContentMediaDTO = pageContentMediaMapper.toDto(updatedPageContentMedia);

        restPageContentMediaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pageContentMediaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pageContentMediaDTO))
            )
            .andExpect(status().isOk());

        // Validate the PageContentMedia in the database
        List<PageContentMedia> pageContentMediaList = pageContentMediaRepository.findAll();
        assertThat(pageContentMediaList).hasSize(databaseSizeBeforeUpdate);
        PageContentMedia testPageContentMedia = pageContentMediaList.get(pageContentMediaList.size() - 1);
        assertThat(testPageContentMedia.getMediaUrl()).isEqualTo(UPDATED_MEDIA_URL);
        assertThat(testPageContentMedia.getPageContentMediaType()).isEqualTo(UPDATED_PAGE_CONTENT_MEDIA_TYPE);
        assertThat(testPageContentMedia.getCaption()).isEqualTo(UPDATED_CAPTION);
    }

    @Test
    @Transactional
    void putNonExistingPageContentMedia() throws Exception {
        int databaseSizeBeforeUpdate = pageContentMediaRepository.findAll().size();
        pageContentMedia.setId(longCount.incrementAndGet());

        // Create the PageContentMedia
        PageContentMediaDTO pageContentMediaDTO = pageContentMediaMapper.toDto(pageContentMedia);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPageContentMediaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pageContentMediaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pageContentMediaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PageContentMedia in the database
        List<PageContentMedia> pageContentMediaList = pageContentMediaRepository.findAll();
        assertThat(pageContentMediaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPageContentMedia() throws Exception {
        int databaseSizeBeforeUpdate = pageContentMediaRepository.findAll().size();
        pageContentMedia.setId(longCount.incrementAndGet());

        // Create the PageContentMedia
        PageContentMediaDTO pageContentMediaDTO = pageContentMediaMapper.toDto(pageContentMedia);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPageContentMediaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pageContentMediaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PageContentMedia in the database
        List<PageContentMedia> pageContentMediaList = pageContentMediaRepository.findAll();
        assertThat(pageContentMediaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPageContentMedia() throws Exception {
        int databaseSizeBeforeUpdate = pageContentMediaRepository.findAll().size();
        pageContentMedia.setId(longCount.incrementAndGet());

        // Create the PageContentMedia
        PageContentMediaDTO pageContentMediaDTO = pageContentMediaMapper.toDto(pageContentMedia);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPageContentMediaMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pageContentMediaDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PageContentMedia in the database
        List<PageContentMedia> pageContentMediaList = pageContentMediaRepository.findAll();
        assertThat(pageContentMediaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePageContentMediaWithPatch() throws Exception {
        // Initialize the database
        pageContentMediaRepository.saveAndFlush(pageContentMedia);

        int databaseSizeBeforeUpdate = pageContentMediaRepository.findAll().size();

        // Update the pageContentMedia using partial update
        PageContentMedia partialUpdatedPageContentMedia = new PageContentMedia();
        partialUpdatedPageContentMedia.setId(pageContentMedia.getId());

        restPageContentMediaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPageContentMedia.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPageContentMedia))
            )
            .andExpect(status().isOk());

        // Validate the PageContentMedia in the database
        List<PageContentMedia> pageContentMediaList = pageContentMediaRepository.findAll();
        assertThat(pageContentMediaList).hasSize(databaseSizeBeforeUpdate);
        PageContentMedia testPageContentMedia = pageContentMediaList.get(pageContentMediaList.size() - 1);
        assertThat(testPageContentMedia.getMediaUrl()).isEqualTo(DEFAULT_MEDIA_URL);
        assertThat(testPageContentMedia.getPageContentMediaType()).isEqualTo(DEFAULT_PAGE_CONTENT_MEDIA_TYPE);
        assertThat(testPageContentMedia.getCaption()).isEqualTo(DEFAULT_CAPTION);
    }

    @Test
    @Transactional
    void fullUpdatePageContentMediaWithPatch() throws Exception {
        // Initialize the database
        pageContentMediaRepository.saveAndFlush(pageContentMedia);

        int databaseSizeBeforeUpdate = pageContentMediaRepository.findAll().size();

        // Update the pageContentMedia using partial update
        PageContentMedia partialUpdatedPageContentMedia = new PageContentMedia();
        partialUpdatedPageContentMedia.setId(pageContentMedia.getId());

        partialUpdatedPageContentMedia
            .mediaUrl(UPDATED_MEDIA_URL)
            .pageContentMediaType(UPDATED_PAGE_CONTENT_MEDIA_TYPE)
            .caption(UPDATED_CAPTION);

        restPageContentMediaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPageContentMedia.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPageContentMedia))
            )
            .andExpect(status().isOk());

        // Validate the PageContentMedia in the database
        List<PageContentMedia> pageContentMediaList = pageContentMediaRepository.findAll();
        assertThat(pageContentMediaList).hasSize(databaseSizeBeforeUpdate);
        PageContentMedia testPageContentMedia = pageContentMediaList.get(pageContentMediaList.size() - 1);
        assertThat(testPageContentMedia.getMediaUrl()).isEqualTo(UPDATED_MEDIA_URL);
        assertThat(testPageContentMedia.getPageContentMediaType()).isEqualTo(UPDATED_PAGE_CONTENT_MEDIA_TYPE);
        assertThat(testPageContentMedia.getCaption()).isEqualTo(UPDATED_CAPTION);
    }

    @Test
    @Transactional
    void patchNonExistingPageContentMedia() throws Exception {
        int databaseSizeBeforeUpdate = pageContentMediaRepository.findAll().size();
        pageContentMedia.setId(longCount.incrementAndGet());

        // Create the PageContentMedia
        PageContentMediaDTO pageContentMediaDTO = pageContentMediaMapper.toDto(pageContentMedia);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPageContentMediaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, pageContentMediaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pageContentMediaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PageContentMedia in the database
        List<PageContentMedia> pageContentMediaList = pageContentMediaRepository.findAll();
        assertThat(pageContentMediaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPageContentMedia() throws Exception {
        int databaseSizeBeforeUpdate = pageContentMediaRepository.findAll().size();
        pageContentMedia.setId(longCount.incrementAndGet());

        // Create the PageContentMedia
        PageContentMediaDTO pageContentMediaDTO = pageContentMediaMapper.toDto(pageContentMedia);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPageContentMediaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pageContentMediaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PageContentMedia in the database
        List<PageContentMedia> pageContentMediaList = pageContentMediaRepository.findAll();
        assertThat(pageContentMediaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPageContentMedia() throws Exception {
        int databaseSizeBeforeUpdate = pageContentMediaRepository.findAll().size();
        pageContentMedia.setId(longCount.incrementAndGet());

        // Create the PageContentMedia
        PageContentMediaDTO pageContentMediaDTO = pageContentMediaMapper.toDto(pageContentMedia);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPageContentMediaMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pageContentMediaDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PageContentMedia in the database
        List<PageContentMedia> pageContentMediaList = pageContentMediaRepository.findAll();
        assertThat(pageContentMediaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePageContentMedia() throws Exception {
        // Initialize the database
        pageContentMediaRepository.saveAndFlush(pageContentMedia);

        int databaseSizeBeforeDelete = pageContentMediaRepository.findAll().size();

        // Delete the pageContentMedia
        restPageContentMediaMockMvc
            .perform(delete(ENTITY_API_URL_ID, pageContentMedia.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PageContentMedia> pageContentMediaList = pageContentMediaRepository.findAll();
        assertThat(pageContentMediaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
