package com.paradox.zswebsite.web.rest;

import com.paradox.zswebsite.repository.ProjectImageRepository;
import com.paradox.zswebsite.service.ProjectImageService;
import com.paradox.zswebsite.service.dto.ProjectImageDTO;
import com.paradox.zswebsite.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.paradox.zswebsite.domain.ProjectImage}.
 */
@RestController
@RequestMapping("/api/project-images")
public class ProjectImageResource {

    private static final Logger LOG = LoggerFactory.getLogger(ProjectImageResource.class);

    private static final String ENTITY_NAME = "projectImage";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProjectImageService projectImageService;

    private final ProjectImageRepository projectImageRepository;

    public ProjectImageResource(ProjectImageService projectImageService, ProjectImageRepository projectImageRepository) {
        this.projectImageService = projectImageService;
        this.projectImageRepository = projectImageRepository;
    }

    /**
     * {@code POST  /project-images} : Create a new projectImage.
     *
     * @param projectImageDTO the projectImageDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new projectImageDTO, or with status {@code 400 (Bad Request)} if the projectImage has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ProjectImageDTO> createProjectImage(@Valid @RequestBody ProjectImageDTO projectImageDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save ProjectImage : {}", projectImageDTO);
        if (projectImageDTO.getId() != null) {
            throw new BadRequestAlertException("A new projectImage cannot already have an ID", ENTITY_NAME, "idexists");
        }
        projectImageDTO = projectImageService.save(projectImageDTO);
        return ResponseEntity.created(new URI("/api/project-images/" + projectImageDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, projectImageDTO.getId().toString()))
            .body(projectImageDTO);
    }

    /**
     * {@code PUT  /project-images/:id} : Updates an existing projectImage.
     *
     * @param id the id of the projectImageDTO to save.
     * @param projectImageDTO the projectImageDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated projectImageDTO,
     * or with status {@code 400 (Bad Request)} if the projectImageDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the projectImageDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProjectImageDTO> updateProjectImage(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ProjectImageDTO projectImageDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ProjectImage : {}, {}", id, projectImageDTO);
        if (projectImageDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, projectImageDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!projectImageRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        projectImageDTO = projectImageService.update(projectImageDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, projectImageDTO.getId().toString()))
            .body(projectImageDTO);
    }

    /**
     * {@code PATCH  /project-images/:id} : Partial updates given fields of an existing projectImage, field will ignore if it is null
     *
     * @param id the id of the projectImageDTO to save.
     * @param projectImageDTO the projectImageDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated projectImageDTO,
     * or with status {@code 400 (Bad Request)} if the projectImageDTO is not valid,
     * or with status {@code 404 (Not Found)} if the projectImageDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the projectImageDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProjectImageDTO> partialUpdateProjectImage(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ProjectImageDTO projectImageDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ProjectImage partially : {}, {}", id, projectImageDTO);
        if (projectImageDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, projectImageDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!projectImageRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProjectImageDTO> result = projectImageService.partialUpdate(projectImageDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, projectImageDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /project-images} : get all the projectImages.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of projectImages in body.
     */
    @GetMapping("")
    public List<ProjectImageDTO> getAllProjectImages() {
        LOG.debug("REST request to get all ProjectImages");
        return projectImageService.findAll();
    }

    /**
     * {@code GET  /project-images/:id} : get the "id" projectImage.
     *
     * @param id the id of the projectImageDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the projectImageDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProjectImageDTO> getProjectImage(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ProjectImage : {}", id);
        Optional<ProjectImageDTO> projectImageDTO = projectImageService.findOne(id);
        return ResponseUtil.wrapOrNotFound(projectImageDTO);
    }

    /**
     * {@code DELETE  /project-images/:id} : delete the "id" projectImage.
     *
     * @param id the id of the projectImageDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProjectImage(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ProjectImage : {}", id);
        projectImageService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
