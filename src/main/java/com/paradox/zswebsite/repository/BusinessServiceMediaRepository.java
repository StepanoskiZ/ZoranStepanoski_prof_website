package com.paradox.zswebsite.repository;

import com.paradox.zswebsite.domain.BusinessServiceMedia;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the BusinessServiceMedia entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BusinessServiceMediaRepository extends JpaRepository<BusinessServiceMedia, Long> {}
