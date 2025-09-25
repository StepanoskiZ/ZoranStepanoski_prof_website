package com.paradox.zswebsite.service;

import com.paradox.zswebsite.domain.PageContentMedia;
import com.paradox.zswebsite.repository.PageContentMediaRepository;
import com.paradox.zswebsite.service.dto.PageContentMediaDTO;
import com.paradox.zswebsite.service.mapper.PageContentMediaMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.paradox.zswebsite.domain.PageContentMedia}.
 */
@Service
@Transactional
public class PageContentMediaService {

    private static final Logger LOG = LoggerFactory.getLogger(PageContentMediaService.class);

    private final PageContentMediaRepository pageContentMediaRepository;

    private final PageContentMediaMapper pageContentMediaMapper;

    public PageContentMediaService(PageContentMediaRepository pageContentMediaRepository, PageContentMediaMapper pageContentMediaMapper) {
        this.pageContentMediaRepository = pageContentMediaRepository;
        this.pageContentMediaMapper = pageContentMediaMapper;
    }

    /**
     * Save a pageContentMedia.
     *
     * @param pageContentMediaDTO the entity to save.
     * @return the persisted entity.
     */
    public PageContentMediaDTO save(PageContentMediaDTO pageContentMediaDTO) {
        LOG.debug("Request to save PageContentMedia : {}", pageContentMediaDTO);
        PageContentMedia pageContentMedia = pageContentMediaMapper.toEntity(pageContentMediaDTO);
        pageContentMedia = pageContentMediaRepository.save(pageContentMedia);
        return pageContentMediaMapper.toDto(pageContentMedia);
    }

    /**
     * Update a pageContentMedia.
     *
     * @param pageContentMediaDTO the entity to save.
     * @return the persisted entity.
     */
    public PageContentMediaDTO update(PageContentMediaDTO pageContentMediaDTO) {
        LOG.debug("Request to update PageContentMedia : {}", pageContentMediaDTO);
        PageContentMedia pageContentMedia = pageContentMediaMapper.toEntity(pageContentMediaDTO);
        pageContentMedia = pageContentMediaRepository.save(pageContentMedia);
        return pageContentMediaMapper.toDto(pageContentMedia);
    }

    /**
     * Partially update a pageContentMedia.
     *
     * @param pageContentMediaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PageContentMediaDTO> partialUpdate(PageContentMediaDTO pageContentMediaDTO) {
        LOG.debug("Request to partially update PageContentMedia : {}", pageContentMediaDTO);

        return pageContentMediaRepository
            .findById(pageContentMediaDTO.getId())
            .map(existingPageContentMedia -> {
                pageContentMediaMapper.partialUpdate(existingPageContentMedia, pageContentMediaDTO);

                return existingPageContentMedia;
            })
            .map(pageContentMediaRepository::save)
            .map(pageContentMediaMapper::toDto);
    }

    /**
     * Get all the pageContentMedias.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<PageContentMediaDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all PageContentMedias");
        return pageContentMediaRepository.findAll(pageable).map(pageContentMediaMapper::toDto);
    }

    /**
     * Get all the pageContentMedias with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<PageContentMediaDTO> findAllWithEagerRelationships(Pageable pageable) {
        return pageContentMediaRepository.findAllWithEagerRelationships(pageable).map(pageContentMediaMapper::toDto);
    }

    /**
     * Get one pageContentMedia by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PageContentMediaDTO> findOne(Long id) {
        LOG.debug("Request to get PageContentMedia : {}", id);
        return pageContentMediaRepository.findOneWithEagerRelationships(id).map(pageContentMediaMapper::toDto);
    }

    /**
     * Delete the pageContentMedia by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete PageContentMedia : {}", id);
        pageContentMediaRepository.deleteById(id);
    }
}
