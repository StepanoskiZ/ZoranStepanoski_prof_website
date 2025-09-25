package com.paradox.zswebsite.repository;

import com.paradox.zswebsite.domain.PageContentMedia;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PageContentMedia entity.
 */
@Repository
public interface PageContentMediaRepository extends JpaRepository<PageContentMedia, Long> {
    default Optional<PageContentMedia> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<PageContentMedia> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<PageContentMedia> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select pageContentMedia from PageContentMedia pageContentMedia left join fetch pageContentMedia.pagecontent",
        countQuery = "select count(pageContentMedia) from PageContentMedia pageContentMedia"
    )
    Page<PageContentMedia> findAllWithToOneRelationships(Pageable pageable);

    @Query("select pageContentMedia from PageContentMedia pageContentMedia left join fetch pageContentMedia.pagecontent")
    List<PageContentMedia> findAllWithToOneRelationships();

    @Query(
        "select pageContentMedia from PageContentMedia pageContentMedia left join fetch pageContentMedia.pagecontent where pageContentMedia.id =:id"
    )
    Optional<PageContentMedia> findOneWithToOneRelationships(@Param("id") Long id);
}
