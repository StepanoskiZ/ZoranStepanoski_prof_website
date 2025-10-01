package com.paradox.zswebsite.web.rest;

import com.paradox.zswebsite.repository.BusinessServiceRepository;
import com.paradox.zswebsite.service.BusinessServiceService;
import com.paradox.zswebsite.service.dto.BusinessServiceDTO;
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
 * REST controller for managing {@link com.paradox.zswebsite.domain.BusinessService}.
 */
@RestController
@RequestMapping("/api/business-services")
public class BusinessServiceResource {

    private final Logger log = LoggerFactory.getLogger(BusinessServiceResource.class);

    private static final String ENTITY_NAME = "businessService";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BusinessServiceService businessServiceService;

    private final BusinessServiceRepository businessServiceRepository;

    public BusinessServiceResource(BusinessServiceService businessServiceService, BusinessServiceRepository businessServiceRepository) {
        this.businessServiceService = businessServiceService;
        this.businessServiceRepository = businessServiceRepository;
    }

    /**
     * {@code POST  /business-services} : Create a new businessService.
     *
     * @param businessServiceDTO the businessServiceDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new businessServiceDTO, or with status {@code 400 (Bad Request)} if the businessService has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<BusinessServiceDTO> createBusinessService(@Valid @RequestBody BusinessServiceDTO businessServiceDTO)
        throws URISyntaxException {
        log.debug("REST request to save BusinessService : {}", businessServiceDTO);
        if (businessServiceDTO.getId() != null) {
            throw new BadRequestAlertException("A new businessService cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BusinessServiceDTO result = businessServiceService.save(businessServiceDTO);
        return ResponseEntity
            .created(new URI("/api/business-services/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /business-services/:id} : Updates an existing businessService.
     *
     * @param id the id of the businessServiceDTO to save.
     * @param businessServiceDTO the businessServiceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated businessServiceDTO,
     * or with status {@code 400 (Bad Request)} if the businessServiceDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the businessServiceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<BusinessServiceDTO> updateBusinessService(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody BusinessServiceDTO businessServiceDTO
    ) throws URISyntaxException {
        log.debug("REST request to update BusinessService : {}, {}", id, businessServiceDTO);
        if (businessServiceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, businessServiceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!businessServiceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        BusinessServiceDTO result = businessServiceService.update(businessServiceDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, businessServiceDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /business-services/:id} : Partial updates given fields of an existing businessService, field will ignore if it is null
     *
     * @param id the id of the businessServiceDTO to save.
     * @param businessServiceDTO the businessServiceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated businessServiceDTO,
     * or with status {@code 400 (Bad Request)} if the businessServiceDTO is not valid,
     * or with status {@code 404 (Not Found)} if the businessServiceDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the businessServiceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<BusinessServiceDTO> partialUpdateBusinessService(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody BusinessServiceDTO businessServiceDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update BusinessService partially : {}, {}", id, businessServiceDTO);
        if (businessServiceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, businessServiceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!businessServiceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BusinessServiceDTO> result = businessServiceService.partialUpdate(businessServiceDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, businessServiceDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /business-services} : get all the businessServices.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of businessServices in body.
     */
    @GetMapping("")
    public ResponseEntity<List<BusinessServiceDTO>> getAllBusinessServices(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of BusinessServices");
        Page<BusinessServiceDTO> page = businessServiceService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /business-services/:id} : get the "id" businessService.
     *
     * @param id the id of the businessServiceDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the businessServiceDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<BusinessServiceDTO> getBusinessService(@PathVariable("id") Long id) {
        log.debug("REST request to get BusinessService : {}", id);
        Optional<BusinessServiceDTO> businessServiceDTO = businessServiceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(businessServiceDTO);
    }

    /**
     * {@code DELETE  /business-services/:id} : delete the "id" businessService.
     *
     * @param id the id of the businessServiceDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBusinessService(@PathVariable("id") Long id) {
        log.debug("REST request to delete BusinessService : {}", id);
        businessServiceService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
