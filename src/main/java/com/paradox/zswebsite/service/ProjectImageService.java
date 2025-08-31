package com.paradox.zswebsite.service;

import com.paradox.zswebsite.domain.ProjectImage;
import com.paradox.zswebsite.repository.ProjectImageRepository;
import com.paradox.zswebsite.service.dto.ProjectImageDTO;
import com.paradox.zswebsite.service.mapper.ProjectImageMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.paradox.zswebsite.domain.ProjectImage}.
 */
@Service
@Transactional
public class ProjectImageService {

    private static final Logger LOG = LoggerFactory.getLogger(ProjectImageService.class);

    private final ProjectImageRepository projectImageRepository;

    private final ProjectImageMapper projectImageMapper;

    public ProjectImageService(ProjectImageRepository projectImageRepository, ProjectImageMapper projectImageMapper) {
        this.projectImageRepository = projectImageRepository;
        this.projectImageMapper = projectImageMapper;
    }

    /**
     * Save a projectImage.
     *
     * @param projectImageDTO the entity to save.
     * @return the persisted entity.
     */
    public ProjectImageDTO save(ProjectImageDTO projectImageDTO) {
        LOG.debug("Request to save ProjectImage : {}", projectImageDTO);
        ProjectImage projectImage = projectImageMapper.toEntity(projectImageDTO);
        projectImage = projectImageRepository.save(projectImage);
        return projectImageMapper.toDto(projectImage);
    }

    /**
     * Update a projectImage.
     *
     * @param projectImageDTO the entity to save.
     * @return the persisted entity.
     */
    public ProjectImageDTO update(ProjectImageDTO projectImageDTO) {
        LOG.debug("Request to update ProjectImage : {}", projectImageDTO);
        ProjectImage projectImage = projectImageMapper.toEntity(projectImageDTO);
        projectImage = projectImageRepository.save(projectImage);
        return projectImageMapper.toDto(projectImage);
    }

    /**
     * Partially update a projectImage.
     *
     * @param projectImageDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ProjectImageDTO> partialUpdate(ProjectImageDTO projectImageDTO) {
        LOG.debug("Request to partially update ProjectImage : {}", projectImageDTO);

        return projectImageRepository
            .findById(projectImageDTO.getId())
            .map(existingProjectImage -> {
                projectImageMapper.partialUpdate(existingProjectImage, projectImageDTO);

                return existingProjectImage;
            })
            .map(projectImageRepository::save)
            .map(projectImageMapper::toDto);
    }

    /**
     * Get all the projectImages.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ProjectImageDTO> findAll() {
        LOG.debug("Request to get all ProjectImages");
        return projectImageRepository.findAll().stream().map(projectImageMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one projectImage by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ProjectImageDTO> findOne(Long id) {
        LOG.debug("Request to get ProjectImage : {}", id);
        return projectImageRepository.findById(id).map(projectImageMapper::toDto);
    }

    /**
     * Delete the projectImage by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ProjectImage : {}", id);
        projectImageRepository.deleteById(id);
    }
}
