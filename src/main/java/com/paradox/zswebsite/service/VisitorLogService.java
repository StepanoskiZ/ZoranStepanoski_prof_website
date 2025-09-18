package com.paradox.zswebsite.service;

import com.paradox.zswebsite.domain.VisitorLog;
import com.paradox.zswebsite.repository.VisitorLogRepository;
import jakarta.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class VisitorLogService {

    private final Logger log = LoggerFactory.getLogger(VisitorLogService.class);
    private final VisitorLogRepository visitorLogRepository;

    public VisitorLogService(VisitorLogRepository visitorLogRepository) {
        this.visitorLogRepository = visitorLogRepository;
    }

    /**
     * Logs a new visitor event.
     */
    public void logVisit(HttpServletRequest request) {
        VisitorLog visit = new VisitorLog();
        visit.setIpAddress(getClientIp(request));
        visit.setUserAgent(request.getHeader("User-Agent"));
        visit.setPageVisited(request.getRequestURI());
        visit.setVisitTimestamp(Instant.now());

        visitorLogRepository.save(visit);
        log.debug("Logged visit from IP: {}", visit.getIpAddress());
    }

    /**
     * Retrieves visitor statistics for the admin dashboard.
     */
    @Transactional(readOnly = true)
    public Map<String, Long> getVisitorStats() {
        Map<String, Long> stats = new HashMap<>();
        stats.put("totalVisits", visitorLogRepository.count());
        stats.put("uniqueVisitors", visitorLogRepository.countDistinctByIpAddress());
        return stats;
    }

    /**
     * Helper method to get the client's real IP address, even behind a proxy like Fly.io.
     */
    private String getClientIp(HttpServletRequest request) {
        String remoteAddr = "";
        if (request != null) {
            remoteAddr = request.getHeader("Fly-Client-IP"); // Fly.io specific header
            if (remoteAddr == null || remoteAddr.isEmpty()) {
                remoteAddr = request.getHeader("X-Forwarded-For");
            }
            if (remoteAddr == null || remoteAddr.isEmpty()) {
                remoteAddr = request.getRemoteAddr();
            }
        }
        return remoteAddr;
    }
}
