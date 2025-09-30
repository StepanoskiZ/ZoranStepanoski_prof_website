package com.paradox.zswebsite.service.dto;

import java.io.Serializable;

public class AboutMeCardDTO implements Serializable {

    private Long id;
    private String contentHtml;
    private String firstMediaUrl;

    // Constructors
    public AboutMeCardDTO(Long id, String contentHtml, String firstMediaUrl) {
        this.id = id;
        this.contentHtml = contentHtml;
        this.firstMediaUrl = firstMediaUrl;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getContentHtml() {
        return contentHtml;
    }
    public void setContentHtml(String contentHtml) {
        this.contentHtml = contentHtml;
    }

    public String getFirstMediaUrl() {
        return firstMediaUrl;
    }
    public void setFirstMediaUrl(String firstMediaUrl) {
        this.firstMediaUrl = firstMediaUrl;
    }
}
