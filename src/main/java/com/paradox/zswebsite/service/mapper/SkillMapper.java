package com.paradox.zswebsite.service.mapper;

import com.paradox.zswebsite.domain.Skill;
import com.paradox.zswebsite.service.dto.SkillDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Skill} and its DTO {@link SkillDTO}.
 */
@Mapper(componentModel = "spring")
public interface SkillMapper extends EntityMapper<SkillDTO, Skill> {}
