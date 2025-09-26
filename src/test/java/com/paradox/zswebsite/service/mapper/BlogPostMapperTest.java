package com.paradox.zswebsite.service.mapper;

import org.junit.jupiter.api.BeforeEach;

class BlogPostMapperTest {

    private BlogPostMapper blogPostMapper;

    @BeforeEach
    public void setUp() {
        blogPostMapper = new BlogPostMapperImpl();
    }
}
