package com.paradox.zswebsite.repository;

import com.paradox.zswebsite.domain.CurriculumVitae;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CurriculumVitaeRepository extends JpaRepository<CurriculumVitae, Long> {
    // Add this new method
    @Query("SELECT cv FROM CurriculumVitae cv ORDER BY cv.endDate DESC, cv.startDate DESC")
    List<CurriculumVitae> findAllOrderByEndDateDesc();

    @Query("SELECT DISTINCT cv FROM CurriculumVitae cv LEFT JOIN FETCH cv.media ORDER BY cv.endDate DESC, cv.startDate DESC")
    List<CurriculumVitae> findAllWithEagerRelationships();

    // You'll also want one for the detail view later
    @Query("SELECT cv FROM CurriculumVitae cv LEFT JOIN FETCH cv.media WHERE cv.id = :id")
    Optional<CurriculumVitae> findOneWithEagerRelationships(@Param("id") Long id);
}
