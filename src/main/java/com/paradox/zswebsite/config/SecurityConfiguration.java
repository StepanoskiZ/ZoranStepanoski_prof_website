//package com.paradox.zswebsite.config;
//
//import static org.springframework.security.config.Customizer.withDefaults;
//
//import com.paradox.zswebsite.security.*;
////import com.paradox.zswebsite.web.filter.ZSWebSiteFilter;
////import org.slf4j.Logger; // <-- ADD THIS IMPORT
////import org.slf4j.LoggerFactory; // <-- ADD THIS IMPORT
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
//import org.springframework.security.oauth2.server.resource.web.access.BearerTokenAccessDeniedHandler;
//import org.springframework.security.web.SecurityFilterChain;
////import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
//import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;
//import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
//import org.springframework.web.cors.CorsConfiguration;
//import org.springframework.web.cors.CorsConfigurationSource;
//import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
////import org.springframework.web.servlet.handler.HandlerMappingIntrospector;
//import tech.jhipster.config.JHipsterProperties;
//
//@Configuration
//@EnableMethodSecurity(securedEnabled = true)
//public class SecurityConfiguration {
//
//    //    private static final Logger log = LoggerFactory.getLogger(SecurityConfiguration.class);
//
//    private final JHipsterProperties jHipsterProperties;
//
//    public SecurityConfiguration(JHipsterProperties jHipsterProperties) {
//        this.jHipsterProperties = jHipsterProperties;
//        //        log.info("SECURITY CONFIGURATION LOADED! CSRF IS DISABLED. Version 3.");
//    }
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http, MvcRequestMatcher.Builder mvc) throws Exception {
//        http
//            // --- CORS + CSRF ---
//            .cors(withDefaults())
//            .csrf(csrf -> csrf.disable())
//
//            // --- HEADERS ---
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
//
//            // --- AUTHORIZATION ---
//            .authorizeHttpRequests(authz -> authz
//                // 1. PUBLIC STATIC ASSETS (MvcRequestMatcher is fine here)
//                .requestMatchers(mvc.pattern("/")).permitAll()
//                .requestMatchers(mvc.pattern("/index.html"), mvc.pattern("/*.js"), mvc.pattern("/*.txt"), mvc.pattern("/*.json"),
//                    mvc.pattern("/*.map"), mvc.pattern("/*.css")).permitAll()
//                .requestMatchers(mvc.pattern("/*.ico"), mvc.pattern("/*.png"), mvc.pattern("/*.svg"), mvc.pattern("/*.webapp")).permitAll()
//                .requestMatchers(mvc.pattern("/app/**"), mvc.pattern("/i18n/**"), mvc.pattern("/content/**"), mvc.pattern("/webfonts/**"),
//                    mvc.pattern("/assets/**")).permitAll()
//                .requestMatchers(mvc.pattern("/*.woff2"), mvc.pattern("/*.woff"), mvc.pattern("/*.ttf"), mvc.pattern("/*.eot")).permitAll()
//                .requestMatchers(mvc.pattern("/swagger-ui/**"), mvc.pattern("/v3/api-docs/**")).permitAll()
//
//                // 2. PUBLIC API ENDPOINTS (use direct requestMatchers with HttpMethod)
//                .requestMatchers(HttpMethod.POST, "/api/authenticate").permitAll()
//                .requestMatchers(HttpMethod.GET, "/api/authenticate").permitAll()
//                .requestMatchers(HttpMethod.POST, "/api/register").permitAll()
//                .requestMatchers("/api/activate").permitAll()
//                .requestMatchers(HttpMethod.POST, "/api/account/reset-password/init").permitAll()
//                .requestMatchers(HttpMethod.POST, "/api/account/reset-password/finish").permitAll()
//                .requestMatchers(HttpMethod.POST, "/api/contact-messages").permitAll()
//                .requestMatchers(HttpMethod.GET, "/api/blog-posts", "/api/blog-posts/**").permitAll()
//                .requestMatchers(HttpMethod.GET, "/api/skills", "/api/projects", "/api/project-images", "/api/services").permitAll()
//                .requestMatchers("/management/health", "/management/health/**", "/management/info", "/management/prometheus").permitAll()
//
//                // 3. ADMIN-ONLY ENDPOINTS
//                .requestMatchers(mvc.pattern("/api/admin/**")).hasAuthority(AuthoritiesConstants.ADMIN)
//                .requestMatchers(mvc.pattern("/management/**")).hasAuthority(AuthoritiesConstants.ADMIN)
//
//                // 4. CATCH-ALL FOR AUTHENTICATED USERS
//                .requestMatchers(mvc.pattern("/api/**")).authenticated()
//            )
//
//            // --- STATELESS SESSION + JWT ---
//            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//            .exceptionHandling(exceptions ->
//                exceptions
//                    .authenticationEntryPoint(new BearerTokenAuthenticationEntryPoint())
//                    .accessDeniedHandler(new BearerTokenAccessDeniedHandler())
//            )
//            .oauth2ResourceServer(oauth2 -> oauth2.jwt(withDefaults()));
//
//        return http.build();
//    }
//
//    /**
//     * Global CORS configuration - allows Angular (localhost) & production frontend
//     */
//    @Bean
//    public CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration config = new CorsConfiguration();
//        // Allow both dev and prod frontends (adjust to your domains)
//        config.addAllowedOrigin("http://localhost:4200");
//        config.addAllowedOrigin("https://zoranstepanoski-prof-website.fly.dev");
//        config.addAllowedHeader("*");
//        config.addAllowedMethod("*");
//        config.setAllowCredentials(true);
//
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", config);
//        return source;
//    }
//    //    @Bean
//    //    MvcRequestMatcher.Builder mvc(HandlerMappingIntrospector introspector) {
//    //        return new MvcRequestMatcher.Builder(introspector);
//    //    }
//}
package com.paradox.zswebsite.config;

import static org.springframework.security.config.Customizer.withDefaults;

import com.paradox.zswebsite.security.AuthoritiesConstants;
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
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;
import tech.jhipster.config.JHipsterProperties;

@Configuration
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfiguration {

    private static final Logger log = LoggerFactory.getLogger(SecurityConfiguration.class);

    private final JHipsterProperties jHipsterProperties;

    public SecurityConfiguration(JHipsterProperties jHipsterProperties) {
        this.jHipsterProperties = jHipsterProperties;
        // This log message is our proof that the latest code is running.
        log.info("SECURITY CONFIGURATION LOADED! Using explicit CSRF ignore list. Version 4.");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, MvcRequestMatcher.Builder mvc) throws Exception {
        http
            .cors(withDefaults())
            // 1. THE DEFINITIVE CSRF FIX:
            // Instead of disabling CSRF globally (which was being ignored), we keep it enabled
            // and explicitly tell it which state-changing public endpoints to ignore.
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
            // 2. CONSISTENT AND CORRECT MATCHERS:
            // All rules now consistently use the mvc.pattern() builder for reliable matching.
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

    // 3. CRITICAL BEAN RESTORED:
    // This bean is required for the mvc.pattern() builder to work. It must not be commented out.
    @Bean
    MvcRequestMatcher.Builder mvc(HandlerMappingIntrospector introspector) {
        return new MvcRequestMatcher.Builder(introspector);
    }
}
