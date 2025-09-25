package com.paradox.zswebsite.web.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class DiagnosticLoggingFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(DiagnosticLoggingFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {
        // This enhanced version logs the method, URI, AND all headers.
        if (log.isInfoEnabled()) {
            StringBuilder headers = new StringBuilder();
            Collections.list(request.getHeaderNames()).forEach(headerName ->
                headers.append("  ").append(headerName).append(": ").append(request.getHeader(headerName)).append("\n")
            );

            log.info(
                "\n--- DIAGNOSTIC FILTER ---\n" +
                "Received {} request for URI: {}\n" +
                "From Remote Address: {}\n" +
                "--- HEADERS ---\n{}" +
                "------------------------",
                request.getMethod(),
                request.getRequestURI(),
                request.getRemoteAddr(),
                headers.toString()
            );
        }

        filterChain.doFilter(request, response);
    }
}
