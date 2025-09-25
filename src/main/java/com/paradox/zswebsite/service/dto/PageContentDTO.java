package com.paradox.zswebsite.service.dto;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.paradox.zswebsite.domain.PageContent} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PageContentDTO implements Serializable {

    private Long id;

    @NotNull
    private String sectionKey;

    @Lob
    private String contentHtml;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSectionKey() {
        return sectionKey;
    }

    public void setSectionKey(String sectionKey) {
        this.sectionKey = sectionKey;
    }

    public String getContentHtml() {
        return contentHtml;
    }

    public void setContentHtml(String contentHtml) {
        this.contentHtml = contentHtml;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PageContentDTO)) {
            return false;
        }

        PageContentDTO pageContentDTO = (PageContentDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, pageContentDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PageContentDTO{" +
            "id=" + getId() +
            ", sectionKey='" + getSectionKey() + "'" +
            ", contentHtml='" + getContentHtml() + "'" +
            "}";
    }
}
