package com.paradox.zswebsite.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.paradox.zswebsite.domain.enumeration.UnifiedMediaType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A AboutMeMedia.
 */
@Entity
@Table(name = "about_me_media")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AboutMeMedia implements Serializable {

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
    @Column(name = "about_me_media_type", nullable = false)
    private UnifiedMediaType aboutMeMediaType;

    @Column(name = "caption")
    private String caption;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "media" }, allowSetters = true)
    private AboutMe aboutMe;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AboutMeMedia id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMediaUrl() {
        return this.mediaUrl;
    }

    public AboutMeMedia mediaUrl(String mediaUrl) {
        this.setMediaUrl(mediaUrl);
        return this;
    }

    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }

    public UnifiedMediaType getAboutMeMediaType() {
        return this.aboutMeMediaType;
    }

    public AboutMeMedia aboutMeMediaType(UnifiedMediaType aboutMeMediaType) {
        this.setAboutMeMediaType(aboutMeMediaType);
        return this;
    }

    public void setAboutMeMediaType(UnifiedMediaType aboutMeMediaType) {
        this.aboutMeMediaType = aboutMeMediaType;
    }

    public String getCaption() {
        return this.caption;
    }

    public AboutMeMedia caption(String caption) {
        this.setCaption(caption);
        return this;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public AboutMe getAboutMe() {
        return this.aboutMe;
    }

    public void setAboutMe(AboutMe aboutMe) {
        this.aboutMe = aboutMe;
    }

    public AboutMeMedia aboutMe(AboutMe aboutMe) {
        this.setAboutMe(aboutMe);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AboutMeMedia)) {
            return false;
        }
        return getId() != null && getId().equals(((AboutMeMedia) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AboutMeMedia{" +
            "id=" + getId() +
            ", mediaUrl='" + getMediaUrl() + "'" +
            ", aboutMeMediaType='" + getAboutMeMediaType() + "'" +
            ", caption='" + getCaption() + "'" +
            "}";
    }
}
