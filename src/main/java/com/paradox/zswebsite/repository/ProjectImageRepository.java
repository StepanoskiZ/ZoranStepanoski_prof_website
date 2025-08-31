package com.paradox.zswebsite.repository;

import com.paradox.zswebsite.domain.ProjectImage;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ProjectImage entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProjectImageRepository extends JpaRepository<ProjectImage, Long> {}
