package com.paradox.zswebsite.domain;

import static com.paradox.zswebsite.domain.VisitorLogTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.paradox.zswebsite.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VisitorLogTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(VisitorLog.class);
        VisitorLog visitorLog1 = getVisitorLogSample1();
        VisitorLog visitorLog2 = new VisitorLog();
        assertThat(visitorLog1).isNotEqualTo(visitorLog2);

        visitorLog2.setId(visitorLog1.getId());
        assertThat(visitorLog1).isEqualTo(visitorLog2);

        visitorLog2 = getVisitorLogSample2();
        assertThat(visitorLog1).isNotEqualTo(visitorLog2);
    }
}
