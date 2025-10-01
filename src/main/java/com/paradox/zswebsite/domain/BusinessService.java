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
 * A BusinessService.
 */
@Entity
@Table(name = "business_service")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BusinessService implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @Lob
    @Column(name = "description_html", nullable = false)
    private String descriptionHTML;

    @Column(name = "icon")
    private String icon;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "businessService")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "businessService" }, allowSetters = true)
    private Set<BusinessServiceMedia> media = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public BusinessService id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public BusinessService title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescriptionHTML() {
        return this.descriptionHTML;
    }

    public BusinessService descriptionHTML(String descriptionHTML) {
        this.setDescriptionHTML(descriptionHTML);
        return this;
    }

    public void setDescriptionHTML(String descriptionHTML) {
        this.descriptionHTML = descriptionHTML;
    }

    public String getIcon() {
        return this.icon;
    }

    public BusinessService icon(String icon) {
        this.setIcon(icon);
        return this;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Set<BusinessServiceMedia> getMedia() {
        return this.media;
    }

    public void setMedia(Set<BusinessServiceMedia> businessServiceMedias) {
        if (this.media != null) {
            this.media.forEach(i -> i.setBusinessService(null));
        }
        if (businessServiceMedias != null) {
            businessServiceMedias.forEach(i -> i.setBusinessService(this));
        }
        this.media = businessServiceMedias;
    }

    public BusinessService media(Set<BusinessServiceMedia> businessServiceMedias) {
        this.setMedia(businessServiceMedias);
        return this;
    }

    public BusinessService addMedia(BusinessServiceMedia businessServiceMedia) {
        this.media.add(businessServiceMedia);
        businessServiceMedia.setBusinessService(this);
        return this;
    }

    public BusinessService removeMedia(BusinessServiceMedia businessServiceMedia) {
        this.media.remove(businessServiceMedia);
        businessServiceMedia.setBusinessService(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BusinessService)) {
            return false;
        }
        return getId() != null && getId().equals(((BusinessService) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BusinessService{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", descriptionHTML='" + getDescriptionHTML() + "'" +
            ", icon='" + getIcon() + "'" +
            "}";
    }
}
