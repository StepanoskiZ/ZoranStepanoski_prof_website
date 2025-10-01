package com.paradox.zswebsite.web.rest;

import com.paradox.zswebsite.repository.AboutMeMediaRepository;
import com.paradox.zswebsite.service.AboutMeMediaService;
import com.paradox.zswebsite.service.dto.AboutMeMediaDTO;
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
 * REST controller for managing {@link com.paradox.zswebsite.domain.AboutMeMedia}.
 */
@RestController
@RequestMapping("/api/about-me-media")
public class AboutMeMediaResource {

    private final Logger log = LoggerFactory.getLogger(AboutMeMediaResource.class);

    private static final String ENTITY_NAME = "aboutMeMedia";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AboutMeMediaService aboutMeMediaService;

    private final AboutMeMediaRepository aboutMeMediaRepository;

    public AboutMeMediaResource(AboutMeMediaService aboutMeMediaService, AboutMeMediaRepository aboutMeMediaRepository) {
        this.aboutMeMediaService = aboutMeMediaService;
        this.aboutMeMediaRepository = aboutMeMediaRepository;
    }

    /**
     * {@code POST  /about-me-medias} : Create a new aboutMeMedia.
     *
     * @param aboutMeMediaDTO the aboutMeMediaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new aboutMeMediaDTO, or with status {@code 400 (Bad Request)} if the aboutMeMedia has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AboutMeMediaDTO> createAboutMeMedia(@Valid @RequestBody AboutMeMediaDTO aboutMeMediaDTO)
        throws URISyntaxException {
        log.debug("REST request to save AboutMeMedia : {}", aboutMeMediaDTO);
        if (aboutMeMediaDTO.getId() != null) {
            throw new BadRequestAlertException("A new aboutMeMedia cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AboutMeMediaDTO result = aboutMeMediaService.save(aboutMeMediaDTO);
        return ResponseEntity
            .created(new URI("/api/about-me-media/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /about-me-medias/:id} : Updates an existing aboutMeMedia.
     *
     * @param id the id of the aboutMeMediaDTO to save.
     * @param aboutMeMediaDTO the aboutMeMediaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated aboutMeMediaDTO,
     * or with status {@code 400 (Bad Request)} if the aboutMeMediaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the aboutMeMediaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AboutMeMediaDTO> updateAboutMeMedia(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AboutMeMediaDTO aboutMeMediaDTO
    ) throws URISyntaxException {
        log.debug("REST request to update AboutMeMedia : {}, {}", id, aboutMeMediaDTO);
        if (aboutMeMediaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, aboutMeMediaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!aboutMeMediaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AboutMeMediaDTO result = aboutMeMediaService.update(aboutMeMediaDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, aboutMeMediaDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /about-me-medias/:id} : Partial updates given fields of an existing aboutMeMedia, field will ignore if it is null
     *
     * @param id the id of the aboutMeMediaDTO to save.
     * @param aboutMeMediaDTO the aboutMeMediaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated aboutMeMediaDTO,
     * or with status {@code 400 (Bad Request)} if the aboutMeMediaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the aboutMeMediaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the aboutMeMediaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AboutMeMediaDTO> partialUpdateAboutMeMedia(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AboutMeMediaDTO aboutMeMediaDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update AboutMeMedia partially : {}, {}", id, aboutMeMediaDTO);
        if (aboutMeMediaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, aboutMeMediaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!aboutMeMediaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AboutMeMediaDTO> result = aboutMeMediaService.partialUpdate(aboutMeMediaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, aboutMeMediaDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /about-me-medias} : get all the aboutMeMedias.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of aboutMeMedias in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AboutMeMediaDTO>> getAllAboutMeMedias(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of AboutMeMedias");
        Page<AboutMeMediaDTO> page = aboutMeMediaService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /about-me-medias/:id} : get the "id" aboutMeMedia.
     *
     * @param id the id of the aboutMeMediaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the aboutMeMediaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AboutMeMediaDTO> getAboutMeMedia(@PathVariable("id") Long id) {
        log.debug("REST request to get AboutMeMedia : {}", id);
        Optional<AboutMeMediaDTO> aboutMeMediaDTO = aboutMeMediaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(aboutMeMediaDTO);
    }

    /**
     * {@code DELETE  /about-me-medias/:id} : delete the "id" aboutMeMedia.
     *
     * @param id the id of the aboutMeMediaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAboutMeMedia(@PathVariable("id") Long id) {
        log.debug("REST request to delete AboutMeMedia : {}", id);
        aboutMeMediaService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
