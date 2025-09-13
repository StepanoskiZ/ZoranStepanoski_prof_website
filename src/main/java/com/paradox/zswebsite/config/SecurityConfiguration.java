package com.paradox.zswebsite.config;

import static org.springframework.security.config.Customizer.withDefaults;

import com.paradox.zswebsite.security.AuthoritiesConstants;
import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
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
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
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
        log.info("SECURITY CONFIG LOADED! Using robust MvcRequestMatcher for multi-chain routing. Version 12.");
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

    // =================================================================================
    // CHAIN 1: STATELESS PUBLIC AUTH API - HIGHEST PRIORITY
    // =================================================================================
    @Bean
    @Order(1)
    public SecurityFilterChain statelessPublicApiFilterChain(HttpSecurity http) throws Exception {
        http
            .securityMatcher(
                new OrRequestMatcher(
                    new AntPathRequestMatcher("/api/authenticate"),
                    new AntPathRequestMatcher("/api/register"),
                    new AntPathRequestMatcher("/api/activate"),
                    new AntPathRequestMatcher("/api/account/reset-password/init"),
                    new AntPathRequestMatcher("/api/account/reset-password/finish")
                )
            )
            .cors(withDefaults())
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(authz -> authz.anyRequest().permitAll())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .exceptionHandling(exceptions ->
                exceptions
                    .authenticationEntryPoint(new BearerTokenAuthenticationEntryPoint())
                    .accessDeniedHandler(new BearerTokenAccessDeniedHandler())
            );
        return http.build();
    }

    // =================================================================================
    // CHAIN 2: DEFAULT APPLICATION SECURITY - LOWER PRIORITY
    // =================================================================================
    @Bean
    @Order(2)
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http, MvcRequestMatcher.Builder mvc) throws Exception {
        http
            .cors(withDefaults())
            .csrf(AbstractHttpConfigurer::disable)
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
                    .requestMatchers(mvc.pattern(HttpMethod.POST, "/api/contact-messages"))
                    .permitAll()
                    .requestMatchers(mvc.pattern(HttpMethod.GET, "/api/blog-posts"), mvc.pattern(HttpMethod.GET, "/api/blog-posts/**"))
                    .permitAll()
                    .requestMatchers(
                        mvc.pattern(HttpMethod.GET, "/api/skills"),
                        mvc.pattern(HttpMethod.GET, "/api/projects"),
                        mvc.pattern(HttpMethod.GET, "/api/project-images"),
                        mvc.pattern(HttpMethod.GET, "/api/services")
                    )
                    .permitAll()
                    .requestMatchers(
                        mvc.pattern("/management/health"),
                        mvc.pattern("/management/health/**"),
                        mvc.pattern("/management/info"),
                        mvc.pattern("/management/prometheus")
                    )
                    .permitAll()
                    .requestMatchers(mvc.pattern("/api/admin/**"))
                    .hasAuthority(AuthoritiesConstants.ADMIN)
                    .requestMatchers(mvc.pattern("/management/**"))
                    .hasAuthority(AuthoritiesConstants.ADMIN)
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
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:9000", "https://zoranstepanoski-prof-website.fly.dev"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
