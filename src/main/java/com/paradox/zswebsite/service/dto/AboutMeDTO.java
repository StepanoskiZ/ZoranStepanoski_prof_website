package com.paradox.zswebsite.service.dto;

import com.paradox.zswebsite.domain.enumeration.Language;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import java.util.HashSet;
import java.util.Set;

/**
 * A DTO for the {@link com.paradox.zswebsite.domain.AboutMe} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AboutMeDTO implements Serializable {

    private Long id;

    @Lob
    private String contentHtml;

    private Set<AboutMeMediaDTO> mediaFiles = new HashSet<>();

    @NotNull
    private Language language;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContentHtml() {
        return contentHtml;
    }

    public void setContentHtml(String contentHtml) {
        this.contentHtml = contentHtml;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public Set<AboutMeMediaDTO> getMediaFiles() { return mediaFiles; }

    public void setMediaFiles(Set<AboutMeMediaDTO> mediaFiles) { this.mediaFiles = mediaFiles; }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AboutMeDTO)) {
            return false;
        }

        AboutMeDTO aboutMeDTO = (AboutMeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, aboutMeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AboutMeDTO{" +
            "id=" + getId() +
            ", contentHtml='" + getContentHtml() + "'" +
            ", language='" + getLanguage() + "'" +
            "}";
    }
}
