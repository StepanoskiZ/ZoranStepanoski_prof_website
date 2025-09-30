package com.paradox.zswebsite.repository;

import com.paradox.zswebsite.domain.AboutMe;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AboutMeRepository extends JpaRepository<AboutMe, Long> {

    // Method for the modal (Correct)
    @Query("select aboutMe from AboutMe aboutMe left join fetch aboutMe.media where aboutMe.id = :id")
    Optional<AboutMe> findOneWithEagerRelationships(@Param("id") Long id);

    // --- ADD THIS METHOD (to mirror ProjectRepository) ---
    @Query("select distinct aboutMe from AboutMe aboutMe left join fetch aboutMe.media")
    List<AboutMe> findAllWithEagerRelationships();
}
