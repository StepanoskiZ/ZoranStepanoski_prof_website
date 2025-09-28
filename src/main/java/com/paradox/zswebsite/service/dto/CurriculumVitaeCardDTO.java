package com.paradox.zswebsite.service.dto;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * A DTO for representing a CurriculumVitae entry in a simplified card format for the landing page.
 */
public class CurriculumVitaeCardDTO implements Serializable {

    private Long id;
    private String companyName;
    private String jobDescriptionHTML;
    private LocalDate startDate;
    private LocalDate endDate;
    private String firstMediaUrl; // Placeholder for a potential future image

    // --- Getters and Setters ---

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

    public String getFirstMediaUrl() {
        return firstMediaUrl;
    }

    public void setFirstMediaUrl(String firstMediaUrl) {
        this.firstMediaUrl = firstMediaUrl;
    }
}
