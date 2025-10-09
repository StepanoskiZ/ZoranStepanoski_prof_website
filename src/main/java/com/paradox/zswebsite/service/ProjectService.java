package com.paradox.zswebsite.service;

import com.paradox.zswebsite.domain.Project;
import com.paradox.zswebsite.domain.ProjectMedia;
import com.paradox.zswebsite.repository.ProjectMediaRepository;
import com.paradox.zswebsite.repository.ProjectRepository;
import com.paradox.zswebsite.service.dto.ProjectCardDTO;
import com.paradox.zswebsite.service.dto.ProjectDTO;
import com.paradox.zswebsite.service.dto.ProjectDetailDTO;
import com.paradox.zswebsite.service.dto.ProjectMediaDTO;
import com.paradox.zswebsite.service.mapper.ProjectMapper;
import com.paradox.zswebsite.service.mapper.ProjectMediaMapper;
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
 * Service Implementation for managing {@link com.paradox.zswebsite.domain.Project}.
 */
@Service
@Transactional
public class ProjectService {

    private final Logger log = LoggerFactory.getLogger(ProjectService.class);
    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;
    private final ProjectMediaRepository projectMediaRepository;
    private final ProjectMediaMapper projectMediaMapper;

    public ProjectService(
        ProjectRepository projectRepository,
        ProjectMapper projectMapper,
        ProjectMediaRepository projectMediaRepository,
        ProjectMediaMapper projectMediaMapper
    ) {
        this.projectRepository = projectRepository;
        this.projectMapper = projectMapper;
        this.projectMediaRepository = projectMediaRepository;
        this.projectMediaMapper = projectMediaMapper;
    }

    /**
     * Save a project.
     *
     * @param projectDTO the entity to save.
     * @return the persisted entity.
     */
    public ProjectDTO save(ProjectDTO projectDTO) {
        log.debug("Request to save Project : {}", projectDTO);
        // --- FIX: Clean the HTML content before saving ---
        projectDTO.setDescriptionHTML(cleanContentHtml(projectDTO.getDescriptionHTML()));

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
        log.debug("Request to update Project : {}", projectDTO);
        // --- FIX: Clean the HTML content before updating ---
        projectDTO.setDescriptionHTML(cleanContentHtml(projectDTO.getDescriptionHTML()));

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
        log.debug("Request to partially update Project : {}", projectDTO);
        // --- FIX: Clean the HTML content before partial update ---
        projectDTO.setDescriptionHTML(cleanContentHtml(projectDTO.getDescriptionHTML()));

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
     * Get all the projects.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ProjectDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Projects");
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
        log.debug("Request to get Project : {}", id);
        return projectRepository.findById(id).map(projectMapper::toDto);
    }

    /**
     * Delete the project by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Project : {}", id);
        projectRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<ProjectCardDTO> findAllCards() {
        log.debug("Request to get all Projects for landing page");
        return projectRepository
            .findAllWithEagerRelationships()
            .stream()
            .map(this::mapProjectToCardDTO)
            .collect(Collectors.toList());
    }

    private ProjectCardDTO mapProjectToCardDTO(Project project) {
        ProjectCardDTO dto = new ProjectCardDTO();
        dto.setId(project.getId());
        dto.setTitle(project.getTitle() != null ? project.getTitle() : "");
        dto.setDescription(project.getDescriptionHTML());
        dto.setStatus(project.getStatus());

        project
            .getMedia()
            .stream()
            .min(Comparator.comparing(ProjectMedia::getId))
            .ifPresent(firstMedia -> {
                dto.setFirstMediaUrl(firstMedia.getMediaUrl());
                dto.setFirstMediaType(firstMedia.getProjectMediaType());
            });
        return dto;
    }

    @Transactional(readOnly = true)
    public Optional<ProjectDetailDTO> findOneWithDetails(Long id) {
        log.debug("Request to get detailed Project with media : {}", id);
        return projectRepository
            .findOneWithEagerRelationships(id)
            .map(project -> {
                ProjectDetailDTO dto = new ProjectDetailDTO();
                dto.setId(project.getId());
                dto.setTitle(project.getTitle());
                dto.setDescription(project.getDescriptionHTML());
                List<ProjectMediaDTO> mediaDTOs = project
                    .getMedia()
                    .stream()
                    .map(projectMediaMapper::toDto)
                    .sorted(Comparator.comparing(ProjectMediaDTO::getId)) // Sort by ID
                    .collect(Collectors.toList());
                dto.setMediaFiles(mediaDTOs);
                return dto;
            });
    }
}
