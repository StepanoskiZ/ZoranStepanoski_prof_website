package com.paradox.zswebsite.repository;

import com.paradox.zswebsite.domain.BusinessService;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the BusinessService entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BusinessServiceRepository extends JpaRepository<BusinessService, Long> {}
