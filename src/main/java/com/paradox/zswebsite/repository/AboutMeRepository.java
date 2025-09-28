package com.paradox.zswebsite.repository;

import com.paradox.zswebsite.domain.AboutMe;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AboutMe entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AboutMeRepository extends JpaRepository<AboutMe, Long> {}
