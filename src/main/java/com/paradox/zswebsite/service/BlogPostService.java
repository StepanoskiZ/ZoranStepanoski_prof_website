package com.paradox.zswebsite.service;

import com.paradox.zswebsite.domain.BlogPost;
import com.paradox.zswebsite.repository.BlogPostRepository;
import com.paradox.zswebsite.service.dto.BlogPostDTO;
import com.paradox.zswebsite.service.mapper.BlogPostMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.apache.commons.text.StringEscapeUtils;

/**
 * Service Implementation for managing {@link com.paradox.zswebsite.domain.BlogPost}.
 */
@Service
@Transactional
public class BlogPostService {

    private final Logger log = LoggerFactory.getLogger(BlogPostService.class);

    private final BlogPostRepository blogPostRepository;

    private final BlogPostMapper blogPostMapper;

    public BlogPostService(BlogPostRepository blogPostRepository, BlogPostMapper blogPostMapper) {
        this.blogPostRepository = blogPostRepository;
        this.blogPostMapper = blogPostMapper;
    }

    /**
     * Save a blogPost.
     *
     * @param blogPostDTO the entity to save.
     * @return the persisted entity.
     */
    public BlogPostDTO save(BlogPostDTO blogPostDTO) {
        log.debug("Request to save BlogPost : {}", blogPostDTO);
        // --- FIX: Clean the HTML content before saving ---
        blogPostDTO.setContentHTML(cleanContentHtml(blogPostDTO.getContentHTML()));

        BlogPost blogPost = blogPostMapper.toEntity(blogPostDTO);
        blogPost = blogPostRepository.save(blogPost);
        return blogPostMapper.toDto(blogPost);
    }

    /**
     * Update a blogPost.
     *
     * @param blogPostDTO the entity to save.
     * @return the persisted entity.
     */
    public BlogPostDTO update(BlogPostDTO blogPostDTO) {
        log.debug("Request to update BlogPost : {}", blogPostDTO);
        // --- FIX: Clean the HTML content before updating ---
        blogPostDTO.setContentHTML(cleanContentHtml(blogPostDTO.getContentHTML()));

        BlogPost blogPost = blogPostMapper.toEntity(blogPostDTO);
        blogPost = blogPostRepository.save(blogPost);
        return blogPostMapper.toDto(blogPost);
    }

    /**
     * Partially update a blogPost.
     *
     * @param blogPostDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<BlogPostDTO> partialUpdate(BlogPostDTO blogPostDTO) {
        log.debug("Request to partially update BlogPost : {}", blogPostDTO);
        // --- FIX: Clean the HTML content before partial update ---
        blogPostDTO.setContentHTML(cleanContentHtml(blogPostDTO.getContentHTML()));

        return blogPostRepository
            .findById(blogPostDTO.getId())
            .map(existingBlogPost -> {
                blogPostMapper.partialUpdate(existingBlogPost, blogPostDTO);
                return existingBlogPost;
            })
            .map(blogPostRepository::save)
            .map(blogPostMapper::toDto);
    }

    /**
     * Cleans the HTML content by unescaping entities and replacing non-breaking spaces.
     * @param rawHtml the raw HTML string from the frontend.
     * @return a cleaned HTML string ready for database storage.
     */
    private String cleanContentHtml(String rawHtml) {
        if (rawHtml == null) {
            return null;
        }
        // First, unescape entities like &lt; to <
        String decodedHtml = StringEscapeUtils.unescapeHtml4(rawHtml);
        // Then, replace all non-breaking spaces with regular spaces to allow word wrapping.
        return decodedHtml.replace("&nbsp;", " ");
    }

    /**
     * Get all the blogPosts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<BlogPostDTO> findAll(Pageable pageable) {
        log.debug("Request to get all BlogPosts");
        return blogPostRepository.findAll(pageable).map(blogPostMapper::toDto);
    }

    /**
     * Get one blogPost by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<BlogPostDTO> findOne(Long id) {
        log.debug("Request to get BlogPost : {}", id);
        return blogPostRepository.findById(id).map(blogPostMapper::toDto);
    }

    /**
     * Delete the blogPost by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete BlogPost : {}", id);
        blogPostRepository.deleteById(id);
    }
}
