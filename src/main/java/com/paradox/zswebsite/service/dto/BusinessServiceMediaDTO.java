package com.paradox.zswebsite.service.dto;

import com.paradox.zswebsite.domain.enumeration.UnifiedMediaType;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.paradox.zswebsite.domain.BusinessServiceMedia} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BusinessServiceMediaDTO implements Serializable {

    private Long id;

    @NotNull
    private String mediaUrl;

    @NotNull
    private UnifiedMediaType businessServiceMediaType;

    private String caption;

    private BusinessServiceDTO businessService;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMediaUrl() {
        return mediaUrl;
    }

    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }

    public UnifiedMediaType getBusinessServiceMediaType() {
        return businessServiceMediaType;
    }

    public void setBusinessServiceMediaType(UnifiedMediaType businessServiceMediaType) {
        this.businessServiceMediaType = businessServiceMediaType;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public BusinessServiceDTO getBusinessService() {
        return businessService;
    }

    public void setBusinessService(BusinessServiceDTO businessService) {
        this.businessService = businessService;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BusinessServiceMediaDTO)) {
            return false;
        }

        BusinessServiceMediaDTO businessServiceMediaDTO = (BusinessServiceMediaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, businessServiceMediaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BusinessServiceMediaDTO{" +
            "id=" + getId() +
            ", mediaUrl='" + getMediaUrl() + "'" +
            ", businessServiceMediaType='" + getBusinessServiceMediaType() + "'" +
            ", caption='" + getCaption() + "'" +
            ", businessService=" + getBusinessService() +
            "}";
    }
}
