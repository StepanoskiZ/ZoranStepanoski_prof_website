package com.paradox.zswebsite.service;

import com.paradox.zswebsite.domain.CurriculumVitae;
import com.paradox.zswebsite.domain.CurriculumVitaeMedia;
import com.paradox.zswebsite.repository.CurriculumVitaeRepository;
import com.paradox.zswebsite.service.dto.CurriculumVitaeCardDTO;
import com.paradox.zswebsite.service.dto.CurriculumVitaeDTO;
import com.paradox.zswebsite.service.dto.CurriculumVitaeDetailDTO;
import com.paradox.zswebsite.service.dto.CurriculumVitaeMediaDTO;
import com.paradox.zswebsite.service.mapper.CurriculumVitaeMapper;
import com.paradox.zswebsite.service.mapper.CurriculumVitaeMediaMapper;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.apache.commons.text.StringEscapeUtils;

/**
 * Service Implementation for managing {@link com.paradox.zswebsite.domain.CurriculumVitae}.
 */
@Service
@Transactional
public class CurriculumVitaeService {

    private final Logger log = LoggerFactory.getLogger(CurriculumVitaeService.class);
    private final CurriculumVitaeRepository curriculumVitaeRepository;
    private final CurriculumVitaeMapper curriculumVitaeMapper;
    private final CurriculumVitaeMediaMapper curriculumVitaeMediaMapper;

    public CurriculumVitaeService(
        CurriculumVitaeRepository curriculumVitaeRepository,
        CurriculumVitaeMapper curriculumVitaeMapper,
        CurriculumVitaeMediaMapper curriculumVitaeMediaMapper
    ) {
        this.curriculumVitaeRepository = curriculumVitaeRepository;
        this.curriculumVitaeMapper = curriculumVitaeMapper;
        this.curriculumVitaeMediaMapper = curriculumVitaeMediaMapper;
    }

    /**
     * Save a curriculumVitae.
     *
     * @param curriculumVitaeDTO the entity to save.
     * @return the persisted entity.
     */
    public CurriculumVitaeDTO save(CurriculumVitaeDTO curriculumVitaeDTO) {
        log.debug("Request to save CurriculumVitae : {}", curriculumVitaeDTO);
        curriculumVitaeDTO.setJobDescriptionHTML(cleanContentHtml(curriculumVitaeDTO.getJobDescriptionHTML()));

        CurriculumVitae curriculumVitae = curriculumVitaeMapper.toEntity(curriculumVitaeDTO);
        curriculumVitae = curriculumVitaeRepository.save(curriculumVitae);
        return curriculumVitaeMapper.toDto(curriculumVitae);
    }

    /**
     * Update a curriculumVitae.
     *
     * @param curriculumVitaeDTO the entity to save.
     * @return the persisted entity.
     */
    public CurriculumVitaeDTO update(CurriculumVitaeDTO curriculumVitaeDTO) {
        log.debug("Request to update CurriculumVitae : {}", curriculumVitaeDTO);
        curriculumVitaeDTO.setJobDescriptionHTML(cleanContentHtml(curriculumVitaeDTO.getJobDescriptionHTML()));

        CurriculumVitae curriculumVitae = curriculumVitaeMapper.toEntity(curriculumVitaeDTO);
        curriculumVitae = curriculumVitaeRepository.save(curriculumVitae);
        return curriculumVitaeMapper.toDto(curriculumVitae);
    }

    /**
     * Partially update a curriculumVitae.
     *
     * @param curriculumVitaeDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CurriculumVitaeDTO> partialUpdate(CurriculumVitaeDTO curriculumVitaeDTO) {
        log.debug("Request to partially update CurriculumVitae : {}", curriculumVitaeDTO);
        curriculumVitaeDTO.setJobDescriptionHTML(cleanContentHtml(curriculumVitaeDTO.getJobDescriptionHTML()));

        return curriculumVitaeRepository
            .findById(curriculumVitaeDTO.getId())
            .map(existingCurriculumVitae -> {
                curriculumVitaeMapper.partialUpdate(existingCurriculumVitae, curriculumVitaeDTO);
                return existingCurriculumVitae;
            })
            .map(curriculumVitaeRepository::save)
            .map(curriculumVitaeMapper::toDto);
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
     * Get all the curriculumVitaes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CurriculumVitaeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CurriculumVitaes");
        return curriculumVitaeRepository.findAll(pageable).map(curriculumVitaeMapper::toDto);
    }

    /**
     * Get one curriculumVitae by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CurriculumVitaeDTO> findOne(Long id) {
        log.debug("Request to get CurriculumVitae : {}", id);
        return curriculumVitaeRepository.findById(id).map(curriculumVitaeMapper::toDto);
    }

    /**
     * Delete the curriculumVitae by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete CurriculumVitae : {}", id);
        curriculumVitaeRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<CurriculumVitaeCardDTO> findAllForLandingPage() {
        log.debug("Request to get all CurriculumVitae for landing page cards");
        return curriculumVitaeRepository
            .findAllWithEagerRelationships() // <-- Use the new optimized method
            .stream()
            .map(cv -> {
                CurriculumVitaeCardDTO dto = new CurriculumVitaeCardDTO();
                dto.setId(cv.getId());
                dto.setCompanyName(cv.getCompanyName());

                String description = cv.getJobDescriptionHTML();
                if (description != null && description.length() > 150) {
                    description = description.substring(0, 150) + "...";
                }
                dto.setJobDescriptionHTML(description);
                dto.setStartDate(cv.getStartDate());
                dto.setEndDate(cv.getEndDate());

                // --- ADD THIS MEDIA LOGIC (copied and adapted from ProjectService) ---
                cv
                    .getMedia()
                    .stream()
                    .sorted(Comparator.comparing(CurriculumVitaeMedia::getId)) // Sort for consistency
                    .findFirst()
                    .ifPresent(firstMedia -> {
                        dto.setFirstMediaUrl(firstMedia.getMediaUrl());
                    });

                return dto;
            })
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<CurriculumVitaeDetailDTO> findOneWithDetails(Long id) {
        log.debug("Request to get detailed CurriculumVitae with media : {}", id);
        return curriculumVitaeRepository
            .findOneWithEagerRelationships(id) // <-- USE THE EAGER METHOD
            .map(cv -> {
                CurriculumVitaeDetailDTO dto = new CurriculumVitaeDetailDTO();
                dto.setId(cv.getId());
                dto.setCompanyName(cv.getCompanyName());
                dto.setJobDescriptionHTML(cv.getJobDescriptionHTML());
                dto.setStartDate(cv.getStartDate());
                dto.setEndDate(cv.getEndDate());

                List<CurriculumVitaeMediaDTO> mediaDTOs = cv
                    .getMedia()
                    .stream()
                    .map(curriculumVitaeMediaMapper::toDto)
                    .sorted(Comparator.comparing(CurriculumVitaeMediaDTO::getId))
                    .collect(Collectors.toList());
                dto.setMediaFiles(mediaDTOs);

                return dto;
            });
    }
}
