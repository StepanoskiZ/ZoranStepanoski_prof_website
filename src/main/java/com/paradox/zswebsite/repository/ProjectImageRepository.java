package com.paradox.zswebsite.repository;

import com.paradox.zswebsite.domain.ProjectImage;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ProjectImage entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProjectImageRepository extends JpaRepository<ProjectImage, Long> {
    Optional<ProjectImage> findFirstByProjectId(Long projectId);
    List<ProjectImage> findAllByProjectId(Long projectId);
}
