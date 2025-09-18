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
    private List<String> imageUrls; // A list of all image URLs

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

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }
}
