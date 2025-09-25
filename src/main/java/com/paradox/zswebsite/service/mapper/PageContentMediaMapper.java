package com.paradox.zswebsite.service.mapper;

import com.paradox.zswebsite.domain.PageContent;
import com.paradox.zswebsite.domain.PageContentMedia;
import com.paradox.zswebsite.service.dto.PageContentDTO;
import com.paradox.zswebsite.service.dto.PageContentMediaDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PageContentMedia} and its DTO {@link PageContentMediaDTO}.
 */
@Mapper(componentModel = "spring")
public interface PageContentMediaMapper extends EntityMapper<PageContentMediaDTO, PageContentMedia> {
    @Mapping(target = "pagecontent", source = "pagecontent", qualifiedByName = "pageContentSectionKey")
    PageContentMediaDTO toDto(PageContentMedia s);

    @Named("pageContentSectionKey")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "sectionKey", source = "sectionKey")
    PageContentDTO toDtoPageContentSectionKey(PageContent pageContent);
}
