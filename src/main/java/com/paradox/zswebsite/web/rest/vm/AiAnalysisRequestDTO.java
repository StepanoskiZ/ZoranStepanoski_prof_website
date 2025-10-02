package com.paradox.zswebsite.web.rest.vm; // Or your preferred package

import com.paradox.zswebsite.service.dto.AboutMeDTO;
import com.paradox.zswebsite.service.dto.CurriculumVitaeDTO;
import com.paradox.zswebsite.service.dto.ProjectDTO;
import com.paradox.zswebsite.service.dto.SkillDTO;
import java.util.List;

public class AiAnalysisRequestDTO {

    private String jobPost;
    private List<CurriculumVitaeDTO> cvEntries;
    private List<ProjectDTO> projects;
    private List<SkillDTO> skills;
    private List<AboutMeDTO> aboutMe;
    private String modelName;

    public String getJobPost() {
        return jobPost;
    }
    public void setJobPost(String jobPost) {
        this.jobPost = jobPost;
    }

    public List<CurriculumVitaeDTO> getCvEntries() {
        return cvEntries;
    }
    public void setCvEntries(List<CurriculumVitaeDTO> cvEntries) {
        this.cvEntries = cvEntries;
    }

    public List<ProjectDTO> getProjects() {
        return projects;
    }
    public void setProjects(List<ProjectDTO> projects) {
        this.projects = projects;
    }

    public List<SkillDTO> getSkills() {
        return skills;
    }
    public void setSkills(List<SkillDTO> skills) {
        this.skills = skills;
    }

    public List<AboutMeDTO> getAboutMe() {
        return aboutMe;
    }
    public void setAboutMe(List<AboutMeDTO> aboutMe) {
        this.aboutMe = aboutMe;
    }

    public String getModelName() { return modelName; }
    public void setModelName(String modelName) { this.modelName = modelName; }
}
