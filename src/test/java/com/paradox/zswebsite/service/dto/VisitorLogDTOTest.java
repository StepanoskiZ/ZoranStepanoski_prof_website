package com.paradox.zswebsite.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.paradox.zswebsite.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VisitorLogDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(VisitorLogDTO.class);
        VisitorLogDTO visitorLogDTO1 = new VisitorLogDTO();
        visitorLogDTO1.setId(1L);
        VisitorLogDTO visitorLogDTO2 = new VisitorLogDTO();
        assertThat(visitorLogDTO1).isNotEqualTo(visitorLogDTO2);
        visitorLogDTO2.setId(visitorLogDTO1.getId());
        assertThat(visitorLogDTO1).isEqualTo(visitorLogDTO2);
        visitorLogDTO2.setId(2L);
        assertThat(visitorLogDTO1).isNotEqualTo(visitorLogDTO2);
        visitorLogDTO1.setId(null);
        assertThat(visitorLogDTO1).isNotEqualTo(visitorLogDTO2);
    }
}
