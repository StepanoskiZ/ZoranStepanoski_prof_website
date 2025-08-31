package com.paradox.zswebsite.web.rest;

import static com.paradox.zswebsite.domain.BlogPostAsserts.*;
import static com.paradox.zswebsite.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paradox.zswebsite.IntegrationTest;
import com.paradox.zswebsite.domain.BlogPost;
import com.paradox.zswebsite.repository.BlogPostRepository;
import com.paradox.zswebsite.service.dto.BlogPostDTO;
import com.paradox.zswebsite.service.mapper.BlogPostMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link BlogPostResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BlogPostResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBB";

    private static final Instant DEFAULT_PUBLISHED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_PUBLISHED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/blog-posts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private BlogPostRepository blogPostRepository;

    @Autowired
    private BlogPostMapper blogPostMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBlogPostMockMvc;

    private BlogPost blogPost;

    private BlogPost insertedBlogPost;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BlogPost createEntity() {
        return new BlogPost().title(DEFAULT_TITLE).content(DEFAULT_CONTENT).publishedDate(DEFAULT_PUBLISHED_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BlogPost createUpdatedEntity() {
        return new BlogPost().title(UPDATED_TITLE).content(UPDATED_CONTENT).publishedDate(UPDATED_PUBLISHED_DATE);
    }

    @BeforeEach
    void initTest() {
        blogPost = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedBlogPost != null) {
            blogPostRepository.delete(insertedBlogPost);
            insertedBlogPost = null;
        }
    }

    @Test
    @Transactional
    void createBlogPost() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the BlogPost
        BlogPostDTO blogPostDTO = blogPostMapper.toDto(blogPost);
        var returnedBlogPostDTO = om.readValue(
            restBlogPostMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(blogPostDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            BlogPostDTO.class
        );

        // Validate the BlogPost in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedBlogPost = blogPostMapper.toEntity(returnedBlogPostDTO);
        assertBlogPostUpdatableFieldsEquals(returnedBlogPost, getPersistedBlogPost(returnedBlogPost));

        insertedBlogPost = returnedBlogPost;
    }

    @Test
    @Transactional
    void createBlogPostWithExistingId() throws Exception {
        // Create the BlogPost with an existing ID
        blogPost.setId(1L);
        BlogPostDTO blogPostDTO = blogPostMapper.toDto(blogPost);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBlogPostMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(blogPostDTO)))
            .andExpect(status().isBadRequest());

        // Validate the BlogPost in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        blogPost.setTitle(null);

        // Create the BlogPost, which fails.
        BlogPostDTO blogPostDTO = blogPostMapper.toDto(blogPost);

        restBlogPostMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(blogPostDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPublishedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        blogPost.setPublishedDate(null);

        // Create the BlogPost, which fails.
        BlogPostDTO blogPostDTO = blogPostMapper.toDto(blogPost);

        restBlogPostMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(blogPostDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllBlogPosts() throws Exception {
        // Initialize the database
        insertedBlogPost = blogPostRepository.saveAndFlush(blogPost);

        // Get all the blogPostList
        restBlogPostMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(blogPost.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT)))
            .andExpect(jsonPath("$.[*].publishedDate").value(hasItem(DEFAULT_PUBLISHED_DATE.toString())));
    }

    @Test
    @Transactional
    void getBlogPost() throws Exception {
        // Initialize the database
        insertedBlogPost = blogPostRepository.saveAndFlush(blogPost);

        // Get the blogPost
        restBlogPostMockMvc
            .perform(get(ENTITY_API_URL_ID, blogPost.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(blogPost.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT))
            .andExpect(jsonPath("$.publishedDate").value(DEFAULT_PUBLISHED_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingBlogPost() throws Exception {
        // Get the blogPost
        restBlogPostMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingBlogPost() throws Exception {
        // Initialize the database
        insertedBlogPost = blogPostRepository.saveAndFlush(blogPost);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the blogPost
        BlogPost updatedBlogPost = blogPostRepository.findById(blogPost.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedBlogPost are not directly saved in db
        em.detach(updatedBlogPost);
        updatedBlogPost.title(UPDATED_TITLE).content(UPDATED_CONTENT).publishedDate(UPDATED_PUBLISHED_DATE);
        BlogPostDTO blogPostDTO = blogPostMapper.toDto(updatedBlogPost);

        restBlogPostMockMvc
            .perform(
                put(ENTITY_API_URL_ID, blogPostDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(blogPostDTO))
            )
            .andExpect(status().isOk());

        // Validate the BlogPost in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedBlogPostToMatchAllProperties(updatedBlogPost);
    }

    @Test
    @Transactional
    void putNonExistingBlogPost() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        blogPost.setId(longCount.incrementAndGet());

        // Create the BlogPost
        BlogPostDTO blogPostDTO = blogPostMapper.toDto(blogPost);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBlogPostMockMvc
            .perform(
                put(ENTITY_API_URL_ID, blogPostDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(blogPostDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BlogPost in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBlogPost() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        blogPost.setId(longCount.incrementAndGet());

        // Create the BlogPost
        BlogPostDTO blogPostDTO = blogPostMapper.toDto(blogPost);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBlogPostMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(blogPostDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BlogPost in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBlogPost() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        blogPost.setId(longCount.incrementAndGet());

        // Create the BlogPost
        BlogPostDTO blogPostDTO = blogPostMapper.toDto(blogPost);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBlogPostMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(blogPostDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the BlogPost in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBlogPostWithPatch() throws Exception {
        // Initialize the database
        insertedBlogPost = blogPostRepository.saveAndFlush(blogPost);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the blogPost using partial update
        BlogPost partialUpdatedBlogPost = new BlogPost();
        partialUpdatedBlogPost.setId(blogPost.getId());

        partialUpdatedBlogPost.content(UPDATED_CONTENT).publishedDate(UPDATED_PUBLISHED_DATE);

        restBlogPostMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBlogPost.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBlogPost))
            )
            .andExpect(status().isOk());

        // Validate the BlogPost in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBlogPostUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedBlogPost, blogPost), getPersistedBlogPost(blogPost));
    }

    @Test
    @Transactional
    void fullUpdateBlogPostWithPatch() throws Exception {
        // Initialize the database
        insertedBlogPost = blogPostRepository.saveAndFlush(blogPost);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the blogPost using partial update
        BlogPost partialUpdatedBlogPost = new BlogPost();
        partialUpdatedBlogPost.setId(blogPost.getId());

        partialUpdatedBlogPost.title(UPDATED_TITLE).content(UPDATED_CONTENT).publishedDate(UPDATED_PUBLISHED_DATE);

        restBlogPostMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBlogPost.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBlogPost))
            )
            .andExpect(status().isOk());

        // Validate the BlogPost in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBlogPostUpdatableFieldsEquals(partialUpdatedBlogPost, getPersistedBlogPost(partialUpdatedBlogPost));
    }

    @Test
    @Transactional
    void patchNonExistingBlogPost() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        blogPost.setId(longCount.incrementAndGet());

        // Create the BlogPost
        BlogPostDTO blogPostDTO = blogPostMapper.toDto(blogPost);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBlogPostMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, blogPostDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(blogPostDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BlogPost in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBlogPost() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        blogPost.setId(longCount.incrementAndGet());

        // Create the BlogPost
        BlogPostDTO blogPostDTO = blogPostMapper.toDto(blogPost);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBlogPostMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(blogPostDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BlogPost in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBlogPost() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        blogPost.setId(longCount.incrementAndGet());

        // Create the BlogPost
        BlogPostDTO blogPostDTO = blogPostMapper.toDto(blogPost);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBlogPostMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(blogPostDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the BlogPost in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBlogPost() throws Exception {
        // Initialize the database
        insertedBlogPost = blogPostRepository.saveAndFlush(blogPost);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the blogPost
        restBlogPostMockMvc
            .perform(delete(ENTITY_API_URL_ID, blogPost.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return blogPostRepository.count();
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

    protected BlogPost getPersistedBlogPost(BlogPost blogPost) {
        return blogPostRepository.findById(blogPost.getId()).orElseThrow();
    }

    protected void assertPersistedBlogPostToMatchAllProperties(BlogPost expectedBlogPost) {
        assertBlogPostAllPropertiesEquals(expectedBlogPost, getPersistedBlogPost(expectedBlogPost));
    }

    protected void assertPersistedBlogPostToMatchUpdatableProperties(BlogPost expectedBlogPost) {
        assertBlogPostAllUpdatablePropertiesEquals(expectedBlogPost, getPersistedBlogPost(expectedBlogPost));
    }
}
