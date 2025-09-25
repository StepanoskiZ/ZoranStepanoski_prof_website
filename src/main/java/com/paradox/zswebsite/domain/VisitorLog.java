package com.paradox.zswebsite.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A VisitorLog.
 */
@Entity
@Table(name = "visitor_log")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class VisitorLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "ip_address")
    private String ipAddress;

    @Column(name = "page_visited")
    private String pageVisited;

    @Column(name = "user_agent")
    private String userAgent;

    @NotNull
    @Column(name = "visit_timestamp", nullable = false)
    private Instant visitTimestamp;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public VisitorLog id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIpAddress() {
        return this.ipAddress;
    }

    public VisitorLog ipAddress(String ipAddress) {
        this.setIpAddress(ipAddress);
        return this;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getPageVisited() {
        return this.pageVisited;
    }

    public VisitorLog pageVisited(String pageVisited) {
        this.setPageVisited(pageVisited);
        return this;
    }

    public void setPageVisited(String pageVisited) {
        this.pageVisited = pageVisited;
    }

    public String getUserAgent() {
        return this.userAgent;
    }

    public VisitorLog userAgent(String userAgent) {
        this.setUserAgent(userAgent);
        return this;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public Instant getVisitTimestamp() {
        return this.visitTimestamp;
    }

    public VisitorLog visitTimestamp(Instant visitTimestamp) {
        this.setVisitTimestamp(visitTimestamp);
        return this;
    }

    public void setVisitTimestamp(Instant visitTimestamp) {
        this.visitTimestamp = visitTimestamp;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VisitorLog)) {
            return false;
        }
        return getId() != null && getId().equals(((VisitorLog) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VisitorLog{" +
            "id=" + getId() +
            ", ipAddress='" + getIpAddress() + "'" +
            ", pageVisited='" + getPageVisited() + "'" +
            ", userAgent='" + getUserAgent() + "'" +
            ", visitTimestamp='" + getVisitTimestamp() + "'" +
            "}";
    }
}
