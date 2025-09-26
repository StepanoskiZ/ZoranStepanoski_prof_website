package com.paradox.zswebsite.service;

import com.paradox.zswebsite.domain.VisitorLog;
import com.paradox.zswebsite.repository.VisitorLogRepository;
import com.paradox.zswebsite.service.dto.VisitorLogDTO;
import com.paradox.zswebsite.service.mapper.VisitorLogMapper;
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

    public VisitorLogService(VisitorLogRepository visitorLogRepository, VisitorLogMapper visitorLogMapper) {
        this.visitorLogRepository = visitorLogRepository;
        this.visitorLogMapper = visitorLogMapper;
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
}
