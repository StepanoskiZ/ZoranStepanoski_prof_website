package com.paradox.zswebsite.service;

import com.paradox.zswebsite.domain.VisitorLog;
import com.paradox.zswebsite.repository.VisitorLogRepository;
import com.paradox.zswebsite.service.IpGeolocationService;
import com.paradox.zswebsite.service.dto.VisitorLogDTO;
import com.paradox.zswebsite.service.dto.VisitorLogWithGeoDTO;
import com.paradox.zswebsite.service.mapper.VisitorLogMapper;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.paradox.zswebsite.domain.VisitorLog}.
 */
@Service
@Transactional
public class VisitorLogService {

    private final Logger log = LoggerFactory.getLogger(VisitorLogService.class);
    private final VisitorLogRepository visitorLogRepository;
    private final VisitorLogMapper visitorLogMapper;
    private final IpGeolocationService ipGeolocationService;

    public VisitorLogService(
        VisitorLogRepository visitorLogRepository,
        VisitorLogMapper visitorLogMapper,
        IpGeolocationService ipGeolocationService
    ) {
        this.visitorLogRepository = visitorLogRepository;
        this.visitorLogMapper = visitorLogMapper;
        this.ipGeolocationService = ipGeolocationService;
    }

    /**
     * Save a visitorLog.
     *
     * @param visitorLogDTO the entity to save.
     * @return the persisted entity.
     */
    public VisitorLogDTO save(VisitorLogDTO visitorLogDTO) {
        log.debug("Request to save VisitorLog : {}", visitorLogDTO);
        VisitorLog visitorLog = visitorLogMapper.toEntity(visitorLogDTO);
        visitorLog = visitorLogRepository.save(visitorLog);
        return visitorLogMapper.toDto(visitorLog);
    }

    /**
     * Update a visitorLog.
     *
     * @param visitorLogDTO the entity to save.
     * @return the persisted entity.
     */
    public VisitorLogDTO update(VisitorLogDTO visitorLogDTO) {
        log.debug("Request to update VisitorLog : {}", visitorLogDTO);
        VisitorLog visitorLog = visitorLogMapper.toEntity(visitorLogDTO);
        visitorLog = visitorLogRepository.save(visitorLog);
        return visitorLogMapper.toDto(visitorLog);
    }

    /**
     * Partially update a visitorLog.
     *
     * @param visitorLogDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<VisitorLogDTO> partialUpdate(VisitorLogDTO visitorLogDTO) {
        log.debug("Request to partially update VisitorLog : {}", visitorLogDTO);

        return visitorLogRepository
            .findById(visitorLogDTO.getId())
            .map(existingVisitorLog -> {
                visitorLogMapper.partialUpdate(existingVisitorLog, visitorLogDTO);

                return existingVisitorLog;
            })
            .map(visitorLogRepository::save)
            .map(visitorLogMapper::toDto);
    }

    /**
     * Get all the visitorLogs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<VisitorLogDTO> findAll(Pageable pageable) {
        log.debug("Request to get all VisitorLogs");
        return visitorLogRepository.findAll(pageable).map(visitorLogMapper::toDto);
    }

    /**
     * Get one visitorLog by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<VisitorLogDTO> findOne(Long id) {
        log.debug("Request to get VisitorLog : {}", id);
        return visitorLogRepository.findById(id).map(visitorLogMapper::toDto);
    }

    /**
     * Delete the visitorLog by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete VisitorLog : {}", id);
        visitorLogRepository.deleteById(id);
    }

    /**
     * Get all the visitorLogs with their geolocation info.
     *
     * @param pageable the pagination information.
     * @return the list of entities with geolocation.
     */
    @Transactional(readOnly = true)
    public Page<VisitorLogWithGeoDTO> findAllWithGeo(Pageable pageable) {
        log.debug("Request to get all VisitorLogs with Geolocation");
        Page<VisitorLog> page = visitorLogRepository.findAll(pageable);

        // Map the entity Page to the new DTO Page
        return page.map(this::enrichWithGeoData);
    }

    /**
     * Helper method to convert a VisitorLog to VisitorLogWithGeoDTO and add geo data.
     */
    private VisitorLogWithGeoDTO enrichWithGeoData(VisitorLog visitorLog) {
        // Convert to the base DTO first
        VisitorLogDTO baseDto = visitorLogMapper.toDto(visitorLog);

        // Create the new DTO and copy properties
        VisitorLogWithGeoDTO geoDto = new VisitorLogWithGeoDTO();
        geoDto.setId(baseDto.getId());
        geoDto.setIpAddress(baseDto.getIpAddress());
        geoDto.setPageVisited(baseDto.getPageVisited());
        geoDto.setUserAgent(baseDto.getUserAgent());
        geoDto.setVisitTimestamp(baseDto.getVisitTimestamp());

        // Perform the lookup and enrich the DTO
        ipGeolocationService
            .getLocation(visitorLog.getIpAddress())
            .ifPresent(response -> {
                geoDto.setCity(response.getCity().getName());
                geoDto.setCountry(response.getCountry().getName());
                if (response.getLocation() != null) {
                    geoDto.setLatitude(response.getLocation().getLatitude());
                    geoDto.setLongitude(response.getLocation().getLongitude());
                }
            });

        return geoDto;
    }

    /**
     * Get one visitorLog by id with its geolocation info.
     *
     * @param id the id of the entity.
     * @return the entity with geolocation.
     */
    @Transactional(readOnly = true)
    public Optional<VisitorLogWithGeoDTO> findOneWithGeo(Long id) {
        log.debug("Request to get VisitorLog with Geo : {}", id);
        return visitorLogRepository.findById(id).map(this::enrichWithGeoData);
    }

    @Transactional(readOnly = true)
    public List<VisitorLogWithGeoDTO> findAllGeoPoints() {
        log.debug("Request to get all VisitorLog geo points");
        return visitorLogRepository
            .findAll() // Get all logs
            .stream()
            .map(this::enrichWithGeoData) // Add geo data
            .filter(dto -> dto.getLatitude() != null && dto.getLongitude() != null) // Filter out entries with no coordinates
            .toList();
    }
}
