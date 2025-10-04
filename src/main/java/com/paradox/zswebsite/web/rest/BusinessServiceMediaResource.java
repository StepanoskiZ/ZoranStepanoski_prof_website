package com.paradox.zswebsite.web.rest;

import com.paradox.zswebsite.repository.BusinessServiceMediaRepository;
import com.paradox.zswebsite.service.BusinessServiceMediaService;
import com.paradox.zswebsite.service.dto.BusinessServiceMediaDTO;
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
 * REST controller for managing {@link com.paradox.zswebsite.domain.BusinessServiceMedia}.
 */
@RestController
@RequestMapping("/api/business-service-media")
public class BusinessServiceMediaResource {

    private final Logger log = LoggerFactory.getLogger(BusinessServiceMediaResource.class);

    private static final String ENTITY_NAME = "businessServiceMedia";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BusinessServiceMediaService businessServiceMediaService;

    private final BusinessServiceMediaRepository businessServiceMediaRepository;

    public BusinessServiceMediaResource(
        BusinessServiceMediaService businessServiceMediaService,
        BusinessServiceMediaRepository businessServiceMediaRepository
    ) {
        this.businessServiceMediaService = businessServiceMediaService;
        this.businessServiceMediaRepository = businessServiceMediaRepository;
    }

    /**
     * {@code POST  /business-service-medias} : Create a new businessServiceMedia.
     *
     * @param businessServiceMediaDTO the businessServiceMediaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new businessServiceMediaDTO, or with status {@code 400 (Bad Request)} if the businessServiceMedia has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<BusinessServiceMediaDTO> createBusinessServiceMedia(
        @Valid @RequestBody BusinessServiceMediaDTO businessServiceMediaDTO
    ) throws URISyntaxException {
        log.debug("REST request to save BusinessServiceMedia : {}", businessServiceMediaDTO);
        if (businessServiceMediaDTO.getId() != null) {
            throw new BadRequestAlertException("A new businessServiceMedia cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BusinessServiceMediaDTO result = businessServiceMediaService.save(businessServiceMediaDTO);
        return ResponseEntity
            .created(new URI("/api/business-service-media/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /business-service-medias/:id} : Updates an existing businessServiceMedia.
     *
     * @param id the id of the businessServiceMediaDTO to save.
     * @param businessServiceMediaDTO the businessServiceMediaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated businessServiceMediaDTO,
     * or with status {@code 400 (Bad Request)} if the businessServiceMediaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the businessServiceMediaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<BusinessServiceMediaDTO> updateBusinessServiceMedia(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody BusinessServiceMediaDTO businessServiceMediaDTO
    ) throws URISyntaxException {
        log.debug("REST request to update BusinessServiceMedia : {}, {}", id, businessServiceMediaDTO);
        if (businessServiceMediaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, businessServiceMediaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!businessServiceMediaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        BusinessServiceMediaDTO result = businessServiceMediaService.update(businessServiceMediaDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, businessServiceMediaDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /business-service-medias/:id} : Partial updates given fields of an existing businessServiceMedia, field will ignore if it is null
     *
     * @param id the id of the businessServiceMediaDTO to save.
     * @param businessServiceMediaDTO the businessServiceMediaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated businessServiceMediaDTO,
     * or with status {@code 400 (Bad Request)} if the businessServiceMediaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the businessServiceMediaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the businessServiceMediaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<BusinessServiceMediaDTO> partialUpdateBusinessServiceMedia(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody BusinessServiceMediaDTO businessServiceMediaDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update BusinessServiceMedia partially : {}, {}", id, businessServiceMediaDTO);
        if (businessServiceMediaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, businessServiceMediaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!businessServiceMediaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BusinessServiceMediaDTO> result = businessServiceMediaService.partialUpdate(businessServiceMediaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, businessServiceMediaDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /business-service-medias} : get all the businessServiceMedias.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of businessServiceMedias in body.
     */
    @GetMapping("")
    public ResponseEntity<List<BusinessServiceMediaDTO>> getAllBusinessServiceMedias(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "false") boolean eagerload
    ) {
        log.debug("REST request to get a page of BusinessServiceMedias");
        Page<BusinessServiceMediaDTO> page;
        if (eagerload) {
            page = businessServiceMediaService.findAllWithEagerRelationships(pageable);
        } else {
            page = businessServiceMediaService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
    /**
     * {@code GET  /business-service-medias/:id} : get the "id" businessServiceMedia.
     *
     * @param id the id of the businessServiceMediaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the businessServiceMediaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<BusinessServiceMediaDTO> getBusinessServiceMedia(@PathVariable("id") Long id) {
        log.debug("REST request to get BusinessServiceMedia : {}", id);
        Optional<BusinessServiceMediaDTO> businessServiceMediaDTO = businessServiceMediaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(businessServiceMediaDTO);
    }

    /**
     * {@code DELETE  /business-service-medias/:id} : delete the "id" businessServiceMedia.
     *
     * @param id the id of the businessServiceMediaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBusinessServiceMedia(@PathVariable("id") Long id) {
        log.debug("REST request to delete BusinessServiceMedia : {}", id);
        businessServiceMediaService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
