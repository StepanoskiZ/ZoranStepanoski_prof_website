package com.paradox.zswebsite.service;

import com.paradox.zswebsite.domain.PageContent;
import com.paradox.zswebsite.repository.PageContentRepository;
import com.paradox.zswebsite.service.dto.PageContentDTO;
import com.paradox.zswebsite.service.mapper.PageContentMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.paradox.zswebsite.domain.PageContent}.
 */
@Service
@Transactional
public class PageContentService {

    private final Logger log = LoggerFactory.getLogger(PageContentService.class);

    private final PageContentRepository pageContentRepository;

    private final PageContentMapper pageContentMapper;

    public PageContentService(PageContentRepository pageContentRepository, PageContentMapper pageContentMapper) {
        this.pageContentRepository = pageContentRepository;
        this.pageContentMapper = pageContentMapper;
    }

    /**
     * Save a pageContent.
     *
     * @param pageContentDTO the entity to save.
     * @return the persisted entity.
     */
    public PageContentDTO save(PageContentDTO pageContentDTO) {
        log.debug("Request to save PageContent : {}", pageContentDTO);
        PageContent pageContent = pageContentMapper.toEntity(pageContentDTO);
        pageContent = pageContentRepository.save(pageContent);
        return pageContentMapper.toDto(pageContent);
    }

    /**
     * Update a pageContent.
     *
     * @param pageContentDTO the entity to save.
     * @return the persisted entity.
     */
    public PageContentDTO update(PageContentDTO pageContentDTO) {
        log.debug("Request to update PageContent : {}", pageContentDTO);
        PageContent pageContent = pageContentMapper.toEntity(pageContentDTO);
        pageContent = pageContentRepository.save(pageContent);
        return pageContentMapper.toDto(pageContent);
    }

    /**
     * Partially update a pageContent.
     *
     * @param pageContentDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PageContentDTO> partialUpdate(PageContentDTO pageContentDTO) {
        log.debug("Request to partially update PageContent : {}", pageContentDTO);

        return pageContentRepository
            .findById(pageContentDTO.getId())
            .map(existingPageContent -> {
                pageContentMapper.partialUpdate(existingPageContent, pageContentDTO);

                return existingPageContent;
            })
            .map(pageContentRepository::save)
            .map(pageContentMapper::toDto);
    }

    /**
     * Get all the pageContents.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<PageContentDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PageContents");
        return pageContentRepository.findAll(pageable).map(pageContentMapper::toDto);
    }

    /**
     * Get one pageContent by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PageContentDTO> findOne(Long id) {
        log.debug("Request to get PageContent : {}", id);
        return pageContentRepository.findById(id).map(pageContentMapper::toDto);
    }

    /**
     * Delete the pageContent by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete PageContent : {}", id);
        pageContentRepository.deleteById(id);
    }

    /**
     * Get one pageContent by its sectionKey.
     *
     * @param sectionKey the key of the entity.
     * @return the entity DTO, or an empty Optional if not found.
     */
    @Transactional(readOnly = true)
    public Optional<PageContentDTO> findOneBySectionKey(String sectionKey) {
        log.debug("Request to get PageContent by sectionKey : {}", sectionKey);
        return pageContentRepository.findBySectionKey(sectionKey).map(pageContentMapper::toDto);
    }
}
