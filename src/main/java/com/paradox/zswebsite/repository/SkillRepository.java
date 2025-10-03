package com.paradox.zswebsite.repository;

import com.paradox.zswebsite.domain.Skill;
import java.util.List; // <-- Make sure this import is added
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Skill entity.
 */
@Repository
public interface SkillRepository extends JpaRepository<Skill, Long> {

    /**
     * Finds all Skill entities and sorts them by yearsOfExperience in descending order using an explicit JPQL query.
     * @return a sorted list of all Skill entities.
     */
    @Query("SELECT s FROM Skill s ORDER BY s.yearsOfExperience DESC")
    List<Skill> findAllSortedByExperience();

}
