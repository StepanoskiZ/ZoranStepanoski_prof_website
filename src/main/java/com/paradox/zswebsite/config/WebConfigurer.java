package com.paradox.zswebsite.config;

import jakarta.servlet.ServletContext;
import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.server.WebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import tech.jhipster.config.JHipsterProperties;

/**
 * Configuration of web application with Servlet 3.0 APIs.
 */
@Configuration
public class WebConfigurer implements ServletContextInitializer, WebServerFactoryCustomizer<WebServerFactory> {

    private final Logger log = LoggerFactory.getLogger(WebConfigurer.class);

    private final Environment env;

    private final JHipsterProperties jHipsterProperties;

    public WebConfigurer(Environment env, JHipsterProperties jHipsterProperties) {
        this.env = env;
        this.jHipsterProperties = jHipsterProperties;
    }

    @Override
    public void onStartup(ServletContext servletContext) {
        if (env.getActiveProfiles().length != 0) {
            log.info("Web application configuration, using profiles: {}", (Object[]) env.getActiveProfiles());
        }

        log.info("Web application fully configured");
    }

    /**
     * Customize the Servlet engine: Mime types, the document root, the cache.
     */
    @Override
    public void customize(WebServerFactory server) {
        // When running in an IDE or with ./mvnw spring-boot:run, set location of the static web assets.
        setLocationForStaticAssets(server);
    }

    private void setLocationForStaticAssets(WebServerFactory server) {
        if (server instanceof ConfigurableServletWebServerFactory servletWebServer) {
            URL resource = getClass().getResource("/static/index.html");

            // Only set the document root if we are running from the file system (protocol is "file")
            if (resource != null && "file".equals(resource.getProtocol())) {
                try {
                    File root = new File(resource.toURI());
                    // The resource is index.html, so we need its parent directory 'static'
                    File staticFolder = new File(root.getParent());
                    servletWebServer.setDocumentRoot(staticFolder);
                } catch (URISyntaxException e) {
                    throw new RuntimeException("Unable to resolve static assets location", e);
                }
            }
            // If the protocol is "jar", we do nothing. Spring Boot will handle it correctly.
        }
    }
}
