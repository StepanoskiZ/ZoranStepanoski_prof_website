package com.paradox.zswebsite.service.mapper;

import com.paradox.zswebsite.domain.Project;
import com.paradox.zswebsite.service.dto.ProjectDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Project} and its DTO {@link ProjectDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProjectMapper extends EntityMapper<ProjectDTO, Project> {}
