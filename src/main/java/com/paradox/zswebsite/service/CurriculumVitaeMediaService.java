package com.paradox.zswebsite.service;

import com.paradox.zswebsite.domain.CurriculumVitaeMedia;
import com.paradox.zswebsite.repository.CurriculumVitaeMediaRepository;
import com.paradox.zswebsite.service.dto.CurriculumVitaeMediaDTO;
import com.paradox.zswebsite.service.mapper.CurriculumVitaeMediaMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.paradox.zswebsite.domain.CurriculumVitaeMedia}.
 */
@Service
@Transactional
public class CurriculumVitaeMediaService {

    private final Logger log = LoggerFactory.getLogger(CurriculumVitaeMediaService.class);

    private final CurriculumVitaeMediaRepository curriculumVitaeMediaRepository;

    private final CurriculumVitaeMediaMapper curriculumVitaeMediaMapper;

    public CurriculumVitaeMediaService(
        CurriculumVitaeMediaRepository curriculumVitaeMediaRepository,
        CurriculumVitaeMediaMapper curriculumVitaeMediaMapper
    ) {
        this.curriculumVitaeMediaRepository = curriculumVitaeMediaRepository;
        this.curriculumVitaeMediaMapper = curriculumVitaeMediaMapper;
    }

    /**
     * Save a curriculumVitaeMedia.
     *
     * @param curriculumVitaeMediaDTO the entity to save.
     * @return the persisted entity.
     */
    public CurriculumVitaeMediaDTO save(CurriculumVitaeMediaDTO curriculumVitaeMediaDTO) {
        log.debug("Request to save CurriculumVitaeMedia : {}", curriculumVitaeMediaDTO);
        CurriculumVitaeMedia curriculumVitaeMedia = curriculumVitaeMediaMapper.toEntity(curriculumVitaeMediaDTO);
        curriculumVitaeMedia = curriculumVitaeMediaRepository.save(curriculumVitaeMedia);
        return curriculumVitaeMediaMapper.toDto(curriculumVitaeMedia);
    }

    /**
     * Update a curriculumVitaeMedia.
     *
     * @param curriculumVitaeMediaDTO the entity to save.
     * @return the persisted entity.
     */
    public CurriculumVitaeMediaDTO update(CurriculumVitaeMediaDTO curriculumVitaeMediaDTO) {
        log.debug("Request to update CurriculumVitaeMedia : {}", curriculumVitaeMediaDTO);
        CurriculumVitaeMedia curriculumVitaeMedia = curriculumVitaeMediaMapper.toEntity(curriculumVitaeMediaDTO);
        curriculumVitaeMedia = curriculumVitaeMediaRepository.save(curriculumVitaeMedia);
        return curriculumVitaeMediaMapper.toDto(curriculumVitaeMedia);
    }

    /**
     * Partially update a curriculumVitaeMedia.
     *
     * @param curriculumVitaeMediaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CurriculumVitaeMediaDTO> partialUpdate(CurriculumVitaeMediaDTO curriculumVitaeMediaDTO) {
        log.debug("Request to partially update CurriculumVitaeMedia : {}", curriculumVitaeMediaDTO);

        return curriculumVitaeMediaRepository
            .findById(curriculumVitaeMediaDTO.getId())
            .map(existingCurriculumVitaeMedia -> {
                curriculumVitaeMediaMapper.partialUpdate(existingCurriculumVitaeMedia, curriculumVitaeMediaDTO);

                return existingCurriculumVitaeMedia;
            })
            .map(curriculumVitaeMediaRepository::save)
            .map(curriculumVitaeMediaMapper::toDto);
    }

    /**
     * Get all the curriculumVitaeMedias.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CurriculumVitaeMediaDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CurriculumVitaeMedias");
        return curriculumVitaeMediaRepository.findAllWithEagerRelationships(pageable).map(curriculumVitaeMediaMapper::toDto);
    }
    /**
     * Get all the curriculumVitaeMedias with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<CurriculumVitaeMediaDTO> findAllWithEagerRelationships(Pageable pageable) {
        return curriculumVitaeMediaRepository.findAllWithEagerRelationships(pageable).map(curriculumVitaeMediaMapper::toDto);
    }

    /**
     * Get one curriculumVitaeMedia by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CurriculumVitaeMediaDTO> findOne(Long id) {
        log.debug("Request to get CurriculumVitaeMedia : {}", id);
        return curriculumVitaeMediaRepository.findOneWithEagerRelationships(id).map(curriculumVitaeMediaMapper::toDto);
    }

    /**
     * Delete the curriculumVitaeMedia by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete CurriculumVitaeMedia : {}", id);
        curriculumVitaeMediaRepository.deleteById(id);
    }
}
