package com.paradox.zswebsite.web.rest;

import com.paradox.zswebsite.repository.PageContentMediaRepository;
import com.paradox.zswebsite.service.PageContentMediaService;
import com.paradox.zswebsite.service.dto.PageContentMediaDTO;
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
 * REST controller for managing {@link com.paradox.zswebsite.domain.PageContentMedia}.
 */
@RestController
@RequestMapping("/api/page-content-medias")
public class PageContentMediaResource {

    private final Logger log = LoggerFactory.getLogger(PageContentMediaResource.class);

    private static final String ENTITY_NAME = "pageContentMedia";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PageContentMediaService pageContentMediaService;

    private final PageContentMediaRepository pageContentMediaRepository;

    public PageContentMediaResource(
        PageContentMediaService pageContentMediaService,
        PageContentMediaRepository pageContentMediaRepository
    ) {
        this.pageContentMediaService = pageContentMediaService;
        this.pageContentMediaRepository = pageContentMediaRepository;
    }

    /**
     * {@code POST  /page-content-medias} : Create a new pageContentMedia.
     *
     * @param pageContentMediaDTO the pageContentMediaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new pageContentMediaDTO, or with status {@code 400 (Bad Request)} if the pageContentMedia has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PageContentMediaDTO> createPageContentMedia(@Valid @RequestBody PageContentMediaDTO pageContentMediaDTO)
        throws URISyntaxException {
        log.debug("REST request to save PageContentMedia : {}", pageContentMediaDTO);
        if (pageContentMediaDTO.getId() != null) {
            throw new BadRequestAlertException("A new pageContentMedia cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PageContentMediaDTO result = pageContentMediaService.save(pageContentMediaDTO);
        return ResponseEntity.created(new URI("/api/page-content-medias/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /page-content-medias/:id} : Updates an existing pageContentMedia.
     *
     * @param id the id of the pageContentMediaDTO to save.
     * @param pageContentMediaDTO the pageContentMediaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pageContentMediaDTO,
     * or with status {@code 400 (Bad Request)} if the pageContentMediaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the pageContentMediaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PageContentMediaDTO> updatePageContentMedia(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PageContentMediaDTO pageContentMediaDTO
    ) throws URISyntaxException {
        log.debug("REST request to update PageContentMedia : {}, {}", id, pageContentMediaDTO);
        if (pageContentMediaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pageContentMediaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pageContentMediaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PageContentMediaDTO result = pageContentMediaService.update(pageContentMediaDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pageContentMediaDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /page-content-medias/:id} : Partial updates given fields of an existing pageContentMedia, field will ignore if it is null
     *
     * @param id the id of the pageContentMediaDTO to save.
     * @param pageContentMediaDTO the pageContentMediaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pageContentMediaDTO,
     * or with status {@code 400 (Bad Request)} if the pageContentMediaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the pageContentMediaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the pageContentMediaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PageContentMediaDTO> partialUpdatePageContentMedia(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PageContentMediaDTO pageContentMediaDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update PageContentMedia partially : {}, {}", id, pageContentMediaDTO);
        if (pageContentMediaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pageContentMediaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pageContentMediaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PageContentMediaDTO> result = pageContentMediaService.partialUpdate(pageContentMediaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pageContentMediaDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /page-content-medias} : get all the pageContentMedias.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of pageContentMedias in body.
     */
    @GetMapping("")
    public ResponseEntity<List<PageContentMediaDTO>> getAllPageContentMedias(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of PageContentMedias");
        Page<PageContentMediaDTO> page;
        if (eagerload) {
            page = pageContentMediaService.findAllWithEagerRelationships(pageable);
        } else {
            page = pageContentMediaService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /page-content-medias/:id} : get the "id" pageContentMedia.
     *
     * @param id the id of the pageContentMediaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the pageContentMediaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PageContentMediaDTO> getPageContentMedia(@PathVariable("id") Long id) {
        log.debug("REST request to get PageContentMedia : {}", id);
        Optional<PageContentMediaDTO> pageContentMediaDTO = pageContentMediaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(pageContentMediaDTO);
    }

    /**
     * {@code DELETE  /page-content-medias/:id} : delete the "id" pageContentMedia.
     *
     * @param id the id of the pageContentMediaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePageContentMedia(@PathVariable("id") Long id) {
        log.debug("REST request to delete PageContentMedia : {}", id);
        pageContentMediaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
