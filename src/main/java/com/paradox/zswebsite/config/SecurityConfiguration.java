package com.paradox.zswebsite.config;

import static org.springframework.security.config.Customizer.withDefaults;

import com.paradox.zswebsite.security.*;
import com.paradox.zswebsite.web.filter.SpaWebFilter;
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
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;
import tech.jhipster.config.JHipsterProperties;

@Configuration
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfiguration {

    private final JHipsterProperties jHipsterProperties;

    public SecurityConfiguration(JHipsterProperties jHipsterProperties) {
        this.jHipsterProperties = jHipsterProperties;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http, MvcRequestMatcher.Builder mvc) throws Exception {
//        http
//            .cors(withDefaults())
//            .csrf(csrf -> csrf.disable())
//            .addFilterAfter(new SpaWebFilter(), BasicAuthenticationFilter.class)
//            .headers(headers ->
//                headers
//                    .contentSecurityPolicy(csp -> csp.policyDirectives(jHipsterProperties.getSecurity().getContentSecurityPolicy()))
//                    .frameOptions(FrameOptionsConfig::sameOrigin)
//                    .referrerPolicy(referrer -> referrer.policy(ReferrerPolicyHeaderWriter.ReferrerPolicy.STRICT_ORIGIN_WHEN_CROSS_ORIGIN))
//                    .permissionsPolicyHeader(permissions ->
//                        permissions.policy(
//                            "camera=(), fullscreen=(self), geolocation=(), gyroscope=(), magnetometer=(), microphone=(), midi=(), payment=(), sync-xhr=()"
//                        )
//                    )
//            )
//            .authorizeHttpRequests(authz ->
//                // prettier-ignore
//                authz
//                    // 1. PUBLIC STATIC ASSETS
//                    .requestMatchers(mvc.pattern("/index.html"), mvc.pattern("/*.js"), mvc.pattern("/*.txt"), mvc.pattern("/*.json"), mvc.pattern("/*.map"), mvc.pattern("/*.css")).permitAll()
//                    .requestMatchers(mvc.pattern("/*.ico"), mvc.pattern("/*.png"), mvc.pattern("/*.svg"), mvc.pattern("/*.webapp")).permitAll()
//                    .requestMatchers(mvc.pattern("/app/**")).permitAll()
//                    .requestMatchers(mvc.pattern("/i18n/**")).permitAll()
//                    .requestMatchers(mvc.pattern("/content/**")).permitAll()
//                    .requestMatchers(mvc.pattern("/webfonts/**")).permitAll()
//                    .requestMatchers(mvc.pattern("/assets/**")).permitAll()
//                    .requestMatchers(mvc.pattern("/*.woff2"), mvc.pattern("/*.woff"), mvc.pattern("/*.ttf"), mvc.pattern("/*.eot")).permitAll()
//                    .requestMatchers(mvc.pattern("/swagger-ui/**")).permitAll()
//                    .requestMatchers(mvc.pattern("/v3/api-docs/**")).permitAll()
//
//                    // 2. PUBLIC API ENDPOINTS
//                    .requestMatchers(mvc.pattern(HttpMethod.POST, "/api/authenticate")).permitAll()
//                    .requestMatchers(mvc.pattern(HttpMethod.GET, "/api/authenticate")).permitAll()
//                    .requestMatchers(mvc.pattern(HttpMethod.POST, "/api/register")).permitAll()
//                    .requestMatchers(mvc.pattern("/api/activate")).permitAll()
//                    .requestMatchers(mvc.pattern(HttpMethod.POST, "/api/account/reset-password/init")).permitAll()
//                    .requestMatchers(mvc.pattern(HttpMethod.POST, "/api/account/reset-password/finish")).permitAll()
//                    .requestMatchers(mvc.pattern(HttpMethod.POST, "/api/contact-messages")).permitAll()
//                    .requestMatchers(mvc.pattern(HttpMethod.GET, "/api/blog-posts")).permitAll()
//                    .requestMatchers(mvc.pattern(HttpMethod.GET, "/api/blog-posts/**")).permitAll()
//                    .requestMatchers(mvc.pattern(HttpMethod.GET, "/api/skills")).permitAll()
//                    .requestMatchers(mvc.pattern(HttpMethod.GET, "/api/projects")).permitAll()
//                    .requestMatchers(mvc.pattern(HttpMethod.GET, "/api/project-images")).permitAll()
//                    .requestMatchers(mvc.pattern(HttpMethod.GET, "/api/services")).permitAll()
//                    .requestMatchers(mvc.pattern("/management/health")).permitAll()
//                    .requestMatchers(mvc.pattern("/management/health/**")).permitAll()
//                    .requestMatchers(mvc.pattern("/management/info")).permitAll()
//                    .requestMatchers(mvc.pattern("/management/prometheus")).permitAll()
//
//                    // 3. ADMIN-ONLY ENDPOINTS
//                    .requestMatchers(mvc.pattern("/api/admin/**")).hasAuthority(AuthoritiesConstants.ADMIN)
//                    .requestMatchers(mvc.pattern("/management/**")).hasAuthority(AuthoritiesConstants.ADMIN)
//
//                    // 4. CATCH-ALL FOR AUTHENTICATED USERS
//                    .requestMatchers(mvc.pattern("/api/**")).authenticated()
//            )
//            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//            .exceptionHandling(exceptions ->
//                exceptions
//                    .authenticationEntryPoint(new BearerTokenAuthenticationEntryPoint())
//                    .accessDeniedHandler(new BearerTokenAccessDeniedHandler())
//            )
//            .oauth2ResourceServer(oauth2 -> oauth2.jwt(withDefaults()));
//        return http.build();
//    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, MvcRequestMatcher.Builder mvc) throws Exception {
        http
            .cors(withDefaults())
            .csrf(csrf -> csrf.disable())
            .addFilterAfter(new SpaWebFilter(), BasicAuthenticationFilter.class)
            .headers(headers ->
                headers
                    .contentSecurityPolicy(csp -> csp.policyDirectives(jHipsterProperties.getSecurity().getContentSecurityPolicy()))
                    .frameOptions(FrameOptionsConfig::sameOrigin)
                    .referrerPolicy(referrer -> referrer.policy(ReferrerPolicyHeaderWriter.ReferrerPolicy.STRICT_ORIGIN_WHEN_CROSS_ORIGIN))
                    .permissionsPolicyHeader(permissions ->
                        permissions.policy("camera=(), fullscreen=(self), geolocation=(), gyroscope=(), magnetometer=(), microphone=(), midi=(), payment=(), sync-xhr=()")
                    )
            )
            .authorizeHttpRequests(authz ->
                authz
                    // static resources
                    .requestMatchers(mvc.pattern("/index.html"), mvc.pattern("/*.js"), mvc.pattern("/*.css"), mvc.pattern("/assets/**")).permitAll()

                    // --- Public API endpoints (no JWT required)
                    .requestMatchers(mvc.pattern(HttpMethod.POST, "/api/authenticate")).permitAll()
                    .requestMatchers(mvc.pattern(HttpMethod.POST, "/api/register")).permitAll()
                    .requestMatchers(mvc.pattern("/api/account/reset-password/**")).permitAll()
                    .requestMatchers(mvc.pattern(HttpMethod.POST, "/api/contact-messages")).permitAll()

                    // --- Public read-only APIs
                    .requestMatchers(mvc.pattern(HttpMethod.GET, "/api/blog-posts/**")).permitAll()
                    .requestMatchers(mvc.pattern(HttpMethod.GET, "/api/skills")).permitAll()
                    .requestMatchers(mvc.pattern(HttpMethod.GET, "/api/projects")).permitAll()

                    // --- Admin-only
                    .requestMatchers(mvc.pattern("/api/admin/**")).hasAuthority(AuthoritiesConstants.ADMIN)

                    // --- All other APIs require authentication
                    .requestMatchers(mvc.pattern("/api/**")).authenticated()

                    // management health/info is public, but other /management/** is admin
                    .requestMatchers(mvc.pattern("/management/health/**"), mvc.pattern("/management/info")).permitAll()
                    .requestMatchers(mvc.pattern("/management/**")).hasAuthority(AuthoritiesConstants.ADMIN)
            )
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .exceptionHandling(exceptions ->
                exceptions
                    .authenticationEntryPoint(new BearerTokenAuthenticationEntryPoint())
                    .accessDeniedHandler(new BearerTokenAccessDeniedHandler())
            )
            // 🟢 Only apply JWT to protected APIs
            .oauth2ResourceServer(oauth2 -> oauth2.jwt(withDefaults()));

        return http.build();
    }

    @Bean
    MvcRequestMatcher.Builder mvc(HandlerMappingIntrospector introspector) {
        return new MvcRequestMatcher.Builder(introspector);
    }
}
