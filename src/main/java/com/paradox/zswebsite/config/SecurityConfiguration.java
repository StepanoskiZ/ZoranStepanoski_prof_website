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
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    // This bean is no longer strictly needed by the filter chain but is harmless to keep.
    @Bean
    MvcRequestMatcher.Builder mvc(HandlerMappingIntrospector introspector) {
        return new MvcRequestMatcher.Builder(introspector);
    }

    //    @Bean
    //    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception { // Removed MvcRequestMatcher.Builder from params
    //        http
    //            .cors(withDefaults())
    //            .csrf(AbstractHttpConfigurer::disable)
    //            .headers(headers ->
    //                headers
    //                    .contentSecurityPolicy(csp -> csp.policyDirectives(jHipsterProperties.getSecurity().getContentSecurityPolicy()))
    //                    .frameOptions(FrameOptionsConfig::sameOrigin)
    //                    .referrerPolicy(referrer -> referrer.policy(ReferrerPolicyHeaderWriter.ReferrerPolicy.STRICT_ORIGIN_WHEN_CROSS_ORIGIN))
    //                    .permissionsPolicyHeader(permissions ->
    //                        permissions.policy(
    //                            "camera=(), fullscreen=(self), geolocation=(), gyroscope=(), magnetometer=(), microphone=(), midi=(), payment=(), sync-xhr()"
    //                        )
    //                    )
    //            )
    //            .authorizeHttpRequests(authz ->
    //                authz
    //                    // THIS IS THE FIX: Reverted to simpler, direct Ant-style matchers
    //                    .requestMatchers(HttpMethod.OPTIONS, "/**")
    //                    .permitAll()
    //                    .requestMatchers(HttpMethod.POST, "/api/contact-messages")
    //                    .permitAll()
    //                    .requestMatchers(
    //                        HttpMethod.GET,
    //                        "/api/blog-posts",
    //                        "/api/blog-posts/**",
    //                        "/api/skills",
    //                        "/api/projects",
    //                        "/api/project-images",
    //                        "/api/services"
    //                    )
    //                    .permitAll()
    //                    .requestMatchers(
    //                        "/api/authenticate",
    //                        "/api/register",
    //                        "/api/account/reset-password/init",
    //                        "/api/account/reset-password/finish"
    //                    )
    //                    .permitAll()
    //                    .requestMatchers(HttpMethod.GET, "/management/health", "/management/health/**", "/management/info")
    //                    .permitAll()
    //                    .requestMatchers("/api/admin/**", "/management/**")
    //                    .hasAuthority(AuthoritiesConstants.ADMIN)
    //                    .requestMatchers("/api/**")
    //                    .authenticated()
    //            )
    //            //            .authorizeHttpRequests(authz -> authz.anyRequest().permitAll())
    //            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
    //            .exceptionHandling(exceptions ->
    //                exceptions
    //                    .authenticationEntryPoint(new BearerTokenAuthenticationEntryPoint())
    //                    .accessDeniedHandler(new BearerTokenAccessDeniedHandler())
    //            )
    //            .oauth2ResourceServer(oauth2 -> oauth2.jwt(withDefaults()));
    //
    //        return httpd();
    //    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors(withDefaults())
            // =================== THE MOST IMPORTANT CHANGE IS HERE ===================
            // Instead of a simple disable, we will explicitly tell the CSRF filter
            // to ignore our public paths. This is a more robust way to handle it.
            .csrf(csrf ->
                csrf.ignoringRequestMatchers(
                    "/api/contact-messages",
                    "/api/authenticate",
                    "/api/register",
                    "/api/account/reset-password/init",
                    "/api/account/reset-password/finish"
                )
            )
            // =======================================================================
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
            .authorizeHttpRequests(authz ->
                authz
                    .requestMatchers(HttpMethod.OPTIONS, "/**")
                    .permitAll()
                    .requestMatchers(HttpMethod.POST, "/api/contact-messages")
                    .permitAll()
                    .requestMatchers(
                        HttpMethod.GET,
                        "/api/blog-posts",
                        "/api/blog-posts/**",
                        "/api/skills",
                        "/api/projects",
                        "/api/project-images",
                        "/api/services"
                    )
                    .permitAll()
                    .requestMatchers(
                        "/api/authenticate",
                        "/api/register",
                        "/api/account/reset-password/init",
                        "/api/account/reset-password/finish"
                    )
                    .permitAll()
                    .requestMatchers(HttpMethod.GET, "/management/health", "/management/health/**", "/management/info")
                    .permitAll()
                    .requestMatchers("/api/admin/**", "/management/**")
                    .hasAuthority(AuthoritiesConstants.ADMIN)
                    .requestMatchers("/api/**")
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
        configuration.setAllowedOriginPatterns(
            Arrays.asList("http://localhost:9000", "http://localhost:8080", "https://*.fly.dev", "http://*.fly.dev")
        );
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "X-XSRF-TOKEN"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
