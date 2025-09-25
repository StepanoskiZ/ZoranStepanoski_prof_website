package com.paradox.zswebsite.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A PageContent.
 */
@Entity
@Table(name = "page_content")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PageContent implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "section_key", nullable = false, unique = true)
    private String sectionKey;

    @Lob
    @Column(name = "content_html", nullable = false)
    private String contentHtml;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "pagecontent")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "pagecontent" }, allowSetters = true)
    private Set<PageContentMedia> media = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PageContent id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSectionKey() {
        return this.sectionKey;
    }

    public PageContent sectionKey(String sectionKey) {
        this.setSectionKey(sectionKey);
        return this;
    }

    public void setSectionKey(String sectionKey) {
        this.sectionKey = sectionKey;
    }

    public String getContentHtml() {
        return this.contentHtml;
    }

    public PageContent contentHtml(String contentHtml) {
        this.setContentHtml(contentHtml);
        return this;
    }

    public void setContentHtml(String contentHtml) {
        this.contentHtml = contentHtml;
    }

    public Set<PageContentMedia> getMedia() {
        return this.media;
    }

    public void setMedia(Set<PageContentMedia> pageContentMedias) {
        if (this.media != null) {
            this.media.forEach(i -> i.setPagecontent(null));
        }
        if (pageContentMedias != null) {
            pageContentMedias.forEach(i -> i.setPagecontent(this));
        }
        this.media = pageContentMedias;
    }

    public PageContent media(Set<PageContentMedia> pageContentMedias) {
        this.setMedia(pageContentMedias);
        return this;
    }

    public PageContent addMedia(PageContentMedia pageContentMedia) {
        this.media.add(pageContentMedia);
        pageContentMedia.setPagecontent(this);
        return this;
    }

    public PageContent removeMedia(PageContentMedia pageContentMedia) {
        this.media.remove(pageContentMedia);
        pageContentMedia.setPagecontent(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PageContent)) {
            return false;
        }
        return getId() != null && getId().equals(((PageContent) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PageContent{" +
            "id=" + getId() +
            ", sectionKey='" + getSectionKey() + "'" +
            ", contentHtml='" + getContentHtml() + "'" +
            "}";
    }
}
