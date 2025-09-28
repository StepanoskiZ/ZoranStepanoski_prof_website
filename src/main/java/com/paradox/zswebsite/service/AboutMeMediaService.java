package com.paradox.zswebsite.service;

import com.paradox.zswebsite.domain.AboutMeMedia;
import com.paradox.zswebsite.repository.AboutMeMediaRepository;
import com.paradox.zswebsite.service.dto.AboutMeMediaDTO;
import com.paradox.zswebsite.service.mapper.AboutMeMediaMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.paradox.zswebsite.domain.AboutMeMedia}.
 */
@Service
@Transactional
public class AboutMeMediaService {

    private final Logger log = LoggerFactory.getLogger(AboutMeMediaService.class);

    private final AboutMeMediaRepository aboutMeMediaRepository;

    private final AboutMeMediaMapper aboutMeMediaMapper;

    public AboutMeMediaService(AboutMeMediaRepository aboutMeMediaRepository, AboutMeMediaMapper aboutMeMediaMapper) {
        this.aboutMeMediaRepository = aboutMeMediaRepository;
        this.aboutMeMediaMapper = aboutMeMediaMapper;
    }

    /**
     * Save a aboutMeMedia.
     *
     * @param aboutMeMediaDTO the entity to save.
     * @return the persisted entity.
     */
    public AboutMeMediaDTO save(AboutMeMediaDTO aboutMeMediaDTO) {
        log.debug("Request to save AboutMeMedia : {}", aboutMeMediaDTO);
        AboutMeMedia aboutMeMedia = aboutMeMediaMapper.toEntity(aboutMeMediaDTO);
        aboutMeMedia = aboutMeMediaRepository.save(aboutMeMedia);
        return aboutMeMediaMapper.toDto(aboutMeMedia);
    }

    /**
     * Update a aboutMeMedia.
     *
     * @param aboutMeMediaDTO the entity to save.
     * @return the persisted entity.
     */
    public AboutMeMediaDTO update(AboutMeMediaDTO aboutMeMediaDTO) {
        log.debug("Request to update AboutMeMedia : {}", aboutMeMediaDTO);
        AboutMeMedia aboutMeMedia = aboutMeMediaMapper.toEntity(aboutMeMediaDTO);
        aboutMeMedia = aboutMeMediaRepository.save(aboutMeMedia);
        return aboutMeMediaMapper.toDto(aboutMeMedia);
    }

    /**
     * Partially update a aboutMeMedia.
     *
     * @param aboutMeMediaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AboutMeMediaDTO> partialUpdate(AboutMeMediaDTO aboutMeMediaDTO) {
        log.debug("Request to partially update AboutMeMedia : {}", aboutMeMediaDTO);

        return aboutMeMediaRepository
            .findById(aboutMeMediaDTO.getId())
            .map(existingAboutMeMedia -> {
                aboutMeMediaMapper.partialUpdate(existingAboutMeMedia, aboutMeMediaDTO);

                return existingAboutMeMedia;
            })
            .map(aboutMeMediaRepository::save)
            .map(aboutMeMediaMapper::toDto);
    }

    /**
     * Get all the aboutMeMedias.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<AboutMeMediaDTO> findAll(Pageable pageable) {
        log.debug("Request to get all AboutMeMedias");
        return aboutMeMediaRepository.findAll(pageable).map(aboutMeMediaMapper::toDto);
    }

    /**
     * Get one aboutMeMedia by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AboutMeMediaDTO> findOne(Long id) {
        log.debug("Request to get AboutMeMedia : {}", id);
        return aboutMeMediaRepository.findById(id).map(aboutMeMediaMapper::toDto);
    }

    /**
     * Delete the aboutMeMedia by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete AboutMeMedia : {}", id);
        aboutMeMediaRepository.deleteById(id);
    }
}
