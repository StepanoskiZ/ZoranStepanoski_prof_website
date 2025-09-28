package com.paradox.zswebsite.service;

import com.paradox.zswebsite.domain.ContactMessage;
import com.paradox.zswebsite.repository.ContactMessageRepository;
import com.paradox.zswebsite.service.dto.ContactMessageDTO;
import com.paradox.zswebsite.service.mapper.ContactMessageMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.paradox.zswebsite.domain.ContactMessage}.
 */
@Service
@Transactional
public class ContactMessageService {

    private final Logger log = LoggerFactory.getLogger(ContactMessageService.class);

    private final ContactMessageRepository contactMessageRepository;

    private final ContactMessageMapper contactMessageMapper;

    public ContactMessageService(ContactMessageRepository contactMessageRepository, ContactMessageMapper contactMessageMapper) {
        this.contactMessageRepository = contactMessageRepository;
        this.contactMessageMapper = contactMessageMapper;
    }

    /**
     * Save a contactMessage.
     *
     * @param contactMessageDTO the entity to save.
     * @return the persisted entity.
     */
    public ContactMessageDTO save(ContactMessageDTO contactMessageDTO) {
        log.debug("Request to save ContactMessage : {}", contactMessageDTO);
        ContactMessage contactMessage = contactMessageMapper.toEntity(contactMessageDTO);
        contactMessage = contactMessageRepository.save(contactMessage);
        return contactMessageMapper.toDto(contactMessage);
    }

    /**
     * Update a contactMessage.
     *
     * @param contactMessageDTO the entity to save.
     * @return the persisted entity.
     */
    public ContactMessageDTO update(ContactMessageDTO contactMessageDTO) {
        log.debug("Request to update ContactMessage : {}", contactMessageDTO);
        ContactMessage contactMessage = contactMessageMapper.toEntity(contactMessageDTO);
        contactMessage = contactMessageRepository.save(contactMessage);
        return contactMessageMapper.toDto(contactMessage);
    }

    /**
     * Partially update a contactMessage.
     *
     * @param contactMessageDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ContactMessageDTO> partialUpdate(ContactMessageDTO contactMessageDTO) {
        log.debug("Request to partially update ContactMessage : {}", contactMessageDTO);

        return contactMessageRepository
            .findById(contactMessageDTO.getId())
            .map(existingContactMessage -> {
                contactMessageMapper.partialUpdate(existingContactMessage, contactMessageDTO);

                return existingContactMessage;
            })
            .map(contactMessageRepository::save)
            .map(contactMessageMapper::toDto);
    }

    /**
     * Get all the contactMessages.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ContactMessageDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ContactMessages");
        return contactMessageRepository.findAll(pageable).map(contactMessageMapper::toDto);
    }

    /**
     * Get one contactMessage by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ContactMessageDTO> findOne(Long id) {
        log.debug("Request to get ContactMessage : {}", id);
        return contactMessageRepository.findById(id).map(contactMessageMapper::toDto);
    }

    /**
     * Delete the contactMessage by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ContactMessage : {}", id);
        contactMessageRepository.deleteById(id);
    }
}
