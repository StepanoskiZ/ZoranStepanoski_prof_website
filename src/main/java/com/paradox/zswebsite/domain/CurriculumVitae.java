package com.paradox.zswebsite.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.paradox.zswebsite.domain.enumeration.Language;
import com.paradox.zswebsite.domain.enumeration.WorkingStatus;
import com.paradox.zswebsite.domain.enumeration.WorkingType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A CurriculumVitae.
 */
@Entity
@Table(name = "curriculum_vitae")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CurriculumVitae implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "company_name", nullable = false)
    private String companyName;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "language", nullable = false)
    private Language language;

    @Lob
    @Column(name = "job_description_html", nullable = false)
    private String jobDescriptionHTML;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private WorkingStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private WorkingType type;

    @Column(name = "category")
    private String category;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "curriculumVitae")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "curriculumVitae" }, allowSetters = true)
    private Set<CurriculumVitaeMedia> media = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CurriculumVitae id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCompanyName() {
        return this.companyName;
    }

    public CurriculumVitae companyName(String companyName) {
        this.setCompanyName(companyName);
        return this;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Language getLanguage() {
        return this.language;
    }

    public CurriculumVitae language(Language language) {
        this.setLanguage(language);
        return this;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public String getJobDescriptionHTML() {
        return this.jobDescriptionHTML;
    }

    public CurriculumVitae jobDescriptionHTML(String jobDescriptionHTML) {
        this.setJobDescriptionHTML(jobDescriptionHTML);
        return this;
    }

    public void setJobDescriptionHTML(String jobDescriptionHTML) {
        this.jobDescriptionHTML = jobDescriptionHTML;
    }

    public LocalDate getStartDate() {
        return this.startDate;
    }

    public CurriculumVitae startDate(LocalDate startDate) {
        this.setStartDate(startDate);
        return this;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return this.endDate;
    }

    public CurriculumVitae endDate(LocalDate endDate) {
        this.setEndDate(endDate);
        return this;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public WorkingStatus getStatus() {
        return this.status;
    }

    public CurriculumVitae status(WorkingStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(WorkingStatus status) {
        this.status = status;
    }

    public WorkingType getType() {
        return this.type;
    }

    public CurriculumVitae type(WorkingType type) {
        this.setType(type);
        return this;
    }

    public void setType(WorkingType type) {
        this.type = type;
    }

    public String getCategory() {
        return this.category;
    }

    public CurriculumVitae category(String category) {
        this.setCategory(category);
        return this;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Set<CurriculumVitaeMedia> getMedia() {
        return this.media;
    }

    public void setMedia(Set<CurriculumVitaeMedia> curriculumVitaeMedias) {
        if (this.media != null) {
            this.media.forEach(i -> i.setCurriculumVitae(null));
        }
        if (curriculumVitaeMedias != null) {
            curriculumVitaeMedias.forEach(i -> i.setCurriculumVitae(this));
        }
        this.media = curriculumVitaeMedias;
    }

    public CurriculumVitae media(Set<CurriculumVitaeMedia> curriculumVitaeMedias) {
        this.setMedia(curriculumVitaeMedias);
        return this;
    }

    public CurriculumVitae addMedia(CurriculumVitaeMedia curriculumVitaeMedia) {
        this.media.add(curriculumVitaeMedia);
        curriculumVitaeMedia.setCurriculumVitae(this);
        return this;
    }

    public CurriculumVitae removeMedia(CurriculumVitaeMedia curriculumVitaeMedia) {
        this.media.remove(curriculumVitaeMedia);
        curriculumVitaeMedia.setCurriculumVitae(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CurriculumVitae)) {
            return false;
        }
        return getId() != null && getId().equals(((CurriculumVitae) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CurriculumVitae{" +
            "id=" + getId() +
            ", companyName='" + getCompanyName() + "'" +
            ", language='" + getLanguage() + "'" +
            ", jobDescriptionHTML='" + getJobDescriptionHTML() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", status='" + getStatus() + "'" +
            ", type='" + getType() + "'" +
            ", category='" + getCategory() + "'" +
            "}";
    }
}
