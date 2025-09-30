package com.paradox.zswebsite.repository;

import com.paradox.zswebsite.domain.AboutMe;
import java.util.Optional; // <-- Add this import
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param; // <-- Add this import
import org.springframework.stereotype.Repository;

@Repository
public interface AboutMeRepository extends JpaRepository<AboutMe, Long> {

    /**
     * Finds an AboutMe entity by its ID and eagerly fetches its associated media collection.
     *
     * @param id the ID of the AboutMe entity.
     * @return an Optional containing the AboutMe entity with its media pre-loaded.
     */
    @Query("select aboutMe from AboutMe aboutMe left join fetch aboutMe.media where aboutMe.id = :id")
    Optional<AboutMe> findOneWithEagerRelationships(@Param("id") Long id);
}
