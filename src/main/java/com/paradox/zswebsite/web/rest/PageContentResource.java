package com.paradox.zswebsite.web.rest;

import com.paradox.zswebsite.repository.PageContentRepository;
import com.paradox.zswebsite.service.PageContentService;
import com.paradox.zswebsite.service.dto.PageContentDTO;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.paradox.zswebsite.domain.PageContent}.
 */
@RestController
@RequestMapping("/api/page-contents")
public class PageContentResource {

    private final Logger log = LoggerFactory.getLogger(PageContentResource.class);

    private static final String ENTITY_NAME = "pageContent";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PageContentService pageContentService;

    private final PageContentRepository pageContentRepository;

    public PageContentResource(PageContentService pageContentService, PageContentRepository pageContentRepository) {
        this.pageContentService = pageContentService;
        this.pageContentRepository = pageContentRepository;
    }

    /**
     * {@code POST  /page-contents} : Create a new pageContent.
     *
     * @param pageContentDTO the pageContentDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new pageContentDTO, or with status {@code 400 (Bad Request)} if the pageContent has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PageContentDTO> createPageContent(@Valid @RequestBody PageContentDTO pageContentDTO) throws URISyntaxException {
        log.debug("REST request to save PageContent : {}", pageContentDTO);
        if (pageContentDTO.getId() != null) {
            throw new BadRequestAlertException("A new pageContent cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PageContentDTO result = pageContentService.save(pageContentDTO);
        return ResponseEntity.created(new URI("/api/page-contents/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /page-contents/:id} : Updates an existing pageContent.
     *
     * @param id the id of the pageContentDTO to save.
     * @param pageContentDTO the pageContentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pageContentDTO,
     * or with status {@code 400 (Bad Request)} if the pageContentDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the pageContentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PageContentDTO> updatePageContent(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PageContentDTO pageContentDTO
    ) throws URISyntaxException {
        log.debug("REST request to update PageContent : {}, {}", id, pageContentDTO);
        if (pageContentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pageContentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pageContentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PageContentDTO result = pageContentService.update(pageContentDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pageContentDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /page-contents/:id} : Partial updates given fields of an existing pageContent, field will ignore if it is null
     *
     * @param id the id of the pageContentDTO to save.
     * @param pageContentDTO the pageContentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pageContentDTO,
     * or with status {@code 400 (Bad Request)} if the pageContentDTO is not valid,
     * or with status {@code 404 (Not Found)} if the pageContentDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the pageContentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PageContentDTO> partialUpdatePageContent(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PageContentDTO pageContentDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update PageContent partially : {}, {}", id, pageContentDTO);
        if (pageContentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pageContentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pageContentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PageContentDTO> result = pageContentService.partialUpdate(pageContentDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pageContentDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /page-contents} : get all the pageContents.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of pageContents in body.
     */
    @GetMapping("")
    public ResponseEntity<List<PageContentDTO>> getAllPageContents(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of PageContents");
        Page<PageContentDTO> page = pageContentService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /page-contents/:id} : get the "id" pageContent.
     *
     * @param id the id of the pageContentDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the pageContentDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PageContentDTO> getPageContent(@PathVariable("id") Long id) {
        log.debug("REST request to get PageContent : {}", id);
        Optional<PageContentDTO> pageContentDTO = pageContentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(pageContentDTO);
    }

    /**
     * {@code DELETE  /page-contents/:id} : delete the "id" pageContent.
     *
     * @param id the id of the pageContentDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePageContent(@PathVariable("id") Long id) {
        log.debug("REST request to delete PageContent : {}", id);
        pageContentService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code GET  /page-contents/by-key/:key} : get the pageContent by its "key".
     *
     * @param key the sectionKey of the pageContentDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the pageContentDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/by-key/{key}")
    public ResponseEntity<PageContentDTO> getPageContentByKey(@PathVariable("key") String key) {
        log.debug("REST request to get PageContent by key : {}", key);
        Optional<PageContentDTO> pageContentDTO = pageContentService.findOneBySectionKey(key);
        return ResponseUtil.wrapOrNotFound(pageContentDTO);
    }
}
