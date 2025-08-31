package com.paradox.zswebsite.domain;

import static com.paradox.zswebsite.domain.ProjectImageTestSamples.*;
import static com.paradox.zswebsite.domain.ProjectTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.paradox.zswebsite.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProjectImageTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProjectImage.class);
        ProjectImage projectImage1 = getProjectImageSample1();
        ProjectImage projectImage2 = new ProjectImage();
        assertThat(projectImage1).isNotEqualTo(projectImage2);

        projectImage2.setId(projectImage1.getId());
        assertThat(projectImage1).isEqualTo(projectImage2);

        projectImage2 = getProjectImageSample2();
        assertThat(projectImage1).isNotEqualTo(projectImage2);
    }

    @Test
    void projectTest() {
        ProjectImage projectImage = getProjectImageRandomSampleGenerator();
        Project projectBack = getProjectRandomSampleGenerator();

        projectImage.setProject(projectBack);
        assertThat(projectImage.getProject()).isEqualTo(projectBack);

        projectImage.project(null);
        assertThat(projectImage.getProject()).isNull();
    }
}
