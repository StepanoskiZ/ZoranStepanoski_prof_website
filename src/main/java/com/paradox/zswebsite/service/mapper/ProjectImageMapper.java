package com.paradox.zswebsite.service.mapper;

import com.paradox.zswebsite.domain.Project;
import com.paradox.zswebsite.domain.ProjectImage;
import com.paradox.zswebsite.service.dto.ProjectDTO;
import com.paradox.zswebsite.service.dto.ProjectImageDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ProjectImage} and its DTO {@link ProjectImageDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProjectImageMapper extends EntityMapper<ProjectImageDTO, ProjectImage> {
    @Mapping(target = "project", source = "project", qualifiedByName = "projectId")
    ProjectImageDTO toDto(ProjectImage s);

    @Named("projectId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ProjectDTO toDtoProjectId(Project project);
}
