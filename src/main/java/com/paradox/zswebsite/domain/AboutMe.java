package com.paradox.zswebsite.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.paradox.zswebsite.domain.enumeration.Language;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A AboutMe.
 */
@Entity
@Table(name = "about_me")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AboutMe implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Lob
    @Column(name = "content_html", nullable = false)
    private String contentHtml;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "language", nullable = false)
    private Language language;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "aboutMe")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "aboutMe" }, allowSetters = true)
    private Set<AboutMeMedia> media = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AboutMe id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContentHtml() {
        return this.contentHtml;
    }

    public AboutMe contentHtml(String contentHtml) {
        this.setContentHtml(contentHtml);
        return this;
    }

    public void setContentHtml(String contentHtml) {
        this.contentHtml = contentHtml;
    }

    public Language getLanguage() {
        return this.language;
    }

    public AboutMe language(Language language) {
        this.setLanguage(language);
        return this;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public Set<AboutMeMedia> getMedia() {
        return this.media;
    }

    public void setMedia(Set<AboutMeMedia> aboutMeMedias) {
        if (this.media != null) {
            this.media.forEach(i -> i.setAboutMe(null));
        }
        if (aboutMeMedias != null) {
            aboutMeMedias.forEach(i -> i.setAboutMe(this));
        }
        this.media = aboutMeMedias;
    }

    public AboutMe media(Set<AboutMeMedia> aboutMeMedias) {
        this.setMedia(aboutMeMedias);
        return this;
    }

    public AboutMe addMedia(AboutMeMedia aboutMeMedia) {
        this.media.add(aboutMeMedia);
        aboutMeMedia.setAboutMe(this);
        return this;
    }

    public AboutMe removeMedia(AboutMeMedia aboutMeMedia) {
        this.media.remove(aboutMeMedia);
        aboutMeMedia.setAboutMe(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AboutMe)) {
            return false;
        }
        return getId() != null && getId().equals(((AboutMe) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AboutMe{" +
            "id=" + getId() +
            ", contentHtml='" + getContentHtml() + "'" +
            ", language='" + getLanguage() + "'" +
            "}";
    }
}
