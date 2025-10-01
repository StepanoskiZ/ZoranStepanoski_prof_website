package com.paradox.zswebsite.service.dto;

import java.io.Serializable;

public class BusinessServiceCardDTO implements Serializable {
    private Long id;
    private String title;
    private String description;
    private String firstMediaUrl;
    private String firstMediaType;

    // Constructors
    public BusinessServiceCardDTO() {}

    public BusinessServiceCardDTO(Long id, String title, String description, String firstMediaUrl, String firstMediaType) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.firstMediaUrl = firstMediaUrl;
        this.firstMediaType = firstMediaType;
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
    public String getFirstMediaUrl() {
        return firstMediaUrl;
    }
    public void setFirstMediaUrl(String firstMediaUrl) {
        this.firstMediaUrl = firstMediaUrl;
    }
    public String getFirstMediaType() {
        return firstMediaType;
    }
    public void setFirstMediaType(String firstMediaType) {
        this.firstMediaType = firstMediaType;
    }
}
