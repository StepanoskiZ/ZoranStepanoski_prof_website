package com.paradox.zswebsite.web.rest;

import com.paradox.zswebsite.repository.ProjectMediaRepository;
import com.paradox.zswebsite.service.ProjectMediaService;
import com.paradox.zswebsite.service.dto.ProjectMediaDTO;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.paradox.zswebsite.domain.ProjectMedia}.
 */
@RestController
@RequestMapping("/api/project-media")
public class ProjectMediaResource {

    private final Logger log = LoggerFactory.getLogger(ProjectMediaResource.class);

    private static final String ENTITY_NAME = "projectMedia";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProjectMediaService projectMediaService;

    private final ProjectMediaRepository projectMediaRepository;

    public ProjectMediaResource(ProjectMediaService projectMediaService, ProjectMediaRepository projectMediaRepository) {
        this.projectMediaService = projectMediaService;
        this.projectMediaRepository = projectMediaRepository;
    }

    /**
     * {@code POST  /project-medias} : Create a new projectMedia.
     *
     * @param projectMediaDTO the projectMediaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new projectMediaDTO, or with status {@code 400 (Bad Request)} if the projectMedia has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ProjectMediaDTO> createProjectMedia(@Valid @RequestBody ProjectMediaDTO projectMediaDTO)
        throws URISyntaxException {
        log.debug("REST request to save ProjectMedia : {}", projectMediaDTO);
        if (projectMediaDTO.getId() != null) {
            throw new BadRequestAlertException("A new projectMedia cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProjectMediaDTO result = projectMediaService.save(projectMediaDTO);
        return ResponseEntity.created(new URI("/api/project-media/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /project-medias/:id} : Updates an existing projectMedia.
     *
     * @param id the id of the projectMediaDTO to save.
     * @param projectMediaDTO the projectMediaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated projectMediaDTO,
     * or with status {@code 400 (Bad Request)} if the projectMediaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the projectMediaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProjectMediaDTO> updateProjectMedia(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ProjectMediaDTO projectMediaDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ProjectMedia : {}, {}", id, projectMediaDTO);
        if (projectMediaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, projectMediaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!projectMediaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ProjectMediaDTO result = projectMediaService.update(projectMediaDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, projectMediaDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /project-medias/:id} : Partial updates given fields of an existing projectMedia, field will ignore if it is null
     *
     * @param id the id of the projectMediaDTO to save.
     * @param projectMediaDTO the projectMediaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated projectMediaDTO,
     * or with status {@code 400 (Bad Request)} if the projectMediaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the projectMediaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the projectMediaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProjectMediaDTO> partialUpdateProjectMedia(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ProjectMediaDTO projectMediaDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ProjectMedia partially : {}, {}", id, projectMediaDTO);
        if (projectMediaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, projectMediaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!projectMediaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProjectMediaDTO> result = projectMediaService.partialUpdate(projectMediaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, projectMediaDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /project-medias} : get all the projectMedias.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of projectMedias in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ProjectMediaDTO>> getAllProjectMedias(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of ProjectMedias");
        Page<ProjectMediaDTO> page;
        if (eagerload) {
            page = projectMediaService.findAllWithEagerRelationships(pageable);
        } else {
            page = projectMediaService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /project-medias/:id} : get the "id" projectMedia.
     *
     * @param id the id of the projectMediaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the projectMediaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProjectMediaDTO> getProjectMedia(@PathVariable("id") Long id) {
        log.debug("REST request to get ProjectMedia : {}", id);
        Optional<ProjectMediaDTO> projectMediaDTO = projectMediaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(projectMediaDTO);
    }

    /**
     * {@code DELETE  /project-medias/:id} : delete the "id" projectMedia.
     *
     * @param id the id of the projectMediaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProjectMedia(@PathVariable("id") Long id) {
        log.debug("REST request to delete ProjectMedia : {}", id);
        projectMediaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
