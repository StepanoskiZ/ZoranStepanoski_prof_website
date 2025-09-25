package com.paradox.zswebsite.service.mapper;

import com.paradox.zswebsite.domain.Project;
import com.paradox.zswebsite.domain.ProjectMedia;
import com.paradox.zswebsite.service.dto.ProjectDTO;
import com.paradox.zswebsite.service.dto.ProjectMediaDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ProjectMedia} and its DTO {@link ProjectMediaDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProjectMediaMapper extends EntityMapper<ProjectMediaDTO, ProjectMedia> {
    @Mapping(target = "project", source = "project", qualifiedByName = "projectTitle")
    ProjectMediaDTO toDto(ProjectMedia s);

    @Named("projectTitle")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "title", source = "title")
    ProjectDTO toDtoProjectTitle(Project project);
}
