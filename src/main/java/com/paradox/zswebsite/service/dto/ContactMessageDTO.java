package com.paradox.zswebsite.service.dto;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.paradox.zswebsite.domain.ContactMessage} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ContactMessageDTO implements Serializable {

    private Long id;

    @NotNull
    private String visitorName;

    @NotNull
    private String visitorEmail;

    @Lob
    private String message;

    @NotNull
    private Instant submittedDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVisitorName() {
        return visitorName;
    }

    public void setVisitorName(String visitorName) {
        this.visitorName = visitorName;
    }

    public String getVisitorEmail() {
        return visitorEmail;
    }

    public void setVisitorEmail(String visitorEmail) {
        this.visitorEmail = visitorEmail;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Instant getSubmittedDate() {
        return submittedDate;
    }

    public void setSubmittedDate(Instant submittedDate) {
        this.submittedDate = submittedDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ContactMessageDTO)) {
            return false;
        }

        ContactMessageDTO contactMessageDTO = (ContactMessageDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, contactMessageDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ContactMessageDTO{" +
            "id=" + getId() +
            ", visitorName='" + getVisitorName() + "'" +
            ", visitorEmail='" + getVisitorEmail() + "'" +
            ", message='" + getMessage() + "'" +
            ", submittedDate='" + getSubmittedDate() + "'" +
            "}";
    }
}
