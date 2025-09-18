package com.paradox.zswebsite.repository;

import com.paradox.zswebsite.domain.VisitorLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface VisitorLogRepository extends JpaRepository<VisitorLog, Long> {
    // Custom query to count distinct IP addresses
    @Query("SELECT COUNT(DISTINCT v.ipAddress) FROM VisitorLog v")
    //    @Query("SELECT COUNT(v.ipAddress) FROM VisitorLog v")
    long countDistinctByIpAddress();
}
