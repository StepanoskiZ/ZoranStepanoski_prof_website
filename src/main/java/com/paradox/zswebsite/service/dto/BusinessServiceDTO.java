package com.paradox.zswebsite.service.dto;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.paradox.zswebsite.domain.BusinessService} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BusinessServiceDTO implements Serializable {

    private Long id;

    @NotNull
    private String title;

    @Lob
    private String descriptionHTML;

    private String icon;

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

    public String getDescriptionHTML() {
        return descriptionHTML;
    }

    public void setDescriptionHTML(String descriptionHTML) {
        this.descriptionHTML = descriptionHTML;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BusinessServiceDTO)) {
            return false;
        }

        BusinessServiceDTO businessServiceDTO = (BusinessServiceDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, businessServiceDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BusinessServiceDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", descriptionHTML='" + getDescriptionHTML() + "'" +
            ", icon='" + getIcon() + "'" +
            "}";
    }
}
