package com.paradox.zswebsite.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.paradox.zswebsite.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProjectMediaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProjectMediaDTO.class);
        ProjectMediaDTO projectMediaDTO1 = new ProjectMediaDTO();
        projectMediaDTO1.setId(1L);
        ProjectMediaDTO projectMediaDTO2 = new ProjectMediaDTO();
        assertThat(projectMediaDTO1).isNotEqualTo(projectMediaDTO2);
        projectMediaDTO2.setId(projectMediaDTO1.getId());
        assertThat(projectMediaDTO1).isEqualTo(projectMediaDTO2);
        projectMediaDTO2.setId(2L);
        assertThat(projectMediaDTO1).isNotEqualTo(projectMediaDTO2);
        projectMediaDTO1.setId(null);
        assertThat(projectMediaDTO1).isNotEqualTo(projectMediaDTO2);
    }
}
