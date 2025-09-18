package com.paradox.zswebsite.web.rest;

import com.paradox.zswebsite.security.AuthoritiesConstants;
import com.paradox.zswebsite.service.VisitorLogService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class VisitorLogResource {

    private final Logger log = LoggerFactory.getLogger(VisitorLogResource.class);
    private final VisitorLogService visitorLogService;

    public VisitorLogResource(VisitorLogService visitorLogService) {
        this.visitorLogService = visitorLogService;
    }

    /**
     * POST /visitors/log : Logs a new visitor.
     * This endpoint is public.
     */
    @PostMapping("/visitors/log")
    public ResponseEntity<Void> logVisitor(HttpServletRequest request) {
        log.debug("REST request to log a new visitor");
        visitorLogService.logVisit(request);
        return ResponseEntity.ok().build();
    }

    /**
     * GET /admin/visitors/stats : Gets visitor statistics.
     * This endpoint is for admins only.
     */
    @GetMapping("/admin/visitors/stats")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Map<String, Long>> getVisitorStats() {
        log.debug("REST request to get visitor statistics");
        Map<String, Long> stats = visitorLogService.getVisitorStats();
        return ResponseEntity.ok(stats);
    }
}
