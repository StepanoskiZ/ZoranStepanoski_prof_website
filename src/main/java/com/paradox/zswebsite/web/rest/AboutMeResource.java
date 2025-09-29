package com.paradox.zswebsite.web.rest;

import com.paradox.zswebsite.repository.AboutMeRepository;
import com.paradox.zswebsite.service.AboutMeService;
import com.paradox.zswebsite.service.dto.AboutMeDTO;
import com.paradox.zswebsite.service.mapper.AboutMeMapper;
import com.paradox.zswebsite.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
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
 * REST controller for managing {@link com.paradox.zswebsite.domain.AboutMe}.
 */
@RestController
@RequestMapping("/api/about-me")
public class AboutMeResource {

    private final Logger log = LoggerFactory.getLogger(AboutMeResource.class);

    private static final String ENTITY_NAME = "aboutMe";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AboutMeService aboutMeService;
    private final AboutMeRepository aboutMeRepository;
    private final AboutMeMapper aboutMeMapper;

    public AboutMeResource(
        AboutMeService aboutMeService,
        AboutMeRepository aboutMeRepository,
        AboutMeMapper aboutMeMapper
    ) {
        this.aboutMeService = aboutMeService;
        this.aboutMeRepository = aboutMeRepository;
        this.aboutMeMapper = aboutMeMapper;
    }

    /**
     * {@code POST  /about-me} : Create a new aboutMe.
     *
     * @param aboutMeDTO the aboutMeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new aboutMeDTO, or with status {@code 400 (Bad Request)} if the aboutMe has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AboutMeDTO> createAboutMe(@Valid @RequestBody AboutMeDTO aboutMeDTO) throws URISyntaxException {
        log.debug("REST request to save AboutMe : {}", aboutMeDTO);
        if (aboutMeDTO.getId() != null) {
            throw new BadRequestAlertException("A new aboutMe cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AboutMeDTO result = aboutMeService.save(aboutMeDTO);
        return ResponseEntity.created(new URI("/api/about-me/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /about-me/:id} : Updates an existing aboutMe.
     *
     * @param id the id of the aboutMeDTO to save.
     * @param aboutMeDTO the aboutMeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated aboutMeDTO,
     * or with status {@code 400 (Bad Request)} if the aboutMeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the aboutMeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AboutMeDTO> updateAboutMe(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AboutMeDTO aboutMeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update AboutMe : {}, {}", id, aboutMeDTO);
        if (aboutMeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, aboutMeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!aboutMeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AboutMeDTO result = aboutMeService.update(aboutMeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, aboutMeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /about-me/:id} : Partial updates given fields of an existing aboutMe, field will ignore if it is null
     *
     * @param id the id of the aboutMeDTO to save.
     * @param aboutMeDTO the aboutMeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated aboutMeDTO,
     * or with status {@code 400 (Bad Request)} if the aboutMeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the aboutMeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the aboutMeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AboutMeDTO> partialUpdateAboutMe(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AboutMeDTO aboutMeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update AboutMe partially : {}, {}", id, aboutMeDTO);
        if (aboutMeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, aboutMeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!aboutMeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AboutMeDTO> result = aboutMeService.partialUpdate(aboutMeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, aboutMeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /about-me/:id} : get the "id" aboutMe.
     *
     * @param id the id of the aboutMeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the aboutMeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AboutMeDTO> getAboutMe(@PathVariable("id") Long id) {
        log.debug("REST request to get AboutMe : {}", id);
        Optional<AboutMeDTO> aboutMeDTO = aboutMeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(aboutMeDTO);
    }

    /**
     * {@code DELETE  /about-me/:id} : delete the "id" aboutMe.
     *
     * @param id the id of the aboutMeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAboutMe(@PathVariable("id") Long id) {
        log.debug("REST request to delete AboutMe : {}", id);
        aboutMeService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code GET  /about-me} : get all the aboutMes.
     * This is the endpoint that the JHipster admin page calls.
     * It MUST return a paginated list.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of aboutMes in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AboutMeDTO>> getAllAboutMe(Pageable pageable) {
        log.debug("REST request to get a page of AboutMe");
        // This service call correctly fetches a "page" of results, even if it's just one.
        Page<AboutMeDTO> page = aboutMeService.findAll(pageable);

        // This utility creates the necessary headers (like X-Total-Count) that the frontend needs.
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);

        // This returns the response with the headers and the body, which is a List<AboutMeDTO>.
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /api/about-me/first} : get the first AboutMe entity.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the aboutMeDTO,
     * or with status {@code 404 (Not Found)} if no AboutMe entity exists.
     */
    @GetMapping("/first")
    public ResponseEntity<AboutMeDTO> getFirstAboutMe() {
        log.debug("REST request to get first AboutMe");
        return ResponseUtil.wrapOrNotFound(aboutMeService.findFirst());
    }

    /**
     * GET /details : get the first aboutMe entry with all details.
     * This endpoint is specifically for the modal and guarantees media files are included.
     *
     * @return the ResponseEntity with status 200 (OK) and the aboutMeDTO in body.
     */
    @GetMapping("/details")
    public ResponseEntity<AboutMeDTO> getFirstAboutMeWithDetails() {
        log.debug("REST request to get first AboutMe with all details for modal");
        // The findFirst() method in your service already does the heavy lifting
        return ResponseUtil.wrapOrNotFound(aboutMeService.findFirst());
    }
}
