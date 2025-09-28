package com.paradox.zswebsite.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

/**
 * A DTO for the detailed view of a CurriculumVitae entity, used in the modal.
 */
public class CurriculumVitaeDetailDTO implements Serializable {

    private Long id;
    private String companyName;
    private String jobDescriptionHTML;
    private LocalDate startDate;
    private LocalDate endDate;
    private List<CurriculumVitaeMediaDTO> mediaFiles;

    // Getters and Setters
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

    public List<CurriculumVitaeMediaDTO> getMediaFiles() {
        return mediaFiles;
    }

    public void setMediaFiles(List<CurriculumVitaeMediaDTO> mediaFiles) {
        this.mediaFiles = mediaFiles;
    }
}
