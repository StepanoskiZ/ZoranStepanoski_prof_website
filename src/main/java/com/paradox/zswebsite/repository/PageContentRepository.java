package com.paradox.zswebsite.repository;

import com.paradox.zswebsite.domain.PageContent;
import java.util.Optional; // Make sure this is imported
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PageContent entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PageContentRepository extends JpaRepository<PageContent, Long> {
    /**
     * Finds a PageContent entity by its unique sectionKey.
     * Spring Data JPA automatically creates the query from this method name.
     *
     * @param sectionKey the key to search for.
     * @return an Optional containing the found PageContent, or empty if not found.
     */
    Optional<PageContent> findBySectionKey(String sectionKey);
}
