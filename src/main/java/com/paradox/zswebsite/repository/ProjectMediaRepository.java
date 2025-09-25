package com.paradox.zswebsite.repository;

import com.paradox.zswebsite.domain.ProjectMedia;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ProjectMedia entity.
 */
@Repository
public interface ProjectMediaRepository extends JpaRepository<ProjectMedia, Long> {
    default Optional<ProjectMedia> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<ProjectMedia> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<ProjectMedia> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select projectMedia from ProjectMedia projectMedia left join fetch projectMedia.project",
        countQuery = "select count(projectMedia) from ProjectMedia projectMedia"
    )
    Page<ProjectMedia> findAllWithToOneRelationships(Pageable pageable);

    @Query("select projectMedia from ProjectMedia projectMedia left join fetch projectMedia.project")
    List<ProjectMedia> findAllWithToOneRelationships();

    @Query("select projectMedia from ProjectMedia projectMedia left join fetch projectMedia.project where projectMedia.id =:id")
    Optional<ProjectMedia> findOneWithToOneRelationships(@Param("id") Long id);
}
