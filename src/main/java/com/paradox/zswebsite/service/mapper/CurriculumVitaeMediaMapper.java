package com.paradox.zswebsite.service.mapper;

import com.paradox.zswebsite.domain.CurriculumVitae;
import com.paradox.zswebsite.domain.CurriculumVitaeMedia;
import com.paradox.zswebsite.service.dto.CurriculumVitaeDTO;
import com.paradox.zswebsite.service.dto.CurriculumVitaeMediaDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CurriculumVitaeMedia} and its DTO {@link CurriculumVitaeMediaDTO}.
 */
@Mapper(componentModel = "spring")
public interface CurriculumVitaeMediaMapper extends EntityMapper<CurriculumVitaeMediaDTO, CurriculumVitaeMedia> {
    @Mapping(target = "curriculumVitae", source = "curriculumVitae", qualifiedByName = "curriculumVitaeId")
    CurriculumVitaeMediaDTO toDto(CurriculumVitaeMedia s);

    @Named("curriculumVitaeId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CurriculumVitaeDTO toDtoCurriculumVitaeId(CurriculumVitae curriculumVitae);
}
