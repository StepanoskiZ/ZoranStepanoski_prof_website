package com.paradox.zswebsite.service.mapper;

import com.paradox.zswebsite.domain.BusinessService;
import com.paradox.zswebsite.service.dto.BusinessServiceDTO;
import java.util.List;

public class BusinessServiceMapperImpl implements BusinessServiceMapper {

    @Override
    public BusinessService toEntity(BusinessServiceDTO dto) {
        return null;
    }

    @Override
    public BusinessServiceDTO toDto(BusinessService entity) {
        return null;
    }

    @Override
    public List<BusinessService> toEntity(List<BusinessServiceDTO> dtoList) {
        return List.of();
    }

    @Override
    public List<BusinessServiceDTO> toDto(List<BusinessService> entityList) {
        return List.of();
    }

    @Override
    public void partialUpdate(BusinessService entity, BusinessServiceDTO dto) {}
}
