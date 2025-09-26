package com.paradox.zswebsite.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.paradox.zswebsite.IntegrationTest;
import com.paradox.zswebsite.domain.ContactMessage;
import com.paradox.zswebsite.repository.ContactMessageRepository;
import com.paradox.zswebsite.service.dto.ContactMessageDTO;
import com.paradox.zswebsite.service.mapper.ContactMessageMapper;
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
 * Integration tests for the {@link ContactMessageResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ContactMessageResourceIT {

    private static final String DEFAULT_VISITOR_NAME = "AAAAAAAAAA";
    private static final String UPDATED_VISITOR_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_VISITOR_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_VISITOR_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_MESSAGE = "AAAAAAAAAA";
    private static final String UPDATED_MESSAGE = "BBBBBBBBBB";

    private static final Instant DEFAULT_SUBMITTED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_SUBMITTED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/contact-messages";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ContactMessageRepository contactMessageRepository;

    @Autowired
    private ContactMessageMapper contactMessageMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restContactMessageMockMvc;

    private ContactMessage contactMessage;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ContactMessage createEntity(EntityManager em) {
        ContactMessage contactMessage = new ContactMessage()
            .visitorName(DEFAULT_VISITOR_NAME)
            .visitorEmail(DEFAULT_VISITOR_EMAIL)
            .message(DEFAULT_MESSAGE)
            .submittedDate(DEFAULT_SUBMITTED_DATE);
        return contactMessage;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ContactMessage createUpdatedEntity(EntityManager em) {
        ContactMessage contactMessage = new ContactMessage()
            .visitorName(UPDATED_VISITOR_NAME)
            .visitorEmail(UPDATED_VISITOR_EMAIL)
            .message(UPDATED_MESSAGE)
            .submittedDate(UPDATED_SUBMITTED_DATE);
        return contactMessage;
    }

    @BeforeEach
    public void initTest() {
        contactMessage = createEntity(em);
    }

    @Test
    @Transactional
    void createContactMessage() throws Exception {
        int databaseSizeBeforeCreate = contactMessageRepository.findAll().size();
        // Create the ContactMessage
        ContactMessageDTO contactMessageDTO = contactMessageMapper.toDto(contactMessage);
        restContactMessageMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contactMessageDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ContactMessage in the database
        List<ContactMessage> contactMessageList = contactMessageRepository.findAll();
        assertThat(contactMessageList).hasSize(databaseSizeBeforeCreate + 1);
        ContactMessage testContactMessage = contactMessageList.get(contactMessageList.size() - 1);
        assertThat(testContactMessage.getVisitorName()).isEqualTo(DEFAULT_VISITOR_NAME);
        assertThat(testContactMessage.getVisitorEmail()).isEqualTo(DEFAULT_VISITOR_EMAIL);
        assertThat(testContactMessage.getMessage()).isEqualTo(DEFAULT_MESSAGE);
        assertThat(testContactMessage.getSubmittedDate()).isEqualTo(DEFAULT_SUBMITTED_DATE);
    }

    @Test
    @Transactional
    void createContactMessageWithExistingId() throws Exception {
        // Create the ContactMessage with an existing ID
        contactMessage.setId(1L);
        ContactMessageDTO contactMessageDTO = contactMessageMapper.toDto(contactMessage);

        int databaseSizeBeforeCreate = contactMessageRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restContactMessageMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contactMessageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContactMessage in the database
        List<ContactMessage> contactMessageList = contactMessageRepository.findAll();
        assertThat(contactMessageList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkVisitorNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = contactMessageRepository.findAll().size();
        // set the field null
        contactMessage.setVisitorName(null);

        // Create the ContactMessage, which fails.
        ContactMessageDTO contactMessageDTO = contactMessageMapper.toDto(contactMessage);

        restContactMessageMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contactMessageDTO))
            )
            .andExpect(status().isBadRequest());

        List<ContactMessage> contactMessageList = contactMessageRepository.findAll();
        assertThat(contactMessageList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkVisitorEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = contactMessageRepository.findAll().size();
        // set the field null
        contactMessage.setVisitorEmail(null);

        // Create the ContactMessage, which fails.
        ContactMessageDTO contactMessageDTO = contactMessageMapper.toDto(contactMessage);

        restContactMessageMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contactMessageDTO))
            )
            .andExpect(status().isBadRequest());

        List<ContactMessage> contactMessageList = contactMessageRepository.findAll();
        assertThat(contactMessageList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSubmittedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = contactMessageRepository.findAll().size();
        // set the field null
        contactMessage.setSubmittedDate(null);

        // Create the ContactMessage, which fails.
        ContactMessageDTO contactMessageDTO = contactMessageMapper.toDto(contactMessage);

        restContactMessageMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contactMessageDTO))
            )
            .andExpect(status().isBadRequest());

        List<ContactMessage> contactMessageList = contactMessageRepository.findAll();
        assertThat(contactMessageList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllContactMessages() throws Exception {
        // Initialize the database
        contactMessageRepository.saveAndFlush(contactMessage);

        // Get all the contactMessageList
        restContactMessageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contactMessage.getId().intValue())))
            .andExpect(jsonPath("$.[*].visitorName").value(hasItem(DEFAULT_VISITOR_NAME)))
            .andExpect(jsonPath("$.[*].visitorEmail").value(hasItem(DEFAULT_VISITOR_EMAIL)))
            .andExpect(jsonPath("$.[*].message").value(hasItem(DEFAULT_MESSAGE.toString())))
            .andExpect(jsonPath("$.[*].submittedDate").value(hasItem(DEFAULT_SUBMITTED_DATE.toString())));
    }

    @Test
    @Transactional
    void getContactMessage() throws Exception {
        // Initialize the database
        contactMessageRepository.saveAndFlush(contactMessage);

        // Get the contactMessage
        restContactMessageMockMvc
            .perform(get(ENTITY_API_URL_ID, contactMessage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(contactMessage.getId().intValue()))
            .andExpect(jsonPath("$.visitorName").value(DEFAULT_VISITOR_NAME))
            .andExpect(jsonPath("$.visitorEmail").value(DEFAULT_VISITOR_EMAIL))
            .andExpect(jsonPath("$.message").value(DEFAULT_MESSAGE.toString()))
            .andExpect(jsonPath("$.submittedDate").value(DEFAULT_SUBMITTED_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingContactMessage() throws Exception {
        // Get the contactMessage
        restContactMessageMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingContactMessage() throws Exception {
        // Initialize the database
        contactMessageRepository.saveAndFlush(contactMessage);

        int databaseSizeBeforeUpdate = contactMessageRepository.findAll().size();

        // Update the contactMessage
        ContactMessage updatedContactMessage = contactMessageRepository.findById(contactMessage.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedContactMessage are not directly saved in db
        em.detach(updatedContactMessage);
        updatedContactMessage
            .visitorName(UPDATED_VISITOR_NAME)
            .visitorEmail(UPDATED_VISITOR_EMAIL)
            .message(UPDATED_MESSAGE)
            .submittedDate(UPDATED_SUBMITTED_DATE);
        ContactMessageDTO contactMessageDTO = contactMessageMapper.toDto(updatedContactMessage);

        restContactMessageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, contactMessageDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contactMessageDTO))
            )
            .andExpect(status().isOk());

        // Validate the ContactMessage in the database
        List<ContactMessage> contactMessageList = contactMessageRepository.findAll();
        assertThat(contactMessageList).hasSize(databaseSizeBeforeUpdate);
        ContactMessage testContactMessage = contactMessageList.get(contactMessageList.size() - 1);
        assertThat(testContactMessage.getVisitorName()).isEqualTo(UPDATED_VISITOR_NAME);
        assertThat(testContactMessage.getVisitorEmail()).isEqualTo(UPDATED_VISITOR_EMAIL);
        assertThat(testContactMessage.getMessage()).isEqualTo(UPDATED_MESSAGE);
        assertThat(testContactMessage.getSubmittedDate()).isEqualTo(UPDATED_SUBMITTED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingContactMessage() throws Exception {
        int databaseSizeBeforeUpdate = contactMessageRepository.findAll().size();
        contactMessage.setId(longCount.incrementAndGet());

        // Create the ContactMessage
        ContactMessageDTO contactMessageDTO = contactMessageMapper.toDto(contactMessage);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContactMessageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, contactMessageDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contactMessageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContactMessage in the database
        List<ContactMessage> contactMessageList = contactMessageRepository.findAll();
        assertThat(contactMessageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchContactMessage() throws Exception {
        int databaseSizeBeforeUpdate = contactMessageRepository.findAll().size();
        contactMessage.setId(longCount.incrementAndGet());

        // Create the ContactMessage
        ContactMessageDTO contactMessageDTO = contactMessageMapper.toDto(contactMessage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContactMessageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contactMessageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContactMessage in the database
        List<ContactMessage> contactMessageList = contactMessageRepository.findAll();
        assertThat(contactMessageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamContactMessage() throws Exception {
        int databaseSizeBeforeUpdate = contactMessageRepository.findAll().size();
        contactMessage.setId(longCount.incrementAndGet());

        // Create the ContactMessage
        ContactMessageDTO contactMessageDTO = contactMessageMapper.toDto(contactMessage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContactMessageMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contactMessageDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ContactMessage in the database
        List<ContactMessage> contactMessageList = contactMessageRepository.findAll();
        assertThat(contactMessageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateContactMessageWithPatch() throws Exception {
        // Initialize the database
        contactMessageRepository.saveAndFlush(contactMessage);

        int databaseSizeBeforeUpdate = contactMessageRepository.findAll().size();

        // Update the contactMessage using partial update
        ContactMessage partialUpdatedContactMessage = new ContactMessage();
        partialUpdatedContactMessage.setId(contactMessage.getId());

        partialUpdatedContactMessage.visitorEmail(UPDATED_VISITOR_EMAIL).message(UPDATED_MESSAGE);

        restContactMessageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContactMessage.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedContactMessage))
            )
            .andExpect(status().isOk());

        // Validate the ContactMessage in the database
        List<ContactMessage> contactMessageList = contactMessageRepository.findAll();
        assertThat(contactMessageList).hasSize(databaseSizeBeforeUpdate);
        ContactMessage testContactMessage = contactMessageList.get(contactMessageList.size() - 1);
        assertThat(testContactMessage.getVisitorName()).isEqualTo(DEFAULT_VISITOR_NAME);
        assertThat(testContactMessage.getVisitorEmail()).isEqualTo(UPDATED_VISITOR_EMAIL);
        assertThat(testContactMessage.getMessage()).isEqualTo(UPDATED_MESSAGE);
        assertThat(testContactMessage.getSubmittedDate()).isEqualTo(DEFAULT_SUBMITTED_DATE);
    }

    @Test
    @Transactional
    void fullUpdateContactMessageWithPatch() throws Exception {
        // Initialize the database
        contactMessageRepository.saveAndFlush(contactMessage);

        int databaseSizeBeforeUpdate = contactMessageRepository.findAll().size();

        // Update the contactMessage using partial update
        ContactMessage partialUpdatedContactMessage = new ContactMessage();
        partialUpdatedContactMessage.setId(contactMessage.getId());

        partialUpdatedContactMessage
            .visitorName(UPDATED_VISITOR_NAME)
            .visitorEmail(UPDATED_VISITOR_EMAIL)
            .message(UPDATED_MESSAGE)
            .submittedDate(UPDATED_SUBMITTED_DATE);

        restContactMessageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContactMessage.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedContactMessage))
            )
            .andExpect(status().isOk());

        // Validate the ContactMessage in the database
        List<ContactMessage> contactMessageList = contactMessageRepository.findAll();
        assertThat(contactMessageList).hasSize(databaseSizeBeforeUpdate);
        ContactMessage testContactMessage = contactMessageList.get(contactMessageList.size() - 1);
        assertThat(testContactMessage.getVisitorName()).isEqualTo(UPDATED_VISITOR_NAME);
        assertThat(testContactMessage.getVisitorEmail()).isEqualTo(UPDATED_VISITOR_EMAIL);
        assertThat(testContactMessage.getMessage()).isEqualTo(UPDATED_MESSAGE);
        assertThat(testContactMessage.getSubmittedDate()).isEqualTo(UPDATED_SUBMITTED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingContactMessage() throws Exception {
        int databaseSizeBeforeUpdate = contactMessageRepository.findAll().size();
        contactMessage.setId(longCount.incrementAndGet());

        // Create the ContactMessage
        ContactMessageDTO contactMessageDTO = contactMessageMapper.toDto(contactMessage);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContactMessageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, contactMessageDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(contactMessageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContactMessage in the database
        List<ContactMessage> contactMessageList = contactMessageRepository.findAll();
        assertThat(contactMessageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchContactMessage() throws Exception {
        int databaseSizeBeforeUpdate = contactMessageRepository.findAll().size();
        contactMessage.setId(longCount.incrementAndGet());

        // Create the ContactMessage
        ContactMessageDTO contactMessageDTO = contactMessageMapper.toDto(contactMessage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContactMessageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(contactMessageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContactMessage in the database
        List<ContactMessage> contactMessageList = contactMessageRepository.findAll();
        assertThat(contactMessageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamContactMessage() throws Exception {
        int databaseSizeBeforeUpdate = contactMessageRepository.findAll().size();
        contactMessage.setId(longCount.incrementAndGet());

        // Create the ContactMessage
        ContactMessageDTO contactMessageDTO = contactMessageMapper.toDto(contactMessage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContactMessageMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(contactMessageDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ContactMessage in the database
        List<ContactMessage> contactMessageList = contactMessageRepository.findAll();
        assertThat(contactMessageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteContactMessage() throws Exception {
        // Initialize the database
        contactMessageRepository.saveAndFlush(contactMessage);

        int databaseSizeBeforeDelete = contactMessageRepository.findAll().size();

        // Delete the contactMessage
        restContactMessageMockMvc
            .perform(delete(ENTITY_API_URL_ID, contactMessage.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ContactMessage> contactMessageList = contactMessageRepository.findAll();
        assertThat(contactMessageList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
