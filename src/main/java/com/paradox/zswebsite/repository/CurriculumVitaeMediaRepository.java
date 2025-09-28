package com.paradox.zswebsite.repository;

import com.paradox.zswebsite.domain.CurriculumVitaeMedia;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CurriculumVitaeMedia entity.
 */
@Repository
public interface CurriculumVitaeMediaRepository extends JpaRepository<CurriculumVitaeMedia, Long> {
    default Optional<CurriculumVitaeMedia> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<CurriculumVitaeMedia> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<CurriculumVitaeMedia> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select curriculumVitaeMedia from CurriculumVitaeMedia curriculumVitaeMedia left join fetch curriculumVitaeMedia.curriculumVitae",
        countQuery = "select count(curriculumVitaeMedia) from CurriculumVitaeMedia curriculumVitaeMedia"
    )
    Page<CurriculumVitaeMedia> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select curriculumVitaeMedia from CurriculumVitaeMedia curriculumVitaeMedia left join fetch curriculumVitaeMedia.curriculumVitae"
    )
    List<CurriculumVitaeMedia> findAllWithToOneRelationships();

    @Query(
        "select curriculumVitaeMedia from CurriculumVitaeMedia curriculumVitaeMedia left join fetch curriculumVitaeMedia.curriculumVitae where curriculumVitaeMedia.id =:id"
    )
    Optional<CurriculumVitaeMedia> findOneWithToOneRelationships(@Param("id") Long id);
}
