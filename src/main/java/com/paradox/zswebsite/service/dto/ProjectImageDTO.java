package com.paradox.zswebsite.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.paradox.zswebsite.domain.ProjectImage} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProjectImageDTO implements Serializable {

    private Long id;

    @NotNull
    private String imageUrl;

    private String caption;

    private ProjectDTO project;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public ProjectDTO getProject() {
        return project;
    }

    public void setProject(ProjectDTO project) {
        this.project = project;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProjectImageDTO)) {
            return false;
        }

        ProjectImageDTO projectImageDTO = (ProjectImageDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, projectImageDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProjectImageDTO{" +
            "id=" + getId() +
            ", imageUrl='" + getImageUrl() + "'" +
            ", caption='" + getCaption() + "'" +
            ", project=" + getProject() +
            "}";
    }
}
