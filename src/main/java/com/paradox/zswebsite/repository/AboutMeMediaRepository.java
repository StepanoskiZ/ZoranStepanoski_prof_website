package com.paradox.zswebsite.repository;

import com.paradox.zswebsite.domain.AboutMeMedia;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AboutMeMedia entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AboutMeMediaRepository extends JpaRepository<AboutMeMedia, Long> {}
