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
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.oauth2.server.resource.web.access.BearerTokenAccessDeniedHandler;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
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
        // Public endpoints patterns
        var publicPostEndpoints = Arrays.asList(mvc.pattern(HttpMethod.POST, "/api/contact-messages"));

        var publicGetEndpoints = Arrays.asList(
            mvc.pattern(HttpMethod.GET, "/api/blog-posts"),
            mvc.pattern(HttpMethod.GET, "/api/blog-posts/**"),
            mvc.pattern(HttpMethod.GET, "/api/skills"),
            mvc.pattern(HttpMethod.GET, "/api/projects"),
            mvc.pattern(HttpMethod.GET, "/api/project-images"),
            mvc.pattern(HttpMethod.GET, "/api/services")
        );

        var publicStaticAssets = Arrays.asList(
            mvc.pattern("/"),
            mvc.pattern("/index.html"),
            mvc.pattern("/*.js"),
            mvc.pattern("/*.css"),
            mvc.pattern("/swagger-ui/**"),
            mvc.pattern("/v3/api-docs/**")
        );

        http
            .cors(withDefaults())
            .csrf(csrf ->
                csrf
                    .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                    .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler())
                    //                    .ignoringRequestMatchers(publicPostEndpoints.toArray(new MvcRequestMatcher[0]))
                    .ignoringRequestMatchers(new AntPathRequestMatcher("/api/contact-messages", "POST"))
            )
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
            .authorizeHttpRequests(authz -> {
                publicPostEndpoints.forEach(ep -> authz.requestMatchers(ep).permitAll());
                publicGetEndpoints.forEach(ep -> authz.requestMatchers(ep).permitAll());
                publicStaticAssets.forEach(ep -> authz.requestMatchers(ep).permitAll());

                // ✅ Permit actuator health/info for Fly health checks
                authz.requestMatchers(mvc.pattern(HttpMethod.GET, "/management/health")).permitAll();
                authz.requestMatchers(mvc.pattern(HttpMethod.GET, "/management/health/**")).permitAll();
                authz.requestMatchers(mvc.pattern(HttpMethod.GET, "/management/info")).permitAll();

                authz
                    // Admin endpoints
                    .requestMatchers(mvc.pattern("/api/admin/**"))
                    .hasAuthority(AuthoritiesConstants.ADMIN)
                    .requestMatchers(mvc.pattern("/management/**"))
                    .hasAuthority(AuthoritiesConstants.ADMIN)
                    // All other /api/** endpoints require authentication
                    .requestMatchers(mvc.pattern("/api/**"))
                    .authenticated();
            })
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
                "http://localhost:9000",
                "http://localhost:8080",
                "https://zoranstepanoski-prof-website.fly.dev",
                "http://zoranstepanoski-prof-website.fly.dev"
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
