package com.paradox.zswebsite.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.paradox.zswebsite.domain.VisitorLog} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class VisitorLogDTO implements Serializable {

    private Long id;

    private String ipAddress;

    private String pageVisited;

    private String userAgent;

    @NotNull
    private Instant visitTimestamp;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getPageVisited() {
        return pageVisited;
    }

    public void setPageVisited(String pageVisited) {
        this.pageVisited = pageVisited;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public Instant getVisitTimestamp() {
        return visitTimestamp;
    }

    public void setVisitTimestamp(Instant visitTimestamp) {
        this.visitTimestamp = visitTimestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VisitorLogDTO)) {
            return false;
        }

        VisitorLogDTO visitorLogDTO = (VisitorLogDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, visitorLogDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VisitorLogDTO{" +
            "id=" + getId() +
            ", ipAddress='" + getIpAddress() + "'" +
            ", pageVisited='" + getPageVisited() + "'" +
            ", userAgent='" + getUserAgent() + "'" +
            ", visitTimestamp='" + getVisitTimestamp() + "'" +
            "}";
    }
}
