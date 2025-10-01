package com.paradox.zswebsite.service;

import com.paradox.zswebsite.domain.BusinessServiceMedia;
import com.paradox.zswebsite.repository.BusinessServiceMediaRepository;
import com.paradox.zswebsite.service.dto.BusinessServiceMediaDTO;
import com.paradox.zswebsite.service.mapper.BusinessServiceMediaMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.paradox.zswebsite.domain.BusinessServiceMedia}.
 */
@Service
@Transactional
public class BusinessServiceMediaService {

    private final Logger log = LoggerFactory.getLogger(BusinessServiceMediaService.class);

    private final BusinessServiceMediaRepository businessServiceMediaRepository;

    private final BusinessServiceMediaMapper businessServiceMediaMapper;

    public BusinessServiceMediaService(
        BusinessServiceMediaRepository businessServiceMediaRepository,
        BusinessServiceMediaMapper businessServiceMediaMapper
    ) {
        this.businessServiceMediaRepository = businessServiceMediaRepository;
        this.businessServiceMediaMapper = businessServiceMediaMapper;
    }

    /**
     * Save a businessServiceMedia.
     *
     * @param businessServiceMediaDTO the entity to save.
     * @return the persisted entity.
     */
    public BusinessServiceMediaDTO save(BusinessServiceMediaDTO businessServiceMediaDTO) {
        log.debug("Request to save BusinessServiceMedia : {}", businessServiceMediaDTO);
        BusinessServiceMedia businessServiceMedia = businessServiceMediaMapper.toEntity(businessServiceMediaDTO);
        businessServiceMedia = businessServiceMediaRepository.save(businessServiceMedia);
        return businessServiceMediaMapper.toDto(businessServiceMedia);
    }

    /**
     * Update a businessServiceMedia.
     *
     * @param businessServiceMediaDTO the entity to save.
     * @return the persisted entity.
     */
    public BusinessServiceMediaDTO update(BusinessServiceMediaDTO businessServiceMediaDTO) {
        log.debug("Request to update BusinessServiceMedia : {}", businessServiceMediaDTO);
        BusinessServiceMedia businessServiceMedia = businessServiceMediaMapper.toEntity(businessServiceMediaDTO);
        businessServiceMedia = businessServiceMediaRepository.save(businessServiceMedia);
        return businessServiceMediaMapper.toDto(businessServiceMedia);
    }

    /**
     * Partially update a businessServiceMedia.
     *
     * @param businessServiceMediaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<BusinessServiceMediaDTO> partialUpdate(BusinessServiceMediaDTO businessServiceMediaDTO) {
        log.debug("Request to partially update BusinessServiceMedia : {}", businessServiceMediaDTO);

        return businessServiceMediaRepository
            .findById(businessServiceMediaDTO.getId())
            .map(existingBusinessServiceMedia -> {
                businessServiceMediaMapper.partialUpdate(existingBusinessServiceMedia, businessServiceMediaDTO);

                return existingBusinessServiceMedia;
            })
            .map(businessServiceMediaRepository::save)
            .map(businessServiceMediaMapper::toDto);
    }

    /**
     * Get all the businessServiceMedias.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<BusinessServiceMediaDTO> findAll(Pageable pageable) {
        log.debug("Request to get all BusinessServiceMedias");
        return businessServiceMediaRepository.findAll(pageable).map(businessServiceMediaMapper::toDto);
    }

    /**
     * Get one businessServiceMedia by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<BusinessServiceMediaDTO> findOne(Long id) {
        log.debug("Request to get BusinessServiceMedia : {}", id);
        return businessServiceMediaRepository.findById(id).map(businessServiceMediaMapper::toDto);
    }

    /**
     * Delete the businessServiceMedia by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete BusinessServiceMedia : {}", id);
        businessServiceMediaRepository.deleteById(id);
    }
}
