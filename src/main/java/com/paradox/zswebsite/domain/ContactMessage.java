package com.paradox.zswebsite.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ContactMessage.
 */
@Entity
@Table(name = "contact_message")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ContactMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "visitor_name", nullable = false)
    private String visitorName;

    @NotNull
    @Column(name = "visitor_email", nullable = false)
    private String visitorEmail;

    @Lob
    @Column(name = "message", nullable = false)
    private String message;

    @NotNull
    @Column(name = "submitted_date", nullable = false)
    private Instant submittedDate;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ContactMessage id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVisitorName() {
        return this.visitorName;
    }

    public ContactMessage visitorName(String visitorName) {
        this.setVisitorName(visitorName);
        return this;
    }

    public void setVisitorName(String visitorName) {
        this.visitorName = visitorName;
    }

    public String getVisitorEmail() {
        return this.visitorEmail;
    }

    public ContactMessage visitorEmail(String visitorEmail) {
        this.setVisitorEmail(visitorEmail);
        return this;
    }

    public void setVisitorEmail(String visitorEmail) {
        this.visitorEmail = visitorEmail;
    }

    public String getMessage() {
        return this.message;
    }

    public ContactMessage message(String message) {
        this.setMessage(message);
        return this;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Instant getSubmittedDate() {
        return this.submittedDate;
    }

    public ContactMessage submittedDate(Instant submittedDate) {
        this.setSubmittedDate(submittedDate);
        return this;
    }

    public void setSubmittedDate(Instant submittedDate) {
        this.submittedDate = submittedDate;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ContactMessage)) {
            return false;
        }
        return getId() != null && getId().equals(((ContactMessage) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ContactMessage{" +
            "id=" + getId() +
            ", visitorName='" + getVisitorName() + "'" +
            ", visitorEmail='" + getVisitorEmail() + "'" +
            ", message='" + getMessage() + "'" +
            ", submittedDate='" + getSubmittedDate() + "'" +
            "}";
    }
}
