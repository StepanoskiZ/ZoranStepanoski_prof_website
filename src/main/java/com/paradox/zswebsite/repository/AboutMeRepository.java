package com.paradox.zswebsite.repository;

import com.paradox.zswebsite.domain.AboutMe;
import java.util.Optional; // <-- Add this import
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

@Repository
public interface AboutMeRepository extends JpaRepository<AboutMe, Long> {

    // --- THIS IS THE NEW METHOD ---
    /**
     * Finds the first AboutMe record by ordering by ID and fetching its media collection eagerly.
     * This avoids lazy-loading issues.
     * @return an Optional containing the AboutMe entity with its media pre-loaded.
     */
    @Query("select aboutMe from AboutMe aboutMe left join fetch aboutMe.media order by aboutMe.id asc limit 1")
    Optional<AboutMe> findFirstWithMediaEagerly();
}
