package com.paradox.zswebsite.service.mapper;

import com.paradox.zswebsite.domain.BlogPost;
import com.paradox.zswebsite.service.dto.BlogPostDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link BlogPost} and its DTO {@link BlogPostDTO}.
 */
@Mapper(componentModel = "spring")
public interface BlogPostMapper extends EntityMapper<BlogPostDTO, BlogPost> {}
