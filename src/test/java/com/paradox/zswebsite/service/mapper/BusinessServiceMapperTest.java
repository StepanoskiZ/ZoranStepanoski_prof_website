package com.paradox.zswebsite.service.mapper;

import static com.paradox.zswebsite.domain.BusinessServiceAsserts.*;
import static com.paradox.zswebsite.domain.BusinessServiceTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BusinessServiceMapperTest {

    private BusinessServiceMapper businessServiceMapper;

    @BeforeEach
    void setUp() {
        businessServiceMapper = new BusinessServiceMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getBusinessServiceSample1();
        var actual = businessServiceMapper.toEntity(businessServiceMapper.toDto(expected));
        assertBusinessServiceAllPropertiesEquals(expected, actual);
    }
}
