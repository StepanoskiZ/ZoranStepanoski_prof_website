package com.paradox.zswebsite.config;

import static org.springframework.security.config.Customizer.withDefaults;

import com.paradox.zswebsite.security.AuthoritiesConstants;
import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.oauth2.server.resource.web.access.BearerTokenAccessDeniedHandler;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;
import tech.jhipster.config.JHipsterProperties;

@Configuration
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfiguration {

    private static final Logger log = LoggerFactory.getLogger(SecurityConfiguration.class);
    private final JHipsterProperties jHipsterProperties;

    public SecurityConfiguration(JHipsterProperties jHipsterProperties) {
        this.jHipsterProperties = jHipsterProperties;
        log.info("SECURITY CONFIG LOADED! Public /api/contact-messages enabled.");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    MvcRequestMatcher.Builder mvc(HandlerMappingIntrospector introspector) {
        return new MvcRequestMatcher.Builder(introspector);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, MvcRequestMatcher.Builder mvc) throws Exception {
        http
            .cors(withDefaults())
            // CSRF is disabled for stateless APIs
            .csrf(AbstractHttpConfigurer::disable)
            .headers(headers ->
                headers
                    .contentSecurityPolicy(csp -> csp.policyDirectives(jHipsterProperties.getSecurity().getContentSecurityPolicy()))
                    .frameOptions(FrameOptionsConfig::sameOrigin)
                    .referrerPolicy(referrer -> referrer.policy(ReferrerPolicyHeaderWriter.ReferrerPolicy.STRICT_ORIGIN_WHEN_CROSS_ORIGIN))
                    .permissionsPolicyHeader(permissions ->
                        permissions.policy(
                            "camera=(), fullscreen=(self), geolocation=(), gyroscope=(), magnetometer=(), microphone=(), midi=(), payment=(), sync-xhr()"
                        )
                    )
            )
            // Authorization rules using the robust MvcRequestMatcher
            .authorizeHttpRequests(authz ->
                authz
                    // Public POST endpoints
                    .requestMatchers(mvc.pattern(HttpMethod.POST, "/api/contact-messages"))
                    .permitAll()
                    // Public GET endpoints
                    .requestMatchers(mvc.pattern(HttpMethod.GET, "/api/blog-posts"))
                    .permitAll()
                    .requestMatchers(mvc.pattern(HttpMethod.GET, "/api/blog-posts/**"))
                    .permitAll()
                    .requestMatchers(mvc.pattern(HttpMethod.GET, "/api/skills"))
                    .permitAll()
                    .requestMatchers(mvc.pattern(HttpMethod.GET, "/api/projects"))
                    .permitAll()
                    .requestMatchers(mvc.pattern(HttpMethod.GET, "/api/project-images"))
                    .permitAll()
                    .requestMatchers(mvc.pattern(HttpMethod.GET, "/api/services"))
                    .permitAll()
                    // Public endpoints for authentication, registration, etc.
                    .requestMatchers(mvc.pattern("/api/authenticate"))
                    .permitAll()
                    .requestMatchers(mvc.pattern("/api/register"))
                    .permitAll()
                    .requestMatchers(mvc.pattern("/api/account/reset-password/init"))
                    .permitAll()
                    .requestMatchers(mvc.pattern("/api/account/reset-password/finish"))
                    .permitAll()
                    // Public actuator endpoints for health checks
                    .requestMatchers(mvc.pattern(HttpMethod.GET, "/management/health"))
                    .permitAll()
                    .requestMatchers(mvc.pattern(HttpMethod.GET, "/management/health/**"))
                    .permitAll()
                    .requestMatchers(mvc.pattern(HttpMethod.GET, "/management/info"))
                    .permitAll()
                    // Admin-only endpoints
                    .requestMatchers(mvc.pattern("/api/admin/**"))
                    .hasAuthority(AuthoritiesConstants.ADMIN)
                    .requestMatchers(mvc.pattern("/management/**"))
                    .hasAuthority(AuthoritiesConstants.ADMIN)
                    // All other API endpoints require authentication
                    .requestMatchers(mvc.pattern("/api/**"))
                    .authenticated()
            )
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .exceptionHandling(exceptions ->
                exceptions
                    .authenticationEntryPoint(new BearerTokenAuthenticationEntryPoint())
                    .accessDeniedHandler(new BearerTokenAccessDeniedHandler())
            )
            .oauth2ResourceServer(oauth2 -> oauth2.jwt(withDefaults()));

        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(
            Arrays.asList(
                //                "http://localhost:9000",
                //                "http://localhost:8080",
                //                "https://zoranstepanoski-prof-website.fly.dev",
                //                "http://zoranstepanoski-prof-website.fly.dev"
                "http://localhost:9000",
                "http://localhost:8080",
                "https://*.fly.dev"
            )
        );
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "X-XSRF-TOKEN"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
