package com.paradox.zswebsite.service;

import com.paradox.zswebsite.domain.Authority;
import com.paradox.zswebsite.repository.AuthorityRepository;
import com.paradox.zswebsite.service.dto.AuthorityDTO;
import com.paradox.zswebsite.service.mapper.AuthorityMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.paradox.zswebsite.domain.Authority}.
 */
@Service
@Transactional
public class AuthorityService {

    private final Logger log = LoggerFactory.getLogger(AuthorityService.class);

    private final AuthorityRepository authorityRepository;

    private final AuthorityMapper authorityMapper;

    public AuthorityService(AuthorityRepository authorityRepository, AuthorityMapper authorityMapper) {
        this.authorityRepository = authorityRepository;
        this.authorityMapper = authorityMapper;
    }

    /**
     * Save a authority.
     *
     * @param authorityDTO the entity to save.
     * @return the persisted entity.
     */
    public AuthorityDTO save(AuthorityDTO authorityDTO) {
        log.debug("Request to save Authority : {}", authorityDTO);
        Authority authority = authorityMapper.toEntity(authorityDTO);
        authority = authorityRepository.save(authority);
        return authorityMapper.toDto(authority);
    }

    /**
     * Update a authority.
     *
     * @param authorityDTO the entity to save.
     * @return the persisted entity.
     */
    public AuthorityDTO update(AuthorityDTO authorityDTO) {
        log.debug("Request to update Authority : {}", authorityDTO);
        Authority authority = authorityMapper.toEntity(authorityDTO);
        // no save call needed as we have no fields that can be updated
        return authorityMapper.toDto(authority);
    }

    /**
     * Partially update a authority.
     *
     * @param authorityDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AuthorityDTO> partialUpdate(AuthorityDTO authorityDTO) {
        log.debug("Request to partially update Authority : {}", authorityDTO);

        return authorityRepository
            .findById(authorityDTO.getName())
            .map(existingAuthority -> {
                authorityMapper.partialUpdate(existingAuthority, authorityDTO);

                return existingAuthority;
            })
            // .map(authorityRepository::save)
            .map(authorityMapper::toDto);
    }

    /**
     * Get all the authorities.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<AuthorityDTO> findAll() {
        log.debug("Request to get all Authorities");
        return authorityRepository.findAll().stream().map(authorityMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one authority by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AuthorityDTO> findOne(String id) {
        log.debug("Request to get Authority : {}", id);
        return authorityRepository.findById(id).map(authorityMapper::toDto);
    }

    /**
     * Delete the authority by id.
     *
     * @param id the id of the entity.
     */
    public void delete(String id) {
        log.debug("Request to delete Authority : {}", id);
        authorityRepository.deleteById(id);
    }
}
