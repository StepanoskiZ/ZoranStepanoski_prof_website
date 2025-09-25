package com.paradox.zswebsite.domain;

import static com.paradox.zswebsite.domain.BusinessServiceTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.paradox.zswebsite.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BusinessServiceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BusinessService.class);
        BusinessService businessService1 = getBusinessServiceSample1();
        BusinessService businessService2 = new BusinessService();
        assertThat(businessService1).isNotEqualTo(businessService2);

        businessService2.setId(businessService1.getId());
        assertThat(businessService1).isEqualTo(businessService2);

        businessService2 = getBusinessServiceSample2();
        assertThat(businessService1).isNotEqualTo(businessService2);
    }
}
