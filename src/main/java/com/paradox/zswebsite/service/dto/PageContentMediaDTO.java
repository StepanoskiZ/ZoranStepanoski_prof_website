package com.paradox.zswebsite.service.dto;

import com.paradox.zswebsite.domain.enumeration.UnifiedMediaType;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.paradox.zswebsite.domain.PageContentMedia} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PageContentMediaDTO implements Serializable {

    private Long id;

    @NotNull
    private String mediaUrl;

    @NotNull
    private UnifiedMediaType pageContentMediaType;

    private String caption;

    private PageContentDTO pagecontent;

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

    public UnifiedMediaType getPageContentMediaType() {
        return pageContentMediaType;
    }

    public void setPageContentMediaType(UnifiedMediaType pageContentMediaType) {
        this.pageContentMediaType = pageContentMediaType;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public PageContentDTO getPagecontent() {
        return pagecontent;
    }

    public void setPagecontent(PageContentDTO pagecontent) {
        this.pagecontent = pagecontent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PageContentMediaDTO)) {
            return false;
        }

        PageContentMediaDTO pageContentMediaDTO = (PageContentMediaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, pageContentMediaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PageContentMediaDTO{" +
            "id=" + getId() +
            ", mediaUrl='" + getMediaUrl() + "'" +
            ", pageContentMediaType='" + getPageContentMediaType() + "'" +
            ", caption='" + getCaption() + "'" +
            ", pagecontent=" + getPagecontent() +
            "}";
    }
}
