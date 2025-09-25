package com.paradox.zswebsite.service.mapper;

import com.paradox.zswebsite.domain.BusinessService;
import com.paradox.zswebsite.service.dto.BusinessServiceDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link BusinessService} and its DTO {@link BusinessServiceDTO}.
 */
@Mapper(componentModel = "spring")
public interface BusinessServiceMapper extends EntityMapper<BusinessServiceDTO, BusinessService> {}
