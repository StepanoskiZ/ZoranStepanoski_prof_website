package com.paradox.zswebsite.service.dto;

import com.paradox.zswebsite.domain.enumeration.Language;
import com.paradox.zswebsite.domain.enumeration.WorkingStatus;
import com.paradox.zswebsite.domain.enumeration.WorkingType;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.paradox.zswebsite.domain.CurriculumVitae} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CurriculumVitaeDTO implements Serializable {

    private Long id;

    @NotNull
    private String companyName;

    @NotNull
    private Language language;

    @Lob
    private String jobDescriptionHTML;

    private LocalDate startDate;

    private LocalDate endDate;

    private WorkingStatus status;

    private WorkingType type;

    private String category;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public String getJobDescriptionHTML() {
        return jobDescriptionHTML;
    }

    public void setJobDescriptionHTML(String jobDescriptionHTML) {
        this.jobDescriptionHTML = jobDescriptionHTML;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public WorkingStatus getStatus() {
        return status;
    }

    public void setStatus(WorkingStatus status) {
        this.status = status;
    }

    public WorkingType getType() {
        return type;
    }

    public void setType(WorkingType type) {
        this.type = type;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CurriculumVitaeDTO)) {
            return false;
        }

        CurriculumVitaeDTO curriculumVitaeDTO = (CurriculumVitaeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, curriculumVitaeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CurriculumVitaeDTO{" +
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
