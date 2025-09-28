package com.paradox.zswebsite.service.mapper;

import com.paradox.zswebsite.domain.CurriculumVitae;
import com.paradox.zswebsite.service.dto.CurriculumVitaeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CurriculumVitae} and its DTO {@link CurriculumVitaeDTO}.
 */
@Mapper(componentModel = "spring")
public interface CurriculumVitaeMapper extends EntityMapper<CurriculumVitaeDTO, CurriculumVitae> {}
