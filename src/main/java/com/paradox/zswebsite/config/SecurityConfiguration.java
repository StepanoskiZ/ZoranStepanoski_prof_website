package com.paradox.zswebsite.config;

import static org.springframework.security.config.Customizer.withDefaults;

import com.paradox.zswebsite.security.AuthoritiesConstants;
import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
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
        log.info("SECURITY CONFIGURATION LOADED! Using explicit Java-based CORS and CSRF ignores. Version 5.");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, MvcRequestMatcher.Builder mvc) throws Exception {
        http
            .cors(withDefaults()) // This will now use the corsConfigurationSource() bean below
            .csrf(csrf ->
                csrf
                    .ignoringRequestMatchers(mvc.pattern("/api/authenticate"))
                    .ignoringRequestMatchers(mvc.pattern("/api/register"))
                    .ignoringRequestMatchers(mvc.pattern("/api/activate"))
                    .ignoringRequestMatchers(mvc.pattern("/api/account/reset-password/init"))
                    .ignoringRequestMatchers(mvc.pattern("/api/account/reset-password/finish"))
                    .ignoringRequestMatchers(mvc.pattern("/api/contact-messages"))
            )
            .headers(headers ->
                headers
                    .contentSecurityPolicy(csp -> csp.policyDirectives(jHipsterProperties.getSecurity().getContentSecurityPolicy()))
                    .frameOptions(FrameOptionsConfig::sameOrigin)
                    .referrerPolicy(referrer -> referrer.policy(ReferrerPolicyHeaderWriter.ReferrerPolicy.STRICT_ORIGIN_WHEN_CROSS_ORIGIN))
                    .permissionsPolicyHeader(permissions ->
                        permissions.policy(
                            "camera=(), fullscreen=(self), geolocation=(), gyroscope=(), magnetometer=(), microphone=(), midi=(), payment=(), sync-xhr=()"
                        )
                    )
            )
            .authorizeHttpRequests(authz ->
                authz
                    // Public static assets
                    .requestMatchers(mvc.pattern("/"))
                    .permitAll()
                    .requestMatchers(
                        mvc.pattern("/index.html"),
                        mvc.pattern("/*.js"),
                        mvc.pattern("/*.txt"),
                        mvc.pattern("/*.json"),
                        mvc.pattern("/*.map"),
                        mvc.pattern("/*.css")
                    )
                    .permitAll()
                    .requestMatchers(mvc.pattern("/*.ico"), mvc.pattern("/*.png"), mvc.pattern("/*.svg"), mvc.pattern("/*.webapp"))
                    .permitAll()
                    .requestMatchers(mvc.pattern("/app/**"))
                    .permitAll()
                    .requestMatchers(mvc.pattern("/i18n/**"))
                    .permitAll()
                    .requestMatchers(mvc.pattern("/content/**"))
                    .permitAll()
                    .requestMatchers(mvc.pattern("/webfonts/**"))
                    .permitAll()
                    .requestMatchers(mvc.pattern("/assets/**"))
                    .permitAll()
                    .requestMatchers(mvc.pattern("/*.woff2"), mvc.pattern("/*.woff"), mvc.pattern("/*.ttf"), mvc.pattern("/*.eot"))
                    .permitAll()
                    .requestMatchers(mvc.pattern("/swagger-ui/**"))
                    .permitAll()
                    .requestMatchers(mvc.pattern("/v3/api-docs/**"))
                    .permitAll()
                    // Public API endpoints
                    .requestMatchers(mvc.pattern(HttpMethod.POST, "/api/authenticate"))
                    .permitAll()
                    .requestMatchers(mvc.pattern(HttpMethod.GET, "/api/authenticate"))
                    .permitAll()
                    .requestMatchers(mvc.pattern(HttpMethod.POST, "/api/register"))
                    .permitAll()
                    .requestMatchers(mvc.pattern("/api/activate"))
                    .permitAll()
                    .requestMatchers(mvc.pattern(HttpMethod.POST, "/api/account/reset-password/init"))
                    .permitAll()
                    .requestMatchers(mvc.pattern(HttpMethod.POST, "/api/account/reset-password/finish"))
                    .permitAll()
                    .requestMatchers(mvc.pattern(HttpMethod.POST, "/api/contact-messages"))
                    .permitAll()
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
                    .requestMatchers(mvc.pattern("/management/health"))
                    .permitAll()
                    .requestMatchers(mvc.pattern("/management/health/**"))
                    .permitAll()
                    .requestMatchers(mvc.pattern("/management/info"))
                    .permitAll()
                    .requestMatchers(mvc.pattern("/management/prometheus"))
                    .permitAll()
                    // Admin-only endpoints
                    .requestMatchers(mvc.pattern("/api/admin/**"))
                    .hasAuthority(AuthoritiesConstants.ADMIN)
                    .requestMatchers(mvc.pattern("/management/**"))
                    .hasAuthority(AuthoritiesConstants.ADMIN)
                    // Catch-all for any other /api endpoint - must be authenticated
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
    MvcRequestMatcher.Builder mvc(HandlerMappingIntrospector introspector) {
        return new MvcRequestMatcher.Builder(introspector);
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // Set your allowed origins here.
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:9000", "https://zoranstepanoski-prof-website.fly.dev"));
        // Allow all standard methods.
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        // Allow all standard headers.
        configuration.setAllowedHeaders(Arrays.asList("authorization", "content-type", "x-auth-token"));
        // Expose headers so the frontend can read them.
        configuration.setExposedHeaders(Arrays.asList("x-auth-token"));
        // Allow credentials (cookies, authorization headers).
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", configuration);
        source.registerCorsConfiguration("/management/**", configuration);
        return source;
    }
}
