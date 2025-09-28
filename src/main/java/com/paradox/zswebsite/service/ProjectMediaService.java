package com.paradox.zswebsite.service;

import com.paradox.zswebsite.domain.ProjectMedia;
import com.paradox.zswebsite.repository.ProjectMediaRepository;
import com.paradox.zswebsite.service.dto.ProjectMediaDTO;
import com.paradox.zswebsite.service.mapper.ProjectMediaMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.paradox.zswebsite.domain.ProjectMedia}.
 */
@Service
@Transactional
public class ProjectMediaService {

    private final Logger log = LoggerFactory.getLogger(ProjectMediaService.class);

    private final ProjectMediaRepository projectMediaRepository;

    private final ProjectMediaMapper projectMediaMapper;

    public ProjectMediaService(ProjectMediaRepository projectMediaRepository, ProjectMediaMapper projectMediaMapper) {
        this.projectMediaRepository = projectMediaRepository;
        this.projectMediaMapper = projectMediaMapper;
    }

    /**
     * Save a projectMedia.
     *
     * @param projectMediaDTO the entity to save.
     * @return the persisted entity.
     */
    public ProjectMediaDTO save(ProjectMediaDTO projectMediaDTO) {
        log.debug("Request to save ProjectMedia : {}", projectMediaDTO);
        ProjectMedia projectMedia = projectMediaMapper.toEntity(projectMediaDTO);
        projectMedia = projectMediaRepository.save(projectMedia);
        return projectMediaMapper.toDto(projectMedia);
    }

    /**
     * Update a projectMedia.
     *
     * @param projectMediaDTO the entity to save.
     * @return the persisted entity.
     */
    public ProjectMediaDTO update(ProjectMediaDTO projectMediaDTO) {
        log.debug("Request to update ProjectMedia : {}", projectMediaDTO);
        ProjectMedia projectMedia = projectMediaMapper.toEntity(projectMediaDTO);
        projectMedia = projectMediaRepository.save(projectMedia);
        return projectMediaMapper.toDto(projectMedia);
    }

    /**
     * Partially update a projectMedia.
     *
     * @param projectMediaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ProjectMediaDTO> partialUpdate(ProjectMediaDTO projectMediaDTO) {
        log.debug("Request to partially update ProjectMedia : {}", projectMediaDTO);

        return projectMediaRepository
            .findById(projectMediaDTO.getId())
            .map(existingProjectMedia -> {
                projectMediaMapper.partialUpdate(existingProjectMedia, projectMediaDTO);

                return existingProjectMedia;
            })
            .map(projectMediaRepository::save)
            .map(projectMediaMapper::toDto);
    }

    /**
     * Get all the projectMedias.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ProjectMediaDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ProjectMedias");
        return projectMediaRepository.findAll(pageable).map(projectMediaMapper::toDto);
    }

    /**
     * Get all the projectMedias with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<ProjectMediaDTO> findAllWithEagerRelationships(Pageable pageable) {
        return projectMediaRepository.findAllWithEagerRelationships(pageable).map(projectMediaMapper::toDto);
    }

    /**
     * Get one projectMedia by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ProjectMediaDTO> findOne(Long id) {
        log.debug("Request to get ProjectMedia : {}", id);
        return projectMediaRepository.findOneWithEagerRelationships(id).map(projectMediaMapper::toDto);
    }

    /**
     * Delete the projectMedia by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ProjectMedia : {}", id);
        projectMediaRepository.deleteById(id);
    }
}
