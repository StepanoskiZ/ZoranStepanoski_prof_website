package com.paradox.zswebsite.service.mapper;

import com.paradox.zswebsite.domain.AboutMe;
import com.paradox.zswebsite.domain.AboutMeMedia;
import com.paradox.zswebsite.service.dto.AboutMeDTO;
import com.paradox.zswebsite.service.dto.AboutMeMediaDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AboutMeMedia} and its DTO {@link AboutMeMediaDTO}.
 */
//@Mapper(componentModel = "spring")
//public interface AboutMeMediaMapper extends EntityMapper<AboutMeMediaDTO, AboutMeMedia> {
//    AboutMeMediaDTO toDto(AboutMeMedia s);
//
//    @Named("aboutMeId")
//    @BeanMapping(ignoreByDefault = true)
//    @Mapping(target = "id", source = "id")
//    AboutMeDTO toDtoAboutMeId(AboutMe aboutMe);
//}
@Mapper(componentModel = "spring")
public interface AboutMeMediaMapper extends EntityMapper<AboutMeMediaDTO, AboutMeMedia> {

    // --- THIS IS THE CRITICAL PART FOR CONVERTING ENTITY -> DTO ---
    // It tells the mapper to use a special, lean DTO for the 'aboutMe' relationship
    @Mapping(target = "aboutMe", source = "aboutMe", qualifiedByName = "aboutMeId")
    AboutMeMediaDTO toDto(AboutMeMedia s);

    // --- THIS IS THE CRITICAL PART FOR CONVERTING DTO -> ENTITY ---
    // It tells the mapper to get the full entity from the DTO's 'aboutMe' object
    @Mapping(target = "aboutMe", source = "aboutMe")
    AboutMeMedia toEntity(AboutMeMediaDTO aboutMeMediaDTO);


    // --- THIS IS THE HELPER METHOD DEFINITION ---
    // It defines the 'aboutMeId' named mapping that creates the lean DTO
    @Named("aboutMeId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    // If you want to show a title/name in a dropdown, you would add it here.
    // For now, ID is enough.
    AboutMeDTO toDtoAboutMeId(AboutMe aboutMe);
}
