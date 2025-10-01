package com.paradox.zswebsite.service.mapper;

import com.paradox.zswebsite.domain.BusinessService;
import com.paradox.zswebsite.domain.BusinessServiceMedia;
import com.paradox.zswebsite.service.dto.BusinessServiceDTO;
import com.paradox.zswebsite.service.dto.BusinessServiceMediaDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link BusinessServiceMedia} and its DTO {@link BusinessServiceMediaDTO}.
 */
@Mapper(componentModel = "spring")
public interface BusinessServiceMediaMapper extends EntityMapper<BusinessServiceMediaDTO, BusinessServiceMedia> {
    @Mapping(target = "businessService", source = "businessService", qualifiedByName = "businessServiceId")
    BusinessServiceMediaDTO toDto(BusinessServiceMedia s);

    @Named("businessServiceId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    BusinessServiceDTO toDtoBusinessServiceId(BusinessService businessService);
}
