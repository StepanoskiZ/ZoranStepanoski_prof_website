package com.paradox.zswebsite.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.paradox.zswebsite.IntegrationTest;
import com.paradox.zswebsite.domain.PageContent;
import com.paradox.zswebsite.repository.PageContentRepository;
import com.paradox.zswebsite.service.dto.PageContentDTO;
import com.paradox.zswebsite.service.mapper.PageContentMapper;
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
 * Integration tests for the {@link PageContentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PageContentResourceIT {

    private static final String DEFAULT_SECTION_KEY = "AAAAAAAAAA";
    private static final String UPDATED_SECTION_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_CONTENT_HTML = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT_HTML = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/page-contents";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PageContentRepository pageContentRepository;

    @Autowired
    private PageContentMapper pageContentMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPageContentMockMvc;

    private PageContent pageContent;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PageContent createEntity(EntityManager em) {
        PageContent pageContent = new PageContent().sectionKey(DEFAULT_SECTION_KEY).contentHtml(DEFAULT_CONTENT_HTML);
        return pageContent;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PageContent createUpdatedEntity(EntityManager em) {
        PageContent pageContent = new PageContent().sectionKey(UPDATED_SECTION_KEY).contentHtml(UPDATED_CONTENT_HTML);
        return pageContent;
    }

    @BeforeEach
    public void initTest() {
        pageContent = createEntity(em);
    }

    @Test
    @Transactional
    void createPageContent() throws Exception {
        int databaseSizeBeforeCreate = pageContentRepository.findAll().size();
        // Create the PageContent
        PageContentDTO pageContentDTO = pageContentMapper.toDto(pageContent);
        restPageContentMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pageContentDTO))
            )
            .andExpect(status().isCreated());

        // Validate the PageContent in the database
        List<PageContent> pageContentList = pageContentRepository.findAll();
        assertThat(pageContentList).hasSize(databaseSizeBeforeCreate + 1);
        PageContent testPageContent = pageContentList.get(pageContentList.size() - 1);
        assertThat(testPageContent.getSectionKey()).isEqualTo(DEFAULT_SECTION_KEY);
        assertThat(testPageContent.getContentHtml()).isEqualTo(DEFAULT_CONTENT_HTML);
    }

    @Test
    @Transactional
    void createPageContentWithExistingId() throws Exception {
        // Create the PageContent with an existing ID
        pageContent.setId(1L);
        PageContentDTO pageContentDTO = pageContentMapper.toDto(pageContent);

        int databaseSizeBeforeCreate = pageContentRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPageContentMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pageContentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PageContent in the database
        List<PageContent> pageContentList = pageContentRepository.findAll();
        assertThat(pageContentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkSectionKeyIsRequired() throws Exception {
        int databaseSizeBeforeTest = pageContentRepository.findAll().size();
        // set the field null
        pageContent.setSectionKey(null);

        // Create the PageContent, which fails.
        PageContentDTO pageContentDTO = pageContentMapper.toDto(pageContent);

        restPageContentMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pageContentDTO))
            )
            .andExpect(status().isBadRequest());

        List<PageContent> pageContentList = pageContentRepository.findAll();
        assertThat(pageContentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPageContents() throws Exception {
        // Initialize the database
        pageContentRepository.saveAndFlush(pageContent);

        // Get all the pageContentList
        restPageContentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pageContent.getId().intValue())))
            .andExpect(jsonPath("$.[*].sectionKey").value(hasItem(DEFAULT_SECTION_KEY)))
            .andExpect(jsonPath("$.[*].contentHtml").value(hasItem(DEFAULT_CONTENT_HTML.toString())));
    }

    @Test
    @Transactional
    void getPageContent() throws Exception {
        // Initialize the database
        pageContentRepository.saveAndFlush(pageContent);

        // Get the pageContent
        restPageContentMockMvc
            .perform(get(ENTITY_API_URL_ID, pageContent.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(pageContent.getId().intValue()))
            .andExpect(jsonPath("$.sectionKey").value(DEFAULT_SECTION_KEY))
            .andExpect(jsonPath("$.contentHtml").value(DEFAULT_CONTENT_HTML.toString()));
    }

    @Test
    @Transactional
    void getNonExistingPageContent() throws Exception {
        // Get the pageContent
        restPageContentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPageContent() throws Exception {
        // Initialize the database
        pageContentRepository.saveAndFlush(pageContent);

        int databaseSizeBeforeUpdate = pageContentRepository.findAll().size();

        // Update the pageContent
        PageContent updatedPageContent = pageContentRepository.findById(pageContent.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPageContent are not directly saved in db
        em.detach(updatedPageContent);
        updatedPageContent.sectionKey(UPDATED_SECTION_KEY).contentHtml(UPDATED_CONTENT_HTML);
        PageContentDTO pageContentDTO = pageContentMapper.toDto(updatedPageContent);

        restPageContentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pageContentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pageContentDTO))
            )
            .andExpect(status().isOk());

        // Validate the PageContent in the database
        List<PageContent> pageContentList = pageContentRepository.findAll();
        assertThat(pageContentList).hasSize(databaseSizeBeforeUpdate);
        PageContent testPageContent = pageContentList.get(pageContentList.size() - 1);
        assertThat(testPageContent.getSectionKey()).isEqualTo(UPDATED_SECTION_KEY);
        assertThat(testPageContent.getContentHtml()).isEqualTo(UPDATED_CONTENT_HTML);
    }

    @Test
    @Transactional
    void putNonExistingPageContent() throws Exception {
        int databaseSizeBeforeUpdate = pageContentRepository.findAll().size();
        pageContent.setId(longCount.incrementAndGet());

        // Create the PageContent
        PageContentDTO pageContentDTO = pageContentMapper.toDto(pageContent);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPageContentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pageContentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pageContentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PageContent in the database
        List<PageContent> pageContentList = pageContentRepository.findAll();
        assertThat(pageContentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPageContent() throws Exception {
        int databaseSizeBeforeUpdate = pageContentRepository.findAll().size();
        pageContent.setId(longCount.incrementAndGet());

        // Create the PageContent
        PageContentDTO pageContentDTO = pageContentMapper.toDto(pageContent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPageContentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pageContentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PageContent in the database
        List<PageContent> pageContentList = pageContentRepository.findAll();
        assertThat(pageContentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPageContent() throws Exception {
        int databaseSizeBeforeUpdate = pageContentRepository.findAll().size();
        pageContent.setId(longCount.incrementAndGet());

        // Create the PageContent
        PageContentDTO pageContentDTO = pageContentMapper.toDto(pageContent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPageContentMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pageContentDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PageContent in the database
        List<PageContent> pageContentList = pageContentRepository.findAll();
        assertThat(pageContentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePageContentWithPatch() throws Exception {
        // Initialize the database
        pageContentRepository.saveAndFlush(pageContent);

        int databaseSizeBeforeUpdate = pageContentRepository.findAll().size();

        // Update the pageContent using partial update
        PageContent partialUpdatedPageContent = new PageContent();
        partialUpdatedPageContent.setId(pageContent.getId());

        partialUpdatedPageContent.sectionKey(UPDATED_SECTION_KEY);

        restPageContentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPageContent.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPageContent))
            )
            .andExpect(status().isOk());

        // Validate the PageContent in the database
        List<PageContent> pageContentList = pageContentRepository.findAll();
        assertThat(pageContentList).hasSize(databaseSizeBeforeUpdate);
        PageContent testPageContent = pageContentList.get(pageContentList.size() - 1);
        assertThat(testPageContent.getSectionKey()).isEqualTo(UPDATED_SECTION_KEY);
        assertThat(testPageContent.getContentHtml()).isEqualTo(DEFAULT_CONTENT_HTML);
    }

    @Test
    @Transactional
    void fullUpdatePageContentWithPatch() throws Exception {
        // Initialize the database
        pageContentRepository.saveAndFlush(pageContent);

        int databaseSizeBeforeUpdate = pageContentRepository.findAll().size();

        // Update the pageContent using partial update
        PageContent partialUpdatedPageContent = new PageContent();
        partialUpdatedPageContent.setId(pageContent.getId());

        partialUpdatedPageContent.sectionKey(UPDATED_SECTION_KEY).contentHtml(UPDATED_CONTENT_HTML);

        restPageContentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPageContent.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPageContent))
            )
            .andExpect(status().isOk());

        // Validate the PageContent in the database
        List<PageContent> pageContentList = pageContentRepository.findAll();
        assertThat(pageContentList).hasSize(databaseSizeBeforeUpdate);
        PageContent testPageContent = pageContentList.get(pageContentList.size() - 1);
        assertThat(testPageContent.getSectionKey()).isEqualTo(UPDATED_SECTION_KEY);
        assertThat(testPageContent.getContentHtml()).isEqualTo(UPDATED_CONTENT_HTML);
    }

    @Test
    @Transactional
    void patchNonExistingPageContent() throws Exception {
        int databaseSizeBeforeUpdate = pageContentRepository.findAll().size();
        pageContent.setId(longCount.incrementAndGet());

        // Create the PageContent
        PageContentDTO pageContentDTO = pageContentMapper.toDto(pageContent);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPageContentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, pageContentDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pageContentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PageContent in the database
        List<PageContent> pageContentList = pageContentRepository.findAll();
        assertThat(pageContentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPageContent() throws Exception {
        int databaseSizeBeforeUpdate = pageContentRepository.findAll().size();
        pageContent.setId(longCount.incrementAndGet());

        // Create the PageContent
        PageContentDTO pageContentDTO = pageContentMapper.toDto(pageContent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPageContentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pageContentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PageContent in the database
        List<PageContent> pageContentList = pageContentRepository.findAll();
        assertThat(pageContentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPageContent() throws Exception {
        int databaseSizeBeforeUpdate = pageContentRepository.findAll().size();
        pageContent.setId(longCount.incrementAndGet());

        // Create the PageContent
        PageContentDTO pageContentDTO = pageContentMapper.toDto(pageContent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPageContentMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(pageContentDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PageContent in the database
        List<PageContent> pageContentList = pageContentRepository.findAll();
        assertThat(pageContentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePageContent() throws Exception {
        // Initialize the database
        pageContentRepository.saveAndFlush(pageContent);

        int databaseSizeBeforeDelete = pageContentRepository.findAll().size();

        // Delete the pageContent
        restPageContentMockMvc
            .perform(delete(ENTITY_API_URL_ID, pageContent.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PageContent> pageContentList = pageContentRepository.findAll();
        assertThat(pageContentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
