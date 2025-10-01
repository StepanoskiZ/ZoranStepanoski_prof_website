package com.paradox.zswebsite.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.paradox.zswebsite.domain.enumeration.UnifiedMediaType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A BusinessServiceMedia.
 */
@Entity
@Table(name = "business_service_media")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BusinessServiceMedia implements Serializable {

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
    @Column(name = "business_service_media_type", nullable = false)
    private UnifiedMediaType businessServiceMediaType;

    @Column(name = "caption")
    private String caption;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "media" }, allowSetters = true)
    private BusinessService businessService;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public BusinessServiceMedia id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMediaUrl() {
        return this.mediaUrl;
    }

    public BusinessServiceMedia mediaUrl(String mediaUrl) {
        this.setMediaUrl(mediaUrl);
        return this;
    }

    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }

    public UnifiedMediaType getBusinessServiceMediaType() {
        return this.businessServiceMediaType;
    }

    public BusinessServiceMedia businessServiceMediaType(UnifiedMediaType businessServiceMediaType) {
        this.setBusinessServiceMediaType(businessServiceMediaType);
        return this;
    }

    public void setBusinessServiceMediaType(UnifiedMediaType businessServiceMediaType) {
        this.businessServiceMediaType = businessServiceMediaType;
    }

    public String getCaption() {
        return this.caption;
    }

    public BusinessServiceMedia caption(String caption) {
        this.setCaption(caption);
        return this;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public BusinessService getBusinessService() {
        return this.businessService;
    }

    public void setBusinessService(BusinessService businessService) {
        this.businessService = businessService;
    }

    public BusinessServiceMedia businessService(BusinessService businessService) {
        this.setBusinessService(businessService);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BusinessServiceMedia)) {
            return false;
        }
        return getId() != null && getId().equals(((BusinessServiceMedia) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BusinessServiceMedia{" +
            "id=" + getId() +
            ", mediaUrl='" + getMediaUrl() + "'" +
            ", businessServiceMediaType='" + getBusinessServiceMediaType() + "'" +
            ", caption='" + getCaption() + "'" +
            "}";
    }
}
