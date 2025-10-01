package com.paradox.zswebsite.web.rest;

import com.paradox.zswebsite.repository.CurriculumVitaeMediaRepository;
import com.paradox.zswebsite.service.CurriculumVitaeMediaService;
import com.paradox.zswebsite.service.dto.CurriculumVitaeMediaDTO;
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
 * REST controller for managing {@link com.paradox.zswebsite.domain.CurriculumVitaeMedia}.
 */
@RestController
@RequestMapping("/api/curriculum-vitae-media")
public class CurriculumVitaeMediaResource {

    private final Logger log = LoggerFactory.getLogger(CurriculumVitaeMediaResource.class);

    private static final String ENTITY_NAME = "curriculumVitaeMedia";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CurriculumVitaeMediaService curriculumVitaeMediaService;

    private final CurriculumVitaeMediaRepository curriculumVitaeMediaRepository;

    public CurriculumVitaeMediaResource(
        CurriculumVitaeMediaService curriculumVitaeMediaService,
        CurriculumVitaeMediaRepository curriculumVitaeMediaRepository
    ) {
        this.curriculumVitaeMediaService = curriculumVitaeMediaService;
        this.curriculumVitaeMediaRepository = curriculumVitaeMediaRepository;
    }

    /**
     * {@code POST  /curriculum-vitae-medias} : Create a new curriculumVitaeMedia.
     *
     * @param curriculumVitaeMediaDTO the curriculumVitaeMediaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new curriculumVitaeMediaDTO, or with status {@code 400 (Bad Request)} if the curriculumVitaeMedia has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CurriculumVitaeMediaDTO> createCurriculumVitaeMedia(
        @Valid @RequestBody CurriculumVitaeMediaDTO curriculumVitaeMediaDTO
    ) throws URISyntaxException {
        log.debug("REST request to save CurriculumVitaeMedia : {}", curriculumVitaeMediaDTO);
        if (curriculumVitaeMediaDTO.getId() != null) {
            throw new BadRequestAlertException("A new curriculumVitaeMedia cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CurriculumVitaeMediaDTO result = curriculumVitaeMediaService.save(curriculumVitaeMediaDTO);
        return ResponseEntity.created(new URI("/api/curriculum-vitae-media/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /curriculum-vitae-medias/:id} : Updates an existing curriculumVitaeMedia.
     *
     * @param id the id of the curriculumVitaeMediaDTO to save.
     * @param curriculumVitaeMediaDTO the curriculumVitaeMediaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated curriculumVitaeMediaDTO,
     * or with status {@code 400 (Bad Request)} if the curriculumVitaeMediaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the curriculumVitaeMediaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CurriculumVitaeMediaDTO> updateCurriculumVitaeMedia(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CurriculumVitaeMediaDTO curriculumVitaeMediaDTO
    ) throws URISyntaxException {
        log.debug("REST request to update CurriculumVitaeMedia : {}, {}", id, curriculumVitaeMediaDTO);
        if (curriculumVitaeMediaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, curriculumVitaeMediaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!curriculumVitaeMediaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CurriculumVitaeMediaDTO result = curriculumVitaeMediaService.update(curriculumVitaeMediaDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, curriculumVitaeMediaDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /curriculum-vitae-medias/:id} : Partial updates given fields of an existing curriculumVitaeMedia, field will ignore if it is null
     *
     * @param id the id of the curriculumVitaeMediaDTO to save.
     * @param curriculumVitaeMediaDTO the curriculumVitaeMediaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated curriculumVitaeMediaDTO,
     * or with status {@code 400 (Bad Request)} if the curriculumVitaeMediaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the curriculumVitaeMediaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the curriculumVitaeMediaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CurriculumVitaeMediaDTO> partialUpdateCurriculumVitaeMedia(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CurriculumVitaeMediaDTO curriculumVitaeMediaDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update CurriculumVitaeMedia partially : {}, {}", id, curriculumVitaeMediaDTO);
        if (curriculumVitaeMediaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, curriculumVitaeMediaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!curriculumVitaeMediaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CurriculumVitaeMediaDTO> result = curriculumVitaeMediaService.partialUpdate(curriculumVitaeMediaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, curriculumVitaeMediaDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /curriculum-vitae-medias} : get all the curriculumVitaeMedias.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of curriculumVitaeMedias in body.
     */
    @GetMapping("")
    public ResponseEntity<List<CurriculumVitaeMediaDTO>> getAllCurriculumVitaeMedias(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of CurriculumVitaeMedias");
        Page<CurriculumVitaeMediaDTO> page;
        if (eagerload) {
            page = curriculumVitaeMediaService.findAllWithEagerRelationships(pageable);
        } else {
            page = curriculumVitaeMediaService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /curriculum-vitae-medias/:id} : get the "id" curriculumVitaeMedia.
     *
     * @param id the id of the curriculumVitaeMediaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the curriculumVitaeMediaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CurriculumVitaeMediaDTO> getCurriculumVitaeMedia(@PathVariable("id") Long id) {
        log.debug("REST request to get CurriculumVitaeMedia : {}", id);
        Optional<CurriculumVitaeMediaDTO> curriculumVitaeMediaDTO = curriculumVitaeMediaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(curriculumVitaeMediaDTO);
    }

    /**
     * {@code DELETE  /curriculum-vitae-medias/:id} : delete the "id" curriculumVitaeMedia.
     *
     * @param id the id of the curriculumVitaeMediaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCurriculumVitaeMedia(@PathVariable("id") Long id) {
        log.debug("REST request to delete CurriculumVitaeMedia : {}", id);
        curriculumVitaeMediaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
