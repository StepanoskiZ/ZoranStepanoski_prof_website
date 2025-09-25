//package com.paradox.zswebsite.service.dto;
//
//import java.io.Serializable;
//
///**
// * A DTO representing a Project for display on the landing page card.
// */
//public class ProjectCardDTO implements Serializable {
//
//    private Long id;
//    private String title;
//    private String description;
//    private String firstImageUrl; // URL of the first image
//
//    public ProjectCardDTO() {
//        // Empty constructor needed for Jackson.
//    }
//
//    // Getters and Setters
//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public String getTitle() {
//        return title;
//    }
//
//    public void setTitle(String title) {
//        this.title = title;
//    }
//
//    public String getDescription() {
//        return description;
//    }
//
//    public void setDescription(String description) {
//        this.description = description;
//    }
//
//    public String getFirstImageUrl() {
//        return firstImageUrl;
//    }
//
//    public void setFirstImageUrl(String firstImageUrl) {
//        this.firstImageUrl = firstImageUrl;
//    }
//}
package com.paradox.zswebsite.service.dto;

import com.paradox.zswebsite.domain.enumeration.UnifiedMediaType;
import java.io.Serializable;

/**
 * A DTO representing a Project for display on the landing page card.
 */
public class ProjectCardDTO implements Serializable {

    private Long id;
    private String title;
    private String description;
    private String firstMediaUrl;
    private UnifiedMediaType firstMediaType;

    public ProjectCardDTO() {
        // Empty constructor needed for Jackson.
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

    public UnifiedMediaType getFirstMediaType() {
        return firstMediaType;
    }

    public void setFirstMediaType(UnifiedMediaType firstMediaType) {
        this.firstMediaType = firstMediaType;
    }
}
