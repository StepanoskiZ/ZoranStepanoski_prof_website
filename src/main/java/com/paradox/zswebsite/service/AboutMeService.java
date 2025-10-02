package com.paradox.zswebsite.service;

import com.paradox.zswebsite.domain.AboutMe;
import com.paradox.zswebsite.domain.AboutMeMedia;
import com.paradox.zswebsite.repository.AboutMeRepository;
import com.paradox.zswebsite.service.dto.AboutMeCardDTO;
import com.paradox.zswebsite.service.dto.AboutMeDTO;
import com.paradox.zswebsite.service.dto.AboutMeDetailDTO;
import com.paradox.zswebsite.service.dto.AboutMeMediaDTO;
import com.paradox.zswebsite.service.mapper.AboutMeMapper;
import com.paradox.zswebsite.service.mapper.AboutMeMediaMapper;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.text.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing {@link com.paradox.zswebsite.domain.AboutMe}.
 */
@Service
@Transactional
public class AboutMeService {

    private final Logger log = LoggerFactory.getLogger(AboutMeService.class);

    private final AboutMeRepository aboutMeRepository;
    private final AboutMeMapper aboutMeMapper;
    private final AboutMeMediaMapper aboutMeMediaMapper; // Assuming you have this mapper

    public AboutMeService(
        AboutMeRepository aboutMeRepository,
        AboutMeMapper aboutMeMapper,
        AboutMeMediaMapper aboutMeMediaMapper
    ) {
        this.aboutMeRepository = aboutMeRepository;
        this.aboutMeMapper = aboutMeMapper;
        this.aboutMeMediaMapper = aboutMeMediaMapper;
    }

    /**
     * Save a aboutMe.
     *
     * @param aboutMeDTO the entity to save.
     * @return the persisted entity.
     */
    public AboutMeDTO save(AboutMeDTO aboutMeDTO) {
        log.debug("Request to save AboutMe : {}", aboutMeDTO);
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

    /**
     * Cleans the HTML content by unescaping entities and replacing non-breaking spaces.
     * @param rawHtml the raw HTML string from the frontend.
     * @return a cleaned HTML string ready for database storage.
     */
    private String cleanContentHtml(String rawHtml) {
        if (rawHtml == null) {
            return null;
        }
        String decodedHtml = StringEscapeUtils.unescapeHtml4(rawHtml);
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
        log.debug("Request to get all AboutMe entries");
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
    public Optional<AboutMeCardDTO> findOneCard() {
        log.debug("Request to get first AboutMe with details");

        return aboutMeRepository.findAllWithEagerRelationships()
            .stream()
            .findFirst()
            .map(this::mapToCardDTO);
    }

    /**
     * Gets the lightweight "card" data for the About Me preview on the landing page.
     * @return an Optional containing the AboutMeCardDTO.
     */
    private AboutMeCardDTO mapToCardDTO(AboutMe aboutMe) {
        AboutMeCardDTO dto = new AboutMeCardDTO();
        dto.setId(aboutMe.getId());
        dto.setContentHtml(aboutMe.getContentHtml());

        // Find the first media item by sorting the media collection by its ID
        aboutMe.getMedia().stream()
            .min(Comparator.comparing(AboutMeMedia::getId))
            .ifPresent(firstMedia -> {
                dto.setFirstMediaUrl(firstMedia.getMediaUrl());
                dto.setFirstMediaType(firstMedia.getAboutMeMediaType());
            });

        return dto;
    }

    /**
     * Finds one "About Me" by ID with all details for the modal view.
     */
    @Transactional(readOnly = true)
    public Optional<AboutMeDetailDTO> findOneWithDetails(Long id) {
        return aboutMeRepository.findOneWithEagerRelationships(id)
            .map(aboutMe -> {
                AboutMeDetailDTO dto = new AboutMeDetailDTO();
                dto.setId(aboutMe.getId());
                // The Detail DTO might not have a title, we'll construct one if needed or get it from a field
                dto.setContentHtml(aboutMe.getContentHtml());

                // Map and sort the media files by their ID for consistent order
                List<AboutMeMediaDTO> mediaDTOs = aboutMe.getMedia().stream()
                    .sorted(Comparator.comparing(AboutMeMedia::getId))
                    .map(aboutMeMediaMapper::toDto)
                    .collect(Collectors.toList());
                dto.setMediaFiles(mediaDTOs);

                return dto;
            });
    }
}
