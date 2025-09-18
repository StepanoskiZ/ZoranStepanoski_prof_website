package com.paradox.zswebsite.service;

import com.paradox.zswebsite.domain.Project;
import com.paradox.zswebsite.domain.ProjectImage;
import com.paradox.zswebsite.repository.ProjectImageRepository;
import com.paradox.zswebsite.repository.ProjectRepository;
import com.paradox.zswebsite.service.dto.ProjectCardDTO;
import com.paradox.zswebsite.service.dto.ProjectDTO;
import com.paradox.zswebsite.service.dto.ProjectDetailDTO;
import com.paradox.zswebsite.service.mapper.ProjectMapper;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.paradox.zswebsite.domain.Project}.
 */
@Service
@Transactional
public class ProjectService {

    private static final Logger LOG = LoggerFactory.getLogger(ProjectService.class);
    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;
    private final ProjectImageRepository projectImageRepository;

    public ProjectService(ProjectRepository projectRepository, ProjectMapper projectMapper, ProjectImageRepository projectImageRepository) {
        this.projectRepository = projectRepository;
        this.projectMapper = projectMapper;
        this.projectImageRepository = projectImageRepository;
    }

    /**
     * Save a project.
     *
     * @param projectDTO the entity to save.
     * @return the persisted entity.
     */
    public ProjectDTO save(ProjectDTO projectDTO) {
        LOG.debug("Request to save Project : {}", projectDTO);
        Project project = projectMapper.toEntity(projectDTO);
        project = projectRepository.save(project);
        return projectMapper.toDto(project);
    }

    /**
     * Update a project.
     *
     * @param projectDTO the entity to save.
     * @return the persisted entity.
     */
    public ProjectDTO update(ProjectDTO projectDTO) {
        LOG.debug("Request to update Project : {}", projectDTO);
        Project project = projectMapper.toEntity(projectDTO);
        project = projectRepository.save(project);
        return projectMapper.toDto(project);
    }

    /**
     * Partially update a project.
     *
     * @param projectDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ProjectDTO> partialUpdate(ProjectDTO projectDTO) {
        LOG.debug("Request to partially update Project : {}", projectDTO);

        return projectRepository
            .findById(projectDTO.getId())
            .map(existingProject -> {
                projectMapper.partialUpdate(existingProject, projectDTO);

                return existingProject;
            })
            .map(projectRepository::save)
            .map(projectMapper::toDto);
    }

    /**
     * Get all the projects.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ProjectDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Projects");
        return projectRepository.findAll(pageable).map(projectMapper::toDto);
    }

    /**
     * Get one project by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ProjectDTO> findOne(Long id) {
        LOG.debug("Request to get Project : {}", id);
        return projectRepository.findById(id).map(projectMapper::toDto);
    }

    /**
     * Delete the project by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Project : {}", id);
        projectRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<ProjectCardDTO> findAllForLandingPage() {
        LOG.debug("Request to get all Projects for landing page");
        return projectRepository
            .findAll()
            .stream()
            .map(project -> {
                ProjectCardDTO dto = new ProjectCardDTO();
                dto.setId(project.getId());
                dto.setTitle(project.getTitle());
                // Truncate description for the card view
                if (project.getDescription() != null && project.getDescription().length() > 150) {
                    dto.setDescription(project.getDescription().substring(0, 150) + "...");
                } else {
                    dto.setDescription(project.getDescription());
                }

                // Find the first image for this project
                projectImageRepository
                    .findFirstByProjectId(project.getId())
                    .ifPresent(firstImage -> dto.setFirstImageUrl(firstImage.getImageUrl()));

                return dto;
            })
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<ProjectDetailDTO> findOneWithImages(Long id) {
        LOG.debug("Request to get detailed Project with images : {}", id);
        return projectRepository
            .findById(id)
            .map(project -> {
                ProjectDetailDTO dto = new ProjectDetailDTO();
                dto.setId(project.getId());
                dto.setTitle(project.getTitle());
                dto.setDescription(project.getDescription()); // Full description
                dto.setUrl(project.getProjectUrl());

                // Fetch all images and extract their URLs
                List<String> imageUrls = projectImageRepository
                    .findAllByProjectId(project.getId())
                    .stream()
                    .map(ProjectImage::getImageUrl)
                    .collect(Collectors.toList());
                dto.setImageUrls(imageUrls);

                return dto;
            });
    }
}
