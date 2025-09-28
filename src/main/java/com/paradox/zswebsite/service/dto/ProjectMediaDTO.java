package com.paradox.zswebsite.service.dto;

import com.paradox.zswebsite.domain.enumeration.UnifiedMediaType;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.paradox.zswebsite.domain.ProjectMedia} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProjectMediaDTO implements Serializable {

    private Long id;

    @NotNull
    private String mediaUrl;

    @NotNull
    private UnifiedMediaType projectMediaType;

    private String caption;

    private ProjectDTO project;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMediaUrl() {
        return mediaUrl;
    }

    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }

    public UnifiedMediaType getProjectMediaType() {
        return projectMediaType;
    }

    public void setProjectMediaType(UnifiedMediaType projectMediaType) {
        this.projectMediaType = projectMediaType;
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
        if (!(o instanceof ProjectMediaDTO)) {
            return false;
        }

        ProjectMediaDTO projectMediaDTO = (ProjectMediaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, projectMediaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProjectMediaDTO{" +
            "id=" + getId() +
            ", mediaUrl='" + getMediaUrl() + "'" +
            ", projectMediaType='" + getProjectMediaType() + "'" +
            ", caption='" + getCaption() + "'" +
            ", project=" + getProject() +
            "}";
    }
}
