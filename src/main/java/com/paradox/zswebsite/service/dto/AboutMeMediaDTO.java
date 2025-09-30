package com.paradox.zswebsite.service.dto;

import com.paradox.zswebsite.domain.enumeration.UnifiedMediaType;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.paradox.zswebsite.domain.AboutMeMedia} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AboutMeMediaDTO implements Serializable {

    private Long id;

    @NotNull
    private String mediaUrl;

    @NotNull
    private UnifiedMediaType aboutMeMediaType;

    private String caption;

    private AboutMeDTO aboutMe;

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

    public UnifiedMediaType getAboutMeMediaType() {
        return aboutMeMediaType;
    }

    public void setAboutMeMediaType(UnifiedMediaType aboutMeMediaType) {
        this.aboutMeMediaType = aboutMeMediaType;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public AboutMeDTO getAboutMe() { return aboutMe; }

    public void setAboutMe(AboutMeDTO aboutMe) {
        this.aboutMe = aboutMe;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AboutMeMediaDTO)) {
            return false;
        }

        AboutMeMediaDTO aboutMeMediaDTO = (AboutMeMediaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, aboutMeMediaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AboutMeMediaDTO{" +
            "id=" + getId() +
            ", mediaUrl='" + getMediaUrl() + "'" +
            ", aboutMeMediaType='" + getAboutMeMediaType() + "'" +
            ", caption='" + getCaption() + "'" +
            ", aboutMe=" + getAboutMe() +
            "}";
    }
}
