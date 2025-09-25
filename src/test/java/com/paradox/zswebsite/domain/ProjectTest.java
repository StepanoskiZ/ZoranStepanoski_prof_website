package com.paradox.zswebsite.domain;

import static com.paradox.zswebsite.domain.ProjectMediaTestSamples.*;
import static com.paradox.zswebsite.domain.ProjectTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.paradox.zswebsite.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ProjectTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Project.class);
        Project project1 = getProjectSample1();
        Project project2 = new Project();
        assertThat(project1).isNotEqualTo(project2);

        project2.setId(project1.getId());
        assertThat(project1).isEqualTo(project2);

        project2 = getProjectSample2();
        assertThat(project1).isNotEqualTo(project2);
    }

    @Test
    void mediaTest() {
        Project project = getProjectRandomSampleGenerator();
        ProjectMedia projectMediaBack = getProjectMediaRandomSampleGenerator();

        project.addMedia(projectMediaBack);
        assertThat(project.getMedia()).containsOnly(projectMediaBack);
        assertThat(projectMediaBack.getProject()).isEqualTo(project);

        project.removeMedia(projectMediaBack);
        assertThat(project.getMedia()).doesNotContain(projectMediaBack);
        assertThat(projectMediaBack.getProject()).isNull();

        project.media(new HashSet<>(Set.of(projectMediaBack)));
        assertThat(project.getMedia()).containsOnly(projectMediaBack);
        assertThat(projectMediaBack.getProject()).isEqualTo(project);

        project.setMedia(new HashSet<>());
        assertThat(project.getMedia()).doesNotContain(projectMediaBack);
        assertThat(projectMediaBack.getProject()).isNull();
    }
}
