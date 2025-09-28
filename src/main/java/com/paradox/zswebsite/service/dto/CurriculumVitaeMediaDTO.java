package com.paradox.zswebsite.service.dto;

import com.paradox.zswebsite.domain.enumeration.UnifiedMediaType;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.paradox.zswebsite.domain.CurriculumVitaeMedia} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CurriculumVitaeMediaDTO implements Serializable {

    private Long id;

    @NotNull
    private String mediaUrl;

    @NotNull
    private UnifiedMediaType curriculumVitaeMediaType;

    private String caption;

    private CurriculumVitaeDTO curriculumVitae;

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

    public UnifiedMediaType getCurriculumVitaeMediaType() {
        return curriculumVitaeMediaType;
    }

    public void setCurriculumVitaeMediaType(UnifiedMediaType curriculumVitaeMediaType) {
        this.curriculumVitaeMediaType = curriculumVitaeMediaType;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public CurriculumVitaeDTO getCurriculumVitae() {
        return curriculumVitae;
    }

    public void setCurriculumVitae(CurriculumVitaeDTO curriculumVitae) {
        this.curriculumVitae = curriculumVitae;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CurriculumVitaeMediaDTO)) {
            return false;
        }

        CurriculumVitaeMediaDTO curriculumVitaeMediaDTO = (CurriculumVitaeMediaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, curriculumVitaeMediaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CurriculumVitaeMediaDTO{" +
            "id=" + getId() +
            ", mediaUrl='" + getMediaUrl() + "'" +
            ", curriculumVitaeMediaType='" + getCurriculumVitaeMediaType() + "'" +
            ", caption='" + getCaption() + "'" +
            ", curriculumVitae=" + getCurriculumVitae() +
            "}";
    }
}
