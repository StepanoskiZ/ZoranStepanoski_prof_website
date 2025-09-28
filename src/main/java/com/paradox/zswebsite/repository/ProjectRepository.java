package com.paradox.zswebsite.repository;

import com.paradox.zswebsite.domain.Project;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    @Query("select project from Project project left join fetch project.media where project.id =:id")
    Optional<Project> findOneWithEagerRelationships(@Param("id") Long id);

    @Query("select distinct project from Project project left join fetch project.media")
    List<Project> findAllWithEagerRelationships();
}
