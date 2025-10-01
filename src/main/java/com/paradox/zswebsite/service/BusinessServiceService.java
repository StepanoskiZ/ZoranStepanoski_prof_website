package com.paradox.zswebsite.service;

import com.paradox.zswebsite.domain.BusinessService;
import com.paradox.zswebsite.repository.BusinessServiceRepository;
import com.paradox.zswebsite.service.dto.BusinessServiceCardDTO;
import com.paradox.zswebsite.service.dto.BusinessServiceDTO;
import com.paradox.zswebsite.service.dto.BusinessServiceDetailDTO;
import com.paradox.zswebsite.service.mapper.BusinessServiceMapper;
import com.paradox.zswebsite.service.mapper.BusinessServiceMediaMapper;
import java.util.Optional;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.paradox.zswebsite.domain.BusinessService}.
 */
@Service
@Transactional
public class BusinessServiceService {

    private final Logger log = LoggerFactory.getLogger(BusinessServiceService.class);

    private final BusinessServiceRepository businessServiceRepository;
    private final BusinessServiceMapper businessServiceMapper;
    private final BusinessServiceMediaMapper businessServiceMediaMapper;

    public BusinessServiceService(
        BusinessServiceRepository businessServiceRepository,
        BusinessServiceMapper businessServiceMapper,
        BusinessServiceMediaMapper businessServiceMediaMapper
    ) {
        this.businessServiceRepository = businessServiceRepository;
        this.businessServiceMapper = businessServiceMapper;
        this.businessServiceMediaMapper = businessServiceMediaMapper;
    }

    /**
     * Save a businessService.
     *
     * @param businessServiceDTO the entity to save.
     * @return the persisted entity.
     */
    public BusinessServiceDTO save(BusinessServiceDTO businessServiceDTO) {
        log.debug("Request to save BusinessService : {}", businessServiceDTO);
        BusinessService businessService = businessServiceMapper.toEntity(businessServiceDTO);
        businessService = businessServiceRepository.save(businessService);
        return businessServiceMapper.toDto(businessService);
    }

    /**
     * Update a businessService.
     *
     * @param businessServiceDTO the entity to save.
     * @return the persisted entity.
     */
    public BusinessServiceDTO update(BusinessServiceDTO businessServiceDTO) {
        log.debug("Request to update BusinessService : {}", businessServiceDTO);
        BusinessService businessService = businessServiceMapper.toEntity(businessServiceDTO);
        businessService = businessServiceRepository.save(businessService);
        return businessServiceMapper.toDto(businessService);
    }

    /**
     * Partially update a businessService.
     *
     * @param businessServiceDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<BusinessServiceDTO> partialUpdate(BusinessServiceDTO businessServiceDTO) {
        log.debug("Request to partially update BusinessService : {}", businessServiceDTO);

        return businessServiceRepository
            .findById(businessServiceDTO.getId())
            .map(existingBusinessService -> {
                businessServiceMapper.partialUpdate(existingBusinessService, businessServiceDTO);

                return existingBusinessService;
            })
            .map(businessServiceRepository::save)
            .map(businessServiceMapper::toDto);
    }

    /**
     * Get all the businessServices.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<BusinessServiceDTO> findAll(Pageable pageable) {
        log.debug("Request to get all BusinessServices");
        return businessServiceRepository.findAll(pageable).map(businessServiceMapper::toDto);
    }

    /**
     * Get one businessService by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<BusinessServiceDTO> findOne(Long id) {
        log.debug("Request to get BusinessService : {}", id);
        return businessServiceRepository.findById(id).map(businessServiceMapper::toDto);
    }

    /**
     * Delete the businessService by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete BusinessService : {}", id);
        businessServiceRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<BusinessServiceCardDTO> findAllCards() {
        return businessServiceRepository.findAllWithFirstMedia().stream() // You'll need to create this repository method
            .map(service -> {
                String firstMediaUrl = service.getMedia().stream().findFirst().map(media -> media.getMediaUrl()).orElse(null);
                String firstMediaType = service.getMedia().stream().findFirst().map(media -> media.getBusinessServiceMediaType().toString()).orElse(null);
                return new BusinessServiceCardDTO(
                    service.getId(),
                    service.getTitle(),
                    service.getDescriptionHTML(),
                    firstMediaUrl,
                    firstMediaType
                );
            })
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public BusinessServiceDetailDTO findOneWithDetails(Long id) {
        return businessServiceRepository.findById(id)
            .map(service -> {
                BusinessServiceDetailDTO dto = new BusinessServiceDetailDTO();
                dto.setId(service.getId());
                dto.setTitle(service.getTitle());
                dto.setDescription(service.getDescriptionHTML());
                dto.setMediaFiles(
                    service.getMedia().stream()
                        .map(businessServiceMediaMapper::toDto)
                        .collect(Collectors.toList())
                );
                return dto;
            })
            .orElse(null);
    }
}
