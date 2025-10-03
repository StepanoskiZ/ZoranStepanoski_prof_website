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
    @Mapping(target = "businessService", source = "businessService", qualifiedByName = "businessServiceTitle")
    BusinessServiceMediaDTO toDto(BusinessServiceMedia s);

    @Named("businessServiceTitle")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "title", source = "title")
    BusinessServiceDTO toDtoBusinessServiceTitle(BusinessService businessService);
}
