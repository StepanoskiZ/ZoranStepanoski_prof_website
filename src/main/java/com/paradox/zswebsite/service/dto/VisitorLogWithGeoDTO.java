// Create a new file: src/main/java/com/paradox/zswebsite/service/dto/VisitorLogWithGeoDTO.java
package com.paradox.zswebsite.service.dto;

import java.time.Instant;

/**
 * A DTO for the {@link com.paradox.zswebsite.domain.VisitorLog} entity that includes geolocation data.
 */
public class VisitorLogWithGeoDTO extends VisitorLogDTO {

    private String city;
    private String country;
    private Double latitude;
    private Double longitude;

    // You can generate constructor, getters, and setters
    public VisitorLogWithGeoDTO() {
        // Default constructor
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
