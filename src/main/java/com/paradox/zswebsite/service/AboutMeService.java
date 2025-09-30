package com.paradox.zswebsite.service;

import com.paradox.zswebsite.domain.AboutMe;
import com.paradox.zswebsite.repository.AboutMeRepository;
import com.paradox.zswebsite.service.dto.AboutMeDTO;
import com.paradox.zswebsite.service.dto.AboutMeMediaDTO;
import com.paradox.zswebsite.service.mapper.AboutMeMapper;
import com.paradox.zswebsite.service.mapper.AboutMeMediaMapper;
import com.paradox.zswebsite.service.dto.AboutMeCardDTO;

import java.util.HashSet;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.Comparator;
import java.util.List;
import org.apache.commons.text.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.hibernate.Hibernate;

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
        // --- FIX: Use the helper method to clean the HTML ---
        aboutMeDTO.setContentHtml(cleanContentHtml(aboutMeDTO.getContentHtml()));

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
        // --- FIX: Use the helper method to clean the HTML ---
        aboutMeDTO.setContentHtml(cleanContentHtml(aboutMeDTO.getContentHtml()));

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

        // --- FIX: Clean the HTML on the incoming DTO before mapping ---
        aboutMeDTO.setContentHtml(cleanContentHtml(aboutMeDTO.getContentHtml()));

        return aboutMeRepository
            .findById(aboutMeDTO.getId())
            .map(existingAboutMe -> {
                aboutMeMapper.partialUpdate(existingAboutMe, aboutMeDTO);
                return existingAboutMe;
            })
            .map(aboutMeRepository::save)
            .map(aboutMeMapper::toDto);
    }

    // --- NEW HELPER METHOD ---
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

        Optional<Long> firstId = aboutMeRepository.findAll(Sort.by("id").ascending())
            .stream()
            .map(AboutMe::getId)
            .findFirst();

        return firstId.flatMap(this::findOneWithDetails);
    }

    /**
     * Gets the lightweight "card" data for the About Me preview on the landing page.
     * @return an Optional containing the AboutMeCardDTO.
     */
    @Transactional(readOnly = true)
    public Optional<AboutMeCardDTO> findAboutMeCard() {
        log.debug("Request to get About Me card data");

        // Find the first ID
        return aboutMeRepository.findAll(Sort.by("id").ascending()).stream().findFirst()
            .flatMap(aboutMe -> {
                return findOneWithDetails(aboutMe.getId()).map(dto -> {
                    String firstMediaUrl = dto.getMediaFiles().stream()
                        .findFirst()
                        .map(AboutMeMediaDTO::getMediaUrl)
                        .orElse(null);

                    return new AboutMeCardDTO(dto.getId(), dto.getContentHtml(), firstMediaUrl);
                });
            });
    }

    @Transactional(readOnly = true)
    public Optional<AboutMeDTO> findOneWithDetails(Long id) {
        log.debug("Request to get detailed AboutMe with media : {}", id);
        return aboutMeRepository
            .findOneWithEagerRelationships(id)
            .map(aboutMe -> {
                AboutMeDTO dto = aboutMeMapper.toDto(aboutMe);

                List<AboutMeMediaDTO> mediaDTOs = aboutMe
                    .getMedia()
                    .stream()
                    .map(aboutMeMediaMapper::toDto)
                    .sorted(Comparator.comparing(AboutMeMediaDTO::getId)) // Sort by ID
                    .collect(Collectors.toList());

                // Use a setter that accepts a List, or convert to a Set
                dto.setMediaFiles(new HashSet<>(mediaDTOs));

                return dto;
            });
    }
}

