package com.paradox.zswebsite.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.paradox.zswebsite.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BusinessServiceDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BusinessServiceDTO.class);
        BusinessServiceDTO businessServiceDTO1 = new BusinessServiceDTO();
        businessServiceDTO1.setId(1L);
        BusinessServiceDTO businessServiceDTO2 = new BusinessServiceDTO();
        assertThat(businessServiceDTO1).isNotEqualTo(businessServiceDTO2);
        businessServiceDTO2.setId(businessServiceDTO1.getId());
        assertThat(businessServiceDTO1).isEqualTo(businessServiceDTO2);
        businessServiceDTO2.setId(2L);
        assertThat(businessServiceDTO1).isNotEqualTo(businessServiceDTO2);
        businessServiceDTO1.setId(null);
        assertThat(businessServiceDTO1).isNotEqualTo(businessServiceDTO2);
    }
}
