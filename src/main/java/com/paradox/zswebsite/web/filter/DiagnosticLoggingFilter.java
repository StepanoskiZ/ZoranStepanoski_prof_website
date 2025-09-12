package com.paradox.zswebsite.web.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class DiagnosticLoggingFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(DiagnosticLoggingFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        // This will log EVERY request that successfully reaches the Java application.
        log.info("DIAGNOSTIC FILTER: Received {} request for URI: {}", httpRequest.getMethod(), httpRequest.getRequestURI());

        // Pass the request along to the rest of the filter chain.
        chain.doFilter(request, response);
    }
}
