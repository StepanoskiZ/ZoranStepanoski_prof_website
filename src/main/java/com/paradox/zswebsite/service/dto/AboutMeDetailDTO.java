// In: src/main/java/com/paradox/zswebsite/service/dto/AboutMeDetailDTO.java

package com.paradox.zswebsite.service.dto;

import java.io.Serializable;
import java.util.List;

/**
 * A DTO for the detailed "About Me" view, used in the modal.
 */
public class AboutMeDetailDTO implements Serializable {

    private Long id;
    private String contentHtml;
    private List<AboutMeMediaDTO> mediaFiles;

    public AboutMeDetailDTO() {}

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

    public List<AboutMeMediaDTO> getMediaFiles() {
        return mediaFiles;
    }
    public void setMediaFiles(List<AboutMeMediaDTO> mediaFiles) {
        this.mediaFiles = mediaFiles;
    }
}
