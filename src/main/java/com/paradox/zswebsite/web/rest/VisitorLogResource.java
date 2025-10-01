package com.paradox.zswebsite.web.rest;

import com.paradox.zswebsite.repository.VisitorLogRepository;
import com.paradox.zswebsite.service.VisitorLogService;
import com.paradox.zswebsite.service.dto.VisitorLogDTO;
import com.paradox.zswebsite.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.paradox.zswebsite.domain.VisitorLog}.
 */
@RestController
@RequestMapping("/api")
public class VisitorLogResource {

    private final Logger log = LoggerFactory.getLogger(VisitorLogResource.class);

    private static final String ENTITY_NAME = "visitorLog";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VisitorLogService visitorLogService;

    private final VisitorLogRepository visitorLogRepository;

    public VisitorLogResource(VisitorLogService visitorLogService, VisitorLogRepository visitorLogRepository) {
        this.visitorLogService = visitorLogService;
        this.visitorLogRepository = visitorLogRepository;
    }

    /**
     * {@code POST  /visitor-logs} : Create a new visitorLog.
     *
     * @param visitorLogDTO the visitorLogDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new visitorLogDTO, or with status {@code 400 (Bad Request)} if the visitorLog has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/visitor-logs")
    public ResponseEntity<VisitorLogDTO> createVisitorLog(@Valid @RequestBody VisitorLogDTO visitorLogDTO) throws URISyntaxException {
        log.debug("REST request to save VisitorLog : {}", visitorLogDTO);
        if (visitorLogDTO.getId() != null) {
            throw new BadRequestAlertException("A new visitorLog cannot already have an ID", ENTITY_NAME, "idexists");
        }
        VisitorLogDTO result = visitorLogService.save(visitorLogDTO);
        return ResponseEntity.created(new URI("/api/visitor-logs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /visitor-logs/:id} : Updates an existing visitorLog.
     *
     * @param id the id of the visitorLogDTO to save.
     * @param visitorLogDTO the visitorLogDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated visitorLogDTO,
     * or with status {@code 400 (Bad Request)} if the visitorLogDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the visitorLogDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/visitor-logs/{id}")
    public ResponseEntity<VisitorLogDTO> updateVisitorLog(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody VisitorLogDTO visitorLogDTO
    ) throws URISyntaxException {
        log.debug("REST request to update VisitorLog : {}, {}", id, visitorLogDTO);
        if (visitorLogDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, visitorLogDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!visitorLogRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        VisitorLogDTO result = visitorLogService.update(visitorLogDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, visitorLogDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /visitor-logs/:id} : Partial updates given fields of an existing visitorLog, field will ignore if it is null
     *
     * @param id the id of the visitorLogDTO to save.
     * @param visitorLogDTO the visitorLogDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated visitorLogDTO,
     * or with status {@code 400 (Bad Request)} if the visitorLogDTO is not valid,
     * or with status {@code 404 (Not Found)} if the visitorLogDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the visitorLogDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/visitor-logs/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<VisitorLogDTO> partialUpdateVisitorLog(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody VisitorLogDTO visitorLogDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update VisitorLog partially : {}, {}", id, visitorLogDTO);
        if (visitorLogDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, visitorLogDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!visitorLogRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<VisitorLogDTO> result = visitorLogService.partialUpdate(visitorLogDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, visitorLogDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /visitor-logs} : get all the visitorLogs.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of visitorLogs in body.
     */
    @GetMapping("/visitor-logs")
    public ResponseEntity<List<VisitorLogDTO>> getAllVisitorLogs(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of VisitorLogs");
        Page<VisitorLogDTO> page = visitorLogService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /visitor-logs/:id} : get the "id" visitorLog.
     *
     * @param id the id of the visitorLogDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the visitorLogDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/visitor-logs/{id}")
    public ResponseEntity<VisitorLogDTO> getVisitorLog(@PathVariable("id") Long id) {
        log.debug("REST request to get VisitorLog : {}", id);
        Optional<VisitorLogDTO> visitorLogDTO = visitorLogService.findOne(id);
        return ResponseUtil.wrapOrNotFound(visitorLogDTO);
    }

    /**
     * {@code DELETE  /visitor-logs/:id} : delete the "id" visitorLog.
     *
     * @param id the id of the visitorLogDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/visitor-logs/{id}")
    public ResponseEntity<Void> deleteVisitorLog(@PathVariable("id") Long id) {
        log.debug("REST request to delete VisitorLog : {}", id);
        visitorLogService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code GET  /admin/visitor-logs/stats} : get visitor statistics.
     * This is an example endpoint.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and a map of stats in the body.
     */
    @GetMapping("/visitor-logs/stats")
    public ResponseEntity<Map<String, Object>> getVisitorStats() {
        log.debug("REST request to get Visitor stats");

        long totalVisits = visitorLogRepository.count();
        long uniqueVisitors = visitorLogRepository.countDistinctByIpAddress();

        Map<String, Object> stats = Map.of("totalVisits", totalVisits, "uniqueVisitors", uniqueVisitors);

        return ResponseEntity.ok(stats);
    }
}
