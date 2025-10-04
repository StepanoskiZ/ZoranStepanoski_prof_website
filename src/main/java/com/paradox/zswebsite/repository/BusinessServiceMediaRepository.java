package com.paradox.zswebsite.repository;

import com.paradox.zswebsite.domain.BusinessServiceMedia;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the BusinessServiceMedia entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BusinessServiceMediaRepository extends JpaRepository<BusinessServiceMedia, Long> {
    @Query(
        value = "select bsm from BusinessServiceMedia bsm left join fetch bsm.businessService",
        countQuery = "select count(bsm) from BusinessServiceMedia bsm"
    )
    Page<BusinessServiceMedia> findAllWithEagerRelationships(Pageable pageable);
}
