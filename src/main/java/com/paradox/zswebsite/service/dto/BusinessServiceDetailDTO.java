package com.paradox.zswebsite.service.dto;

import java.io.Serializable;
import java.util.List;

public class BusinessServiceDetailDTO implements Serializable {
    private Long id;
    private String title;
    private String description;
    private List<BusinessServiceMediaDTO> mediaFiles;

    // Constructors
    public BusinessServiceDetailDTO() {}

    public BusinessServiceDetailDTO(Long id, String title, String description, List<BusinessServiceMediaDTO> mediaFiles) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.mediaFiles = mediaFiles;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public List<BusinessServiceMediaDTO> getMediaFiles() {
        return mediaFiles;
    }
    public void setMediaFiles(List<BusinessServiceMediaDTO> mediaFiles) {
        this.mediaFiles = mediaFiles;
    }
}
