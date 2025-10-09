package com.paradox.zswebsite.service;

import com.paradox.zswebsite.domain.BusinessService;
import com.paradox.zswebsite.domain.BusinessServiceMedia;
import com.paradox.zswebsite.repository.BusinessServiceRepository;
import com.paradox.zswebsite.service.dto.BusinessServiceCardDTO;
import com.paradox.zswebsite.service.dto.BusinessServiceDTO;
import com.paradox.zswebsite.service.dto.BusinessServiceDetailDTO;
import com.paradox.zswebsite.service.dto.BusinessServiceMediaDTO;
import com.paradox.zswebsite.service.mapper.BusinessServiceMapper;
import com.paradox.zswebsite.service.mapper.BusinessServiceMediaMapper;
import java.util.Comparator;
import java.util.Optional;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.text.StringEscapeUtils;
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
        businessServiceDTO.setDescriptionHTML(cleanContentHtml(businessServiceDTO.getDescriptionHTML()));
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
        businessServiceDTO.setDescriptionHTML(cleanContentHtml(businessServiceDTO.getDescriptionHTML()));
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
        businessServiceDTO.setDescriptionHTML(cleanContentHtml(businessServiceDTO.getDescriptionHTML()));

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
     * Cleans the HTML content by unescaping entities and replacing non-breaking spaces.
     * @param rawHtml the raw HTML string from the frontend.
     * @return a cleaned HTML string ready for database storage.
     */
    private String cleanContentHtml(String rawHtml) {
        if (rawHtml == null) {
            return null;
        }
        // First, unescape entities like &lt; to <
        String decodedHtml = StringEscapeUtils.unescapeHtml4(rawHtml);
        // Then, replace all non-breaking spaces with regular spaces to allow word wrapping.
        return decodedHtml.replace("&nbsp;", " ");
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
        log.debug("Request to get all BusinessService cards");
        // Use the repository method with the EntityGraph to prevent N+1 query problems
        List<BusinessService> services = businessServiceRepository.findAllWithFirstMedia();

        return services
            .stream()
            .map(this::mapToCardDTO) // Use a helper method for clarity
            .collect(Collectors.toList());
    }

    // Helper method for the mapping logic
    private BusinessServiceCardDTO mapToCardDTO(BusinessService service) {
        BusinessServiceCardDTO dto = new BusinessServiceCardDTO();
        dto.setId(service.getId());
        dto.setTitle(service.getTitle());
        dto.setDescription(service.getDescriptionHTML());

        // Find the first media item by sorting the collection by ID
        Optional<BusinessServiceMedia> firstMedia = service.getMedia().stream()
            .min(Comparator.comparing(BusinessServiceMedia::getId));

        // Set the DTO properties if a media item was found
        firstMedia.ifPresent(media -> {
            dto.setFirstMediaUrl(media.getMediaUrl());
            if (media.getBusinessServiceMediaType() != null) {
                dto.setFirstMediaType(media.getBusinessServiceMediaType().name());
            }
        });

        return dto;
    }

    @Transactional(readOnly = true)
    public Optional<BusinessServiceDetailDTO> findOneWithDetails(Long id) {
        log.debug("Request to get details for BusinessService : {}", id);
        // Use the EntityGraph method here as well to fetch media in one go
        return businessServiceRepository.findById(id)
            .map(service -> {
                BusinessServiceDetailDTO dto = new BusinessServiceDetailDTO();
                dto.setId(service.getId());
                dto.setTitle(service.getTitle());
                dto.setDescription(service.getDescriptionHTML());

                // Map the media files, ensuring they are sorted by ID
                List<BusinessServiceMediaDTO> mediaDTOs = service.getMedia().stream()
                    .sorted(Comparator.comparing(BusinessServiceMedia::getId)) // Sort media by ID
                    .map(businessServiceMediaMapper::toDto)
                    .collect(Collectors.toList());

                dto.setMediaFiles(mediaDTOs);
                return dto;
            });
    }
}
