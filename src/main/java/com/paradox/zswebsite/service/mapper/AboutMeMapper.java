package com.paradox.zswebsite.service.mapper;

import com.paradox.zswebsite.domain.AboutMe;
import com.paradox.zswebsite.service.dto.AboutMeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AboutMe} and its DTO {@link AboutMeDTO}.
 */
@Mapper(componentModel = "spring")
public interface AboutMeMapper extends EntityMapper<AboutMeDTO, AboutMe> {
    @Mapping(target = "name", constant = "About Me Section")
    AboutMeDTO toDto(AboutMe aboutMe);
}
