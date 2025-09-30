package com.paradox.zswebsite.service.dto;

import java.io.Serializable;

public class AboutMeCardDTO implements Serializable {

    private String contentHtml;
    private String firstMediaUrl;

    // Constructors
    public AboutMeCardDTO() {}

    public AboutMeCardDTO(String contentHtml, String firstMediaUrl) {
        this.contentHtml = contentHtml;
        this.firstMediaUrl = firstMediaUrl;
    }

    // Getters and Setters
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
