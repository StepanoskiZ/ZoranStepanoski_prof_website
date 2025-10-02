package com.paradox.zswebsite.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties specific to ZS Website.
 * <p>
 * Properties are configured in the {@code application.yml} file.
 * See {@link tech.jhipster.config.JHipsterProperties} for a good example.
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {

    private final Liquibase liquibase = new Liquibase();

    private final Ai ai = new Ai();

    // jhipster-needle-application-properties-property

    public Liquibase getLiquibase() {
        return liquibase;
    }

    public Ai getAi() { return ai; }
    // jhipster-needle-application-properties-property-getter

    public static class Liquibase {

        private Boolean asyncStart = true;

        public Boolean getAsyncStart() {
            return asyncStart;
        }

        public void setAsyncStart(Boolean asyncStart) {
            this.asyncStart = asyncStart;
        }
    }

    public static class Ai {
        private String googleApiKey;
        private String projectId;
        private String location;

        // Getters and Setters for Ai
        public String getGoogleApiKey() {
            return googleApiKey;
        }
        public void setGoogleApiKey(String googleApiKey) {
            this.googleApiKey = googleApiKey;
        }
        public String getProjectId() {
            return projectId;
        }
        public void setProjectId(String projectId) {
            this.projectId = projectId;
        }
        public String getLocation() {
            return location;
        }
        public void setLocation(String location) {
            this.location = location;
        }
    }

    // jhipster-needle-application-properties-property-class
}
