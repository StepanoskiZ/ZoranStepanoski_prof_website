package com.paradox.zswebsite.service.mapper;

import static com.paradox.zswebsite.domain.ProjectImageAsserts.*;
import static com.paradox.zswebsite.domain.ProjectImageTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProjectImageMapperTest {

    private ProjectImageMapper projectImageMapper;

    @BeforeEach
    void setUp() {
        projectImageMapper = new ProjectImageMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getProjectImageSample1();
        var actual = projectImageMapper.toEntity(projectImageMapper.toDto(expected));
        assertProjectImageAllPropertiesEquals(expected, actual);
    }
}
