package com.paradox.zswebsite.service;

import com.paradox.zswebsite.domain.AboutMe;
import com.paradox.zswebsite.repository.AboutMeRepository;
import com.paradox.zswebsite.service.dto.AboutMeDTO;
import com.paradox.zswebsite.service.mapper.AboutMeMapper;
import com.paradox.zswebsite.service.mapper.AboutMeMediaMapper;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.commons.text.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Service Implementation for managing {@link com.paradox.zswebsite.domain.AboutMe}.
 */
@Service
@Transactional
public class AboutMeService {

    private final Logger log = LoggerFactory.getLogger(AboutMeService.class);

    private final AboutMeRepository aboutMeRepository;

    private final AboutMeMapper aboutMeMapper;

    @Autowired
    private AboutMeMediaMapper aboutMeMediaMapper;

    public AboutMeService(AboutMeRepository aboutMeRepository, AboutMeMapper aboutMeMapper) {
        this.aboutMeRepository = aboutMeRepository;
        this.aboutMeMapper = aboutMeMapper;
    }

    /**
     * Save a aboutMe.
     *
     * @param aboutMeDTO the entity to save.
     * @return the persisted entity.
     */
    public AboutMeDTO save(AboutMeDTO aboutMeDTO) {
        log.debug("Request to save AboutMe : {}", aboutMeDTO);
        if (aboutMeDTO.getContentHtml() != null) {
            String decodedHtml = StringEscapeUtils.unescapeHtml4(aboutMeDTO.getContentHtml());
            aboutMeDTO.setContentHtml(decodedHtml);
        }
        AboutMe aboutMe = aboutMeMapper.toEntity(aboutMeDTO);
        aboutMe = aboutMeRepository.save(aboutMe);
        return aboutMeMapper.toDto(aboutMe);
    }

    /**
     * Update a aboutMe.
     *
     * @param aboutMeDTO the entity to save.
     * @return the persisted entity.
     */
    public AboutMeDTO update(AboutMeDTO aboutMeDTO) {
        log.debug("Request to update AboutMe : {}", aboutMeDTO);
        if (aboutMeDTO.getContentHtml() != null) {
            String decodedHtml = StringEscapeUtils.unescapeHtml4(aboutMeDTO.getContentHtml());
            aboutMeDTO.setContentHtml(decodedHtml);
        }
        AboutMe aboutMe = aboutMeMapper.toEntity(aboutMeDTO);
        aboutMe = aboutMeRepository.save(aboutMe);
        return aboutMeMapper.toDto(aboutMe);
    }

    /**
     * Partially update a aboutMe.
     *
     * @param aboutMeDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AboutMeDTO> partialUpdate(AboutMeDTO aboutMeDTO) {
        log.debug("Request to partially update AboutMe : {}", aboutMeDTO);

        return aboutMeRepository
            .findById(aboutMeDTO.getId())
            .map(existingAboutMe -> {
                aboutMeMapper.partialUpdate(existingAboutMe, aboutMeDTO);

                return existingAboutMe;
            })
            .map(aboutMeRepository::save)
            .map(aboutMeMapper::toDto);
    }

    /**
     * Get all the aboutMes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<AboutMeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all AboutMes");
        return aboutMeRepository.findAll(pageable).map(aboutMeMapper::toDto);
    }

    /**
     * Get one aboutMe by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AboutMeDTO> findOne(Long id) {
        log.debug("Request to get AboutMe : {}", id);
        return aboutMeRepository.findById(id).map(aboutMeMapper::toDto);
    }

    /**
     * Delete the aboutMe by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete AboutMe : {}", id);
        aboutMeRepository.deleteById(id);
    }

    /**
     * Get the first AboutMe entry found, including its media files.
     *
     * @return the entity if found.
     */
    @Transactional(readOnly = true)
    public Optional<AboutMeDTO> findFirst() {
        log.debug("Request to get first AboutMe with details");

        Optional<AboutMe> aboutMeOptional = aboutMeRepository.findAll().stream().findFirst();

        return aboutMeOptional.map(aboutMe -> {
            AboutMeDTO dto = aboutMeMapper.toDto(aboutMe);

            dto.setMediaFiles(
                aboutMe.getMedia()
                    .stream()
                    .map(aboutMeMediaMapper::toDto)
                    .collect(Collectors.toSet())
            );

            log.debug("Returning AboutMeDTO with {} media files.", dto.getMediaFiles().size());

            return dto;
        });
    }}

