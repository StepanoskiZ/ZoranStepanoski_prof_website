package com.paradox.zswebsite.repository;

import com.paradox.zswebsite.domain.AboutMe;
import java.util.Optional;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;

@Repository
public interface AboutMeRepository extends JpaRepository<AboutMe, Long> {

    // --- THIS IS THE NEW METHOD ---
    /**
     * Finds the first AboutMe record by ordering by ID and fetching its media collection eagerly.
     * This avoids lazy-loading issues.
     * @return an Optional containing the AboutMe entity with its media pre-loaded.
     */
    @Query("select aboutMe from AboutMe aboutMe left join fetch aboutMe.media")
    Optional<AboutMe> findFirstWithMediaEagerly();

    /**
     * Finds AboutMe entities and eagerly fetches their media collections.
     * The query is designed to be used with pagination to get the first result.
     * @param pageable the pagination information (e.g., page 0, size 1).
     * @return a List of AboutMe entities with their media pre-loaded.
     */
    @Query("select aboutMe from AboutMe aboutMe left join fetch aboutMe.media")
    List<AboutMe> findWithMediaEagerly(Pageable pageable);
}
