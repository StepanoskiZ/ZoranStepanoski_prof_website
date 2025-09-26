package com.paradox.zswebsite.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.paradox.zswebsite.IntegrationTest;
import com.paradox.zswebsite.domain.VisitorLog;
import com.paradox.zswebsite.repository.VisitorLogRepository;
import com.paradox.zswebsite.service.dto.VisitorLogDTO;
import com.paradox.zswebsite.service.mapper.VisitorLogMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link VisitorLogResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class VisitorLogResourceIT {

    private static final String DEFAULT_IP_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_IP_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_PAGE_VISITED = "AAAAAAAAAA";
    private static final String UPDATED_PAGE_VISITED = "BBBBBBBBBB";

    private static final String DEFAULT_USER_AGENT = "AAAAAAAAAA";
    private static final String UPDATED_USER_AGENT = "BBBBBBBBBB";

    private static final Instant DEFAULT_VISIT_TIMESTAMP = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_VISIT_TIMESTAMP = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/visitor-logs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private VisitorLogRepository visitorLogRepository;

    @Autowired
    private VisitorLogMapper visitorLogMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVisitorLogMockMvc;

    private VisitorLog visitorLog;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VisitorLog createEntity(EntityManager em) {
        VisitorLog visitorLog = new VisitorLog()
            .ipAddress(DEFAULT_IP_ADDRESS)
            .pageVisited(DEFAULT_PAGE_VISITED)
            .userAgent(DEFAULT_USER_AGENT)
            .visitTimestamp(DEFAULT_VISIT_TIMESTAMP);
        return visitorLog;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VisitorLog createUpdatedEntity(EntityManager em) {
        VisitorLog visitorLog = new VisitorLog()
            .ipAddress(UPDATED_IP_ADDRESS)
            .pageVisited(UPDATED_PAGE_VISITED)
            .userAgent(UPDATED_USER_AGENT)
            .visitTimestamp(UPDATED_VISIT_TIMESTAMP);
        return visitorLog;
    }

    @BeforeEach
    public void initTest() {
        visitorLog = createEntity(em);
    }

    @Test
    @Transactional
    void createVisitorLog() throws Exception {
        int databaseSizeBeforeCreate = visitorLogRepository.findAll().size();
        // Create the VisitorLog
        VisitorLogDTO visitorLogDTO = visitorLogMapper.toDto(visitorLog);
        restVisitorLogMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(visitorLogDTO)))
            .andExpect(status().isCreated());

        // Validate the VisitorLog in the database
        List<VisitorLog> visitorLogList = visitorLogRepository.findAll();
        assertThat(visitorLogList).hasSize(databaseSizeBeforeCreate + 1);
        VisitorLog testVisitorLog = visitorLogList.get(visitorLogList.size() - 1);
        assertThat(testVisitorLog.getIpAddress()).isEqualTo(DEFAULT_IP_ADDRESS);
        assertThat(testVisitorLog.getPageVisited()).isEqualTo(DEFAULT_PAGE_VISITED);
        assertThat(testVisitorLog.getUserAgent()).isEqualTo(DEFAULT_USER_AGENT);
        assertThat(testVisitorLog.getVisitTimestamp()).isEqualTo(DEFAULT_VISIT_TIMESTAMP);
    }

    @Test
    @Transactional
    void createVisitorLogWithExistingId() throws Exception {
        // Create the VisitorLog with an existing ID
        visitorLog.setId(1L);
        VisitorLogDTO visitorLogDTO = visitorLogMapper.toDto(visitorLog);

        int databaseSizeBeforeCreate = visitorLogRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVisitorLogMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(visitorLogDTO)))
            .andExpect(status().isBadRequest());

        // Validate the VisitorLog in the database
        List<VisitorLog> visitorLogList = visitorLogRepository.findAll();
        assertThat(visitorLogList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkVisitTimestampIsRequired() throws Exception {
        int databaseSizeBeforeTest = visitorLogRepository.findAll().size();
        // set the field null
        visitorLog.setVisitTimestamp(null);

        // Create the VisitorLog, which fails.
        VisitorLogDTO visitorLogDTO = visitorLogMapper.toDto(visitorLog);

        restVisitorLogMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(visitorLogDTO)))
            .andExpect(status().isBadRequest());

        List<VisitorLog> visitorLogList = visitorLogRepository.findAll();
        assertThat(visitorLogList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllVisitorLogs() throws Exception {
        // Initialize the database
        visitorLogRepository.saveAndFlush(visitorLog);

        // Get all the visitorLogList
        restVisitorLogMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(visitorLog.getId().intValue())))
            .andExpect(jsonPath("$.[*].ipAddress").value(hasItem(DEFAULT_IP_ADDRESS)))
            .andExpect(jsonPath("$.[*].pageVisited").value(hasItem(DEFAULT_PAGE_VISITED)))
            .andExpect(jsonPath("$.[*].userAgent").value(hasItem(DEFAULT_USER_AGENT)))
            .andExpect(jsonPath("$.[*].visitTimestamp").value(hasItem(DEFAULT_VISIT_TIMESTAMP.toString())));
    }

    @Test
    @Transactional
    void getVisitorLog() throws Exception {
        // Initialize the database
        visitorLogRepository.saveAndFlush(visitorLog);

        // Get the visitorLog
        restVisitorLogMockMvc
            .perform(get(ENTITY_API_URL_ID, visitorLog.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(visitorLog.getId().intValue()))
            .andExpect(jsonPath("$.ipAddress").value(DEFAULT_IP_ADDRESS))
            .andExpect(jsonPath("$.pageVisited").value(DEFAULT_PAGE_VISITED))
            .andExpect(jsonPath("$.userAgent").value(DEFAULT_USER_AGENT))
            .andExpect(jsonPath("$.visitTimestamp").value(DEFAULT_VISIT_TIMESTAMP.toString()));
    }

    @Test
    @Transactional
    void getNonExistingVisitorLog() throws Exception {
        // Get the visitorLog
        restVisitorLogMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingVisitorLog() throws Exception {
        // Initialize the database
        visitorLogRepository.saveAndFlush(visitorLog);

        int databaseSizeBeforeUpdate = visitorLogRepository.findAll().size();

        // Update the visitorLog
        VisitorLog updatedVisitorLog = visitorLogRepository.findById(visitorLog.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedVisitorLog are not directly saved in db
        em.detach(updatedVisitorLog);
        updatedVisitorLog
            .ipAddress(UPDATED_IP_ADDRESS)
            .pageVisited(UPDATED_PAGE_VISITED)
            .userAgent(UPDATED_USER_AGENT)
            .visitTimestamp(UPDATED_VISIT_TIMESTAMP);
        VisitorLogDTO visitorLogDTO = visitorLogMapper.toDto(updatedVisitorLog);

        restVisitorLogMockMvc
            .perform(
                put(ENTITY_API_URL_ID, visitorLogDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(visitorLogDTO))
            )
            .andExpect(status().isOk());

        // Validate the VisitorLog in the database
        List<VisitorLog> visitorLogList = visitorLogRepository.findAll();
        assertThat(visitorLogList).hasSize(databaseSizeBeforeUpdate);
        VisitorLog testVisitorLog = visitorLogList.get(visitorLogList.size() - 1);
        assertThat(testVisitorLog.getIpAddress()).isEqualTo(UPDATED_IP_ADDRESS);
        assertThat(testVisitorLog.getPageVisited()).isEqualTo(UPDATED_PAGE_VISITED);
        assertThat(testVisitorLog.getUserAgent()).isEqualTo(UPDATED_USER_AGENT);
        assertThat(testVisitorLog.getVisitTimestamp()).isEqualTo(UPDATED_VISIT_TIMESTAMP);
    }

    @Test
    @Transactional
    void putNonExistingVisitorLog() throws Exception {
        int databaseSizeBeforeUpdate = visitorLogRepository.findAll().size();
        visitorLog.setId(longCount.incrementAndGet());

        // Create the VisitorLog
        VisitorLogDTO visitorLogDTO = visitorLogMapper.toDto(visitorLog);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVisitorLogMockMvc
            .perform(
                put(ENTITY_API_URL_ID, visitorLogDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(visitorLogDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the VisitorLog in the database
        List<VisitorLog> visitorLogList = visitorLogRepository.findAll();
        assertThat(visitorLogList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchVisitorLog() throws Exception {
        int databaseSizeBeforeUpdate = visitorLogRepository.findAll().size();
        visitorLog.setId(longCount.incrementAndGet());

        // Create the VisitorLog
        VisitorLogDTO visitorLogDTO = visitorLogMapper.toDto(visitorLog);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVisitorLogMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(visitorLogDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the VisitorLog in the database
        List<VisitorLog> visitorLogList = visitorLogRepository.findAll();
        assertThat(visitorLogList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVisitorLog() throws Exception {
        int databaseSizeBeforeUpdate = visitorLogRepository.findAll().size();
        visitorLog.setId(longCount.incrementAndGet());

        // Create the VisitorLog
        VisitorLogDTO visitorLogDTO = visitorLogMapper.toDto(visitorLog);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVisitorLogMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(visitorLogDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the VisitorLog in the database
        List<VisitorLog> visitorLogList = visitorLogRepository.findAll();
        assertThat(visitorLogList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateVisitorLogWithPatch() throws Exception {
        // Initialize the database
        visitorLogRepository.saveAndFlush(visitorLog);

        int databaseSizeBeforeUpdate = visitorLogRepository.findAll().size();

        // Update the visitorLog using partial update
        VisitorLog partialUpdatedVisitorLog = new VisitorLog();
        partialUpdatedVisitorLog.setId(visitorLog.getId());

        partialUpdatedVisitorLog.ipAddress(UPDATED_IP_ADDRESS).userAgent(UPDATED_USER_AGENT);

        restVisitorLogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVisitorLog.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVisitorLog))
            )
            .andExpect(status().isOk());

        // Validate the VisitorLog in the database
        List<VisitorLog> visitorLogList = visitorLogRepository.findAll();
        assertThat(visitorLogList).hasSize(databaseSizeBeforeUpdate);
        VisitorLog testVisitorLog = visitorLogList.get(visitorLogList.size() - 1);
        assertThat(testVisitorLog.getIpAddress()).isEqualTo(UPDATED_IP_ADDRESS);
        assertThat(testVisitorLog.getPageVisited()).isEqualTo(DEFAULT_PAGE_VISITED);
        assertThat(testVisitorLog.getUserAgent()).isEqualTo(UPDATED_USER_AGENT);
        assertThat(testVisitorLog.getVisitTimestamp()).isEqualTo(DEFAULT_VISIT_TIMESTAMP);
    }

    @Test
    @Transactional
    void fullUpdateVisitorLogWithPatch() throws Exception {
        // Initialize the database
        visitorLogRepository.saveAndFlush(visitorLog);

        int databaseSizeBeforeUpdate = visitorLogRepository.findAll().size();

        // Update the visitorLog using partial update
        VisitorLog partialUpdatedVisitorLog = new VisitorLog();
        partialUpdatedVisitorLog.setId(visitorLog.getId());

        partialUpdatedVisitorLog
            .ipAddress(UPDATED_IP_ADDRESS)
            .pageVisited(UPDATED_PAGE_VISITED)
            .userAgent(UPDATED_USER_AGENT)
            .visitTimestamp(UPDATED_VISIT_TIMESTAMP);

        restVisitorLogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVisitorLog.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVisitorLog))
            )
            .andExpect(status().isOk());

        // Validate the VisitorLog in the database
        List<VisitorLog> visitorLogList = visitorLogRepository.findAll();
        assertThat(visitorLogList).hasSize(databaseSizeBeforeUpdate);
        VisitorLog testVisitorLog = visitorLogList.get(visitorLogList.size() - 1);
        assertThat(testVisitorLog.getIpAddress()).isEqualTo(UPDATED_IP_ADDRESS);
        assertThat(testVisitorLog.getPageVisited()).isEqualTo(UPDATED_PAGE_VISITED);
        assertThat(testVisitorLog.getUserAgent()).isEqualTo(UPDATED_USER_AGENT);
        assertThat(testVisitorLog.getVisitTimestamp()).isEqualTo(UPDATED_VISIT_TIMESTAMP);
    }

    @Test
    @Transactional
    void patchNonExistingVisitorLog() throws Exception {
        int databaseSizeBeforeUpdate = visitorLogRepository.findAll().size();
        visitorLog.setId(longCount.incrementAndGet());

        // Create the VisitorLog
        VisitorLogDTO visitorLogDTO = visitorLogMapper.toDto(visitorLog);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVisitorLogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, visitorLogDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(visitorLogDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the VisitorLog in the database
        List<VisitorLog> visitorLogList = visitorLogRepository.findAll();
        assertThat(visitorLogList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVisitorLog() throws Exception {
        int databaseSizeBeforeUpdate = visitorLogRepository.findAll().size();
        visitorLog.setId(longCount.incrementAndGet());

        // Create the VisitorLog
        VisitorLogDTO visitorLogDTO = visitorLogMapper.toDto(visitorLog);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVisitorLogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(visitorLogDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the VisitorLog in the database
        List<VisitorLog> visitorLogList = visitorLogRepository.findAll();
        assertThat(visitorLogList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVisitorLog() throws Exception {
        int databaseSizeBeforeUpdate = visitorLogRepository.findAll().size();
        visitorLog.setId(longCount.incrementAndGet());

        // Create the VisitorLog
        VisitorLogDTO visitorLogDTO = visitorLogMapper.toDto(visitorLog);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVisitorLogMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(visitorLogDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the VisitorLog in the database
        List<VisitorLog> visitorLogList = visitorLogRepository.findAll();
        assertThat(visitorLogList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteVisitorLog() throws Exception {
        // Initialize the database
        visitorLogRepository.saveAndFlush(visitorLog);

        int databaseSizeBeforeDelete = visitorLogRepository.findAll().size();

        // Delete the visitorLog
        restVisitorLogMockMvc
            .perform(delete(ENTITY_API_URL_ID, visitorLog.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<VisitorLog> visitorLogList = visitorLogRepository.findAll();
        assertThat(visitorLogList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
