package com.paradox.zswebsite.service.mapper;

import com.paradox.zswebsite.domain.AboutMe;
import com.paradox.zswebsite.domain.AboutMeMedia;
import com.paradox.zswebsite.service.dto.AboutMeDTO;
import com.paradox.zswebsite.service.dto.AboutMeMediaDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AboutMeMedia} and its DTO {@link AboutMeMediaDTO}.
 */
@Mapper(componentModel = "spring")
public interface AboutMeMediaMapper extends EntityMapper<AboutMeMediaDTO, AboutMeMedia> {
    @Mapping(target = "aboutMe", source = "aboutMe", qualifiedByName = "aboutMeId")
    AboutMeMediaDTO toDto(AboutMeMedia s);

    @Named("aboutMeId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AboutMeDTO toDtoAboutMeId(AboutMe aboutMe);
}
