package com.paradox.zswebsite.service.dto;

import com.paradox.zswebsite.domain.enumeration.UnifiedMediaType;

import java.io.Serializable;

public class AboutMeCardDTO implements Serializable {

    private Long id;
    private String contentHtml;
    private String firstMediaUrl;
    private UnifiedMediaType firstMediaType;

    // Constructors
    public AboutMeCardDTO() {}

    public AboutMeCardDTO(
        Long id,
        String contentHtml,
        String firstMediaUrl,
        UnifiedMediaType firstMediaType
    ) {
        this.id = id;
        this.contentHtml = contentHtml;
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

    public UnifiedMediaType getFirstMediaType() {
        return firstMediaType;
    }
    public void setFirstMediaType(UnifiedMediaType firstMediaType) {
        this.firstMediaType = firstMediaType;
    }
}
