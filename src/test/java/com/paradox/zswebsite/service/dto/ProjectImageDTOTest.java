package com.paradox.zswebsite.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.paradox.zswebsite.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProjectImageDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProjectImageDTO.class);
        ProjectImageDTO projectImageDTO1 = new ProjectImageDTO();
        projectImageDTO1.setId(1L);
        ProjectImageDTO projectImageDTO2 = new ProjectImageDTO();
        assertThat(projectImageDTO1).isNotEqualTo(projectImageDTO2);
        projectImageDTO2.setId(projectImageDTO1.getId());
        assertThat(projectImageDTO1).isEqualTo(projectImageDTO2);
        projectImageDTO2.setId(2L);
        assertThat(projectImageDTO1).isNotEqualTo(projectImageDTO2);
        projectImageDTO1.setId(null);
        assertThat(projectImageDTO1).isNotEqualTo(projectImageDTO2);
    }
}
