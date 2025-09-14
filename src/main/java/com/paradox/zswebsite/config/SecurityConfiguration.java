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
        log.info("SECURITY CONFIG LOADED! Using a single, unified SecurityFilterChain with CSRF exceptions. Version 17.");
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
            .csrf(csrf ->
                csrf
                    .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                    // Tell CSRF filter to IGNORE these specific public POST/PUT/DELETE requests
                    .ignoringRequestMatchers(
                        mvc.pattern(HttpMethod.POST, "/api/authenticate"),
                        mvc.pattern(HttpMethod.POST, "/api/register"),
                        mvc.pattern(HttpMethod.POST, "/api/contact-messages") // <-- THE FIX
                    )
                    .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler())
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
                    // --- START OF PUBLIC ENDPOINTS ---
                    .requestMatchers(mvc.pattern(HttpMethod.POST, "/api/authenticate"))
                    .permitAll()
                    .requestMatchers(mvc.pattern(HttpMethod.POST, "/api/register"))
                    .permitAll()
                    .requestMatchers(mvc.pattern(HttpMethod.POST, "/api/contact-messages"))
                    .permitAll()
                    .requestMatchers(mvc.pattern(HttpMethod.POST, "/api/account/reset-password/init"))
                    .permitAll()
                    .requestMatchers(mvc.pattern(HttpMethod.POST, "/api/account/reset-password/finish"))
                    .permitAll()
                    // THIS IS THE FIX: Allow anyone to send a contact message
                    .requestMatchers(mvc.pattern(HttpMethod.POST, "/api/contact-messages"))
                    .permitAll()
                    // Publicly readable data
                    .requestMatchers(mvc.pattern(HttpMethod.GET, "/api/blog-posts"), mvc.pattern(HttpMethod.GET, "/api/blog-posts/**"))
                    .permitAll()
                    .requestMatchers(mvc.pattern(HttpMethod.GET, "/api/skills"))
                    .permitAll()
                    .requestMatchers(mvc.pattern(HttpMethod.GET, "/api/projects"))
                    .permitAll()
                    .requestMatchers(mvc.pattern(HttpMethod.GET, "/api/project-images"))
                    .permitAll()
                    .requestMatchers(mvc.pattern(HttpMethod.GET, "/api/services"))
                    .permitAll()
                    // Public frontend assets and pages
                    .requestMatchers(
                        mvc.pattern("/"),
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
                    .requestMatchers(
                        mvc.pattern("/app/**"),
                        mvc.pattern("/i18n/**"),
                        mvc.pattern("/content/**"),
                        mvc.pattern("/webfonts/**"),
                        mvc.pattern("/assets/**")
                    )
                    .permitAll()
                    .requestMatchers(mvc.pattern("/*.woff2"), mvc.pattern("/*.woff"), mvc.pattern("/*.ttf"), mvc.pattern("/*.eot"))
                    .permitAll()
                    .requestMatchers(mvc.pattern("/swagger-ui/**"), mvc.pattern("/v3/api-docs/**"))
                    .permitAll()
                    // Public management endpoints
                    .requestMatchers(
                        mvc.pattern("/management/health"),
                        mvc.pattern("/management/health/**"),
                        mvc.pattern("/management/info"),
                        mvc.pattern("/management/prometheus")
                    )
                    .permitAll()
                    // --- END OF PUBLIC ENDPOINTS ---

                    // --- START OF PROTECTED ENDPOINTS ---
                    // These are checked after the public ones.
                    .requestMatchers(mvc.pattern("/api/admin/**"))
                    .hasAuthority(AuthoritiesConstants.ADMIN)
                    .requestMatchers(mvc.pattern("/management/**"))
                    .hasAuthority(AuthoritiesConstants.ADMIN)
                    // The final catch-all rule: any other /api/** endpoint requires authentication.
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
