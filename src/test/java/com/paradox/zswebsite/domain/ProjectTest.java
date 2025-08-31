package com.paradox.zswebsite.domain;

import static com.paradox.zswebsite.domain.ProjectImageTestSamples.*;
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
    void imageTest() {
        Project project = getProjectRandomSampleGenerator();
        ProjectImage projectImageBack = getProjectImageRandomSampleGenerator();

        project.addImage(projectImageBack);
        assertThat(project.getImages()).containsOnly(projectImageBack);
        assertThat(projectImageBack.getProject()).isEqualTo(project);

        project.removeImage(projectImageBack);
        assertThat(project.getImages()).doesNotContain(projectImageBack);
        assertThat(projectImageBack.getProject()).isNull();

        project.images(new HashSet<>(Set.of(projectImageBack)));
        assertThat(project.getImages()).containsOnly(projectImageBack);
        assertThat(projectImageBack.getProject()).isEqualTo(project);

        project.setImages(new HashSet<>());
        assertThat(project.getImages()).doesNotContain(projectImageBack);
        assertThat(projectImageBack.getProject()).isNull();
    }
}
