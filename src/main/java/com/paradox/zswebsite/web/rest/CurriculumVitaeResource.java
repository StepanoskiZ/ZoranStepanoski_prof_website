package com.paradox.zswebsite.web.rest;

import com.paradox.zswebsite.repository.CurriculumVitaeRepository;
import com.paradox.zswebsite.service.CurriculumVitaeService;
import com.paradox.zswebsite.service.dto.CurriculumVitaeCardDTO;
import com.paradox.zswebsite.service.dto.CurriculumVitaeDTO;
import com.paradox.zswebsite.service.dto.CurriculumVitaeDetailDTO;
import com.paradox.zswebsite.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
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
 * REST controller for managing {@link com.paradox.zswebsite.domain.CurriculumVitae}.
 */
@RestController
@RequestMapping("/api")
public class CurriculumVitaeResource {

    private final Logger log = LoggerFactory.getLogger(CurriculumVitaeResource.class);

    private static final String ENTITY_NAME = "curriculumVitae";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CurriculumVitaeService curriculumVitaeService;

    private final CurriculumVitaeRepository curriculumVitaeRepository;

    public CurriculumVitaeResource(CurriculumVitaeService curriculumVitaeService, CurriculumVitaeRepository curriculumVitaeRepository) {
        this.curriculumVitaeService = curriculumVitaeService;
        this.curriculumVitaeRepository = curriculumVitaeRepository;
    }

    /**
     * {@code POST  /curriculum-vitae} : Create a new curriculumVitae.
     *
     * @param curriculumVitaeDTO the curriculumVitaeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new curriculumVitaeDTO, or with status {@code 400 (Bad Request)} if the curriculumVitae has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/curriculum-vitae")
    public ResponseEntity<CurriculumVitaeDTO> createCurriculumVitae(@Valid @RequestBody CurriculumVitaeDTO curriculumVitaeDTO)
        throws URISyntaxException {
        log.debug("REST request to save CurriculumVitae : {}", curriculumVitaeDTO);
        if (curriculumVitaeDTO.getId() != null) {
            throw new BadRequestAlertException("A new curriculumVitae cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CurriculumVitaeDTO result = curriculumVitaeService.save(curriculumVitaeDTO);
        return ResponseEntity.created(new URI("/api/curriculum-vitae/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /curriculum-vitae/:id} : Updates an existing curriculumVitae.
     *
     * @param id the id of the curriculumVitaeDTO to save.
     * @param curriculumVitaeDTO the curriculumVitaeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated curriculumVitaeDTO,
     * or with status {@code 400 (Bad Request)} if the curriculumVitaeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the curriculumVitaeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/curriculum-vitae/{id}")
    public ResponseEntity<CurriculumVitaeDTO> updateCurriculumVitae(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CurriculumVitaeDTO curriculumVitaeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update CurriculumVitae : {}, {}", id, curriculumVitaeDTO);
        if (curriculumVitaeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, curriculumVitaeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!curriculumVitaeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CurriculumVitaeDTO result = curriculumVitaeService.update(curriculumVitaeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, curriculumVitaeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /curriculum-vitae/:id} : Partial updates given fields of an existing curriculumVitae, field will ignore if it is null
     *
     * @param id the id of the curriculumVitaeDTO to save.
     * @param curriculumVitaeDTO the curriculumVitaeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated curriculumVitaeDTO,
     * or with status {@code 400 (Bad Request)} if the curriculumVitaeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the curriculumVitaeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the curriculumVitaeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/curriculum-vitae/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CurriculumVitaeDTO> partialUpdateCurriculumVitae(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CurriculumVitaeDTO curriculumVitaeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update CurriculumVitae partially : {}, {}", id, curriculumVitaeDTO);
        if (curriculumVitaeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, curriculumVitaeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!curriculumVitaeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CurriculumVitaeDTO> result = curriculumVitaeService.partialUpdate(curriculumVitaeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, curriculumVitaeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /curriculum-vitae} : get all the curriculumVitae.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of curriculumVitaes in body.
     */
    @GetMapping("/curriculum-vitae")
    public ResponseEntity<List<CurriculumVitaeDTO>> getAllCurriculumVitaes(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of CurriculumVitaes");
        Page<CurriculumVitaeDTO> page = curriculumVitaeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /curriculum-vitae/:id} : get the "id" curriculumVitae.
     *
     * @param id the id of the curriculumVitaeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the curriculumVitaeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/curriculum-vitae/{id}")
    public ResponseEntity<CurriculumVitaeDTO> getCurriculumVitae(@PathVariable("id") Long id) {
        log.debug("REST request to get CurriculumVitae : {}", id);
        Optional<CurriculumVitaeDTO> curriculumVitaeDTO = curriculumVitaeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(curriculumVitaeDTO);
    }

    /**
     * {@code DELETE  /curriculum-vitae/:id} : delete the "id" curriculumVitae.
     *
     * @param id the id of the curriculumVitaeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/curriculum-vitae/{id}")
    public ResponseEntity<Void> deleteCurriculumVitae(@PathVariable("id") Long id) {
        log.debug("REST request to delete CurriculumVitae : {}", id);
        curriculumVitaeService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code GET  /curriculum-vitae/cards} : get all curriculum vitae entries formatted for landing page cards.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of curriculumVitae cards in body.
     */
    @GetMapping("/curriculum-vitae/cards")
    public ResponseEntity<List<CurriculumVitaeCardDTO>> getCvCards() {
        log.debug("REST request to get all CurriculumVitae for cards");
        List<CurriculumVitaeCardDTO> cards = curriculumVitaeService.findAllCards();
        return ResponseEntity.ok(cards);
    }

    @GetMapping("/curriculum-vitae/{id}/details")
    public ResponseEntity<CurriculumVitaeDetailDTO> getCurriculumVitaeDetails(@PathVariable Long id) {
        log.debug("REST request to get detailed CurriculumVitae : {}", id);
        Optional<CurriculumVitaeDetailDTO> details = curriculumVitaeService.findOneWithDetails(id);
        return ResponseUtil.wrapOrNotFound(details);
    }
}
