package com.paradox.zswebsite.domain;

import static com.paradox.zswebsite.domain.ProjectMediaTestSamples.*;
import static com.paradox.zswebsite.domain.ProjectTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.paradox.zswebsite.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProjectMediaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProjectMedia.class);
        ProjectMedia projectMedia1 = getProjectMediaSample1();
        ProjectMedia projectMedia2 = new ProjectMedia();
        assertThat(projectMedia1).isNotEqualTo(projectMedia2);

        projectMedia2.setId(projectMedia1.getId());
        assertThat(projectMedia1).isEqualTo(projectMedia2);

        projectMedia2 = getProjectMediaSample2();
        assertThat(projectMedia1).isNotEqualTo(projectMedia2);
    }

    @Test
    void projectTest() throws Exception {
        ProjectMedia projectMedia = getProjectMediaRandomSampleGenerator();
        Project projectBack = getProjectRandomSampleGenerator();

        projectMedia.setProject(projectBack);
        assertThat(projectMedia.getProject()).isEqualTo(projectBack);

        projectMedia.project(null);
        assertThat(projectMedia.getProject()).isNull();
    }
}
