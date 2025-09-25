package com.paradox.zswebsite.service.dto;

import java.io.Serializable;
import java.util.List;

/**
 * A DTO for the detailed project view, including all images.
 */
public class ProjectDetailDTO implements Serializable {

    private Long id;
    private String title;
    private String description; // Full description
    private String url;
    private List<String> mediaUrls; // The field your frontend will receive

    // Getters and Setters for all fields...

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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<String> getMediaUrls() {
        return mediaUrls;
    }

    public void setMediaUrls(List<String> mediaUrls) {
        this.mediaUrls = mediaUrls;
    }
}
