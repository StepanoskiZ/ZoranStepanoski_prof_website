package com.paradox.zswebsite.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.paradox.zswebsite.domain.enumeration.UnifiedMediaType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A PageContentMedia.
 */
@Entity
@Table(name = "page_content_media")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PageContentMedia implements Serializable {

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
    @Column(name = "page_content_media_type", nullable = false)
    private UnifiedMediaType pageContentMediaType;

    @Column(name = "caption")
    private String caption;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "media" }, allowSetters = true)
    private PageContent pagecontent;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PageContentMedia id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMediaUrl() {
        return this.mediaUrl;
    }

    public PageContentMedia mediaUrl(String mediaUrl) {
        this.setMediaUrl(mediaUrl);
        return this;
    }

    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }

    public UnifiedMediaType getPageContentMediaType() {
        return this.pageContentMediaType;
    }

    public PageContentMedia pageContentMediaType(UnifiedMediaType pageContentMediaType) {
        this.setPageContentMediaType(pageContentMediaType);
        return this;
    }

    public void setPageContentMediaType(UnifiedMediaType pageContentMediaType) {
        this.pageContentMediaType = pageContentMediaType;
    }

    public String getCaption() {
        return this.caption;
    }

    public PageContentMedia caption(String caption) {
        this.setCaption(caption);
        return this;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public PageContent getPagecontent() {
        return this.pagecontent;
    }

    public void setPagecontent(PageContent pageContent) {
        this.pagecontent = pageContent;
    }

    public PageContentMedia pagecontent(PageContent pageContent) {
        this.setPagecontent(pageContent);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PageContentMedia)) {
            return false;
        }
        return getId() != null && getId().equals(((PageContentMedia) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PageContentMedia{" +
            "id=" + getId() +
            ", mediaUrl='" + getMediaUrl() + "'" +
            ", pageContentMediaType='" + getPageContentMediaType() + "'" +
            ", caption='" + getCaption() + "'" +
            "}";
    }
}
