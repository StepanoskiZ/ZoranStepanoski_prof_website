package com.paradox.zswebsite.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.paradox.zswebsite.domain.enumeration.UnifiedMediaType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A CurriculumVitaeMedia.
 */
@Entity
@Table(name = "curriculum_vitae_media")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CurriculumVitaeMedia implements Serializable {

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
    @Column(name = "curriculum_vitae_media_type", nullable = false)
    private UnifiedMediaType curriculumVitaeMediaType;

    @Column(name = "caption")
    private String caption;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "media" }, allowSetters = true)
    private CurriculumVitae curriculumVitae;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CurriculumVitaeMedia id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMediaUrl() {
        return this.mediaUrl;
    }

    public CurriculumVitaeMedia mediaUrl(String mediaUrl) {
        this.setMediaUrl(mediaUrl);
        return this;
    }

    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }

    public UnifiedMediaType getCurriculumVitaeMediaType() {
        return this.curriculumVitaeMediaType;
    }

    public CurriculumVitaeMedia curriculumVitaeMediaType(UnifiedMediaType curriculumVitaeMediaType) {
        this.setCurriculumVitaeMediaType(curriculumVitaeMediaType);
        return this;
    }

    public void setCurriculumVitaeMediaType(UnifiedMediaType curriculumVitaeMediaType) {
        this.curriculumVitaeMediaType = curriculumVitaeMediaType;
    }

    public String getCaption() {
        return this.caption;
    }

    public CurriculumVitaeMedia caption(String caption) {
        this.setCaption(caption);
        return this;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public CurriculumVitae getCurriculumVitae() {
        return this.curriculumVitae;
    }

    public void setCurriculumVitae(CurriculumVitae curriculumVitae) {
        this.curriculumVitae = curriculumVitae;
    }

    public CurriculumVitaeMedia curriculumVitae(CurriculumVitae curriculumVitae) {
        this.setCurriculumVitae(curriculumVitae);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CurriculumVitaeMedia)) {
            return false;
        }
        return getId() != null && getId().equals(((CurriculumVitaeMedia) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CurriculumVitaeMedia{" +
            "id=" + getId() +
            ", mediaUrl='" + getMediaUrl() + "'" +
            ", curriculumVitaeMediaType='" + getCurriculumVitaeMediaType() + "'" +
            ", caption='" + getCaption() + "'" +
            "}";
    }
}
