package com.paradox.zswebsite.service.mapper;

import com.paradox.zswebsite.domain.Authority;
import com.paradox.zswebsite.service.dto.AuthorityDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Authority} and its DTO {@link AuthorityDTO}.
 */
@Mapper(componentModel = "spring")
public interface AuthorityMapper extends EntityMapper<AuthorityDTO, Authority> {}
