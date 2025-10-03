package com.paradox.zswebsite.repository;

import com.paradox.zswebsite.domain.AboutMeMedia;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AboutMeMedia entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AboutMeMediaRepository extends JpaRepository<AboutMeMedia, Long> {
    @Query(value = "select am from AboutMeMedia am left join fetch am.aboutMe",
        countQuery = "select count(am) from AboutMeMedia am")
    Page<AboutMeMedia> findAllWithEagerRelationships(Pageable pageable);
}
