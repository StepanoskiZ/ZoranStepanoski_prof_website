package com.paradox.zswebsite.service.mapper;

import com.paradox.zswebsite.domain.VisitorLog;
import com.paradox.zswebsite.service.dto.VisitorLogDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link VisitorLog} and its DTO {@link VisitorLogDTO}.
 */
@Mapper(componentModel = "spring")
public interface VisitorLogMapper extends EntityMapper<VisitorLogDTO, VisitorLog> {}
