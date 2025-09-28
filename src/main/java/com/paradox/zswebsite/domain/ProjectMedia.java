package com.paradox.zswebsite.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.paradox.zswebsite.domain.enumeration.UnifiedMediaType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ProjectMedia.
 */
@Entity
@Table(name = "project_media")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProjectMedia implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "media_url", nullable = false)
    private String mediaUrl;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "project_media_type", nullable = false)
    private UnifiedMediaType projectMediaType;

    @Column(name = "caption")
    private String caption;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "media" }, allowSetters = true)
    private Project project;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ProjectMedia id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMediaUrl() {
        return this.mediaUrl;
    }

    public ProjectMedia mediaUrl(String mediaUrl) {
        this.setMediaUrl(mediaUrl);
        return this;
    }

    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }

    public UnifiedMediaType getProjectMediaType() {
        return this.projectMediaType;
    }

    public ProjectMedia projectMediaType(UnifiedMediaType projectMediaType) {
        this.setProjectMediaType(projectMediaType);
        return this;
    }

    public void setProjectMediaType(UnifiedMediaType projectMediaType) {
        this.projectMediaType = projectMediaType;
    }

    public String getCaption() {
        return this.caption;
    }

    public ProjectMedia caption(String caption) {
        this.setCaption(caption);
        return this;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public Project getProject() {
        return this.project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public ProjectMedia project(Project project) {
        this.setProject(project);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProjectMedia)) {
            return false;
        }
        return getId() != null && getId().equals(((ProjectMedia) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProjectMedia{" +
            "id=" + getId() +
            ", mediaUrl='" + getMediaUrl() + "'" +
            ", projectMediaType='" + getProjectMediaType() + "'" +
            ", caption='" + getCaption() + "'" +
            "}";
    }
}
