package com.paradox.zswebsite.service.mapper;

import com.paradox.zswebsite.domain.PageContent;
import com.paradox.zswebsite.service.dto.PageContentDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PageContent} and its DTO {@link PageContentDTO}.
 */
@Mapper(componentModel = "spring")
public interface PageContentMapper extends EntityMapper<PageContentDTO, PageContent> {}
