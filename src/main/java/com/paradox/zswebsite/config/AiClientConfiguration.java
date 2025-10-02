package com.paradox.zswebsite.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import java.time.Duration;

/**
 * Configuration for creating specialized HTTP clients (RestTemplate).
 */
@Configuration
public class AiClientConfiguration {

    /**
     * Creates a RestTemplate bean specifically for long-running AI API calls.
     * It has a much longer read timeout than a standard RestTemplate.
     * @param builder The Spring Boot RestTemplateBuilder.
     * @return A configured RestTemplate instance.
     */
    @Bean("aiRestTemplate")
    public RestTemplate aiRestTemplate(RestTemplateBuilder builder) {
        return builder
            .setConnectTimeout(Duration.ofSeconds(20))   // Time to establish a connection
            .setReadTimeout(Duration.ofMinutes(3))      // 3-minute timeout to wait for the AI's response
            .build();
    }
}
