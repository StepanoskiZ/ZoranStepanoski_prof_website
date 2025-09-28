package com.paradox.zswebsite.repository;

import com.paradox.zswebsite.domain.VisitorLog;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the VisitorLog entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VisitorLogRepository extends JpaRepository<VisitorLog, Long> {
    @Query("SELECT COUNT(DISTINCT v.ipAddress) FROM VisitorLog v")
    Long countDistinctByIpAddress();
}
