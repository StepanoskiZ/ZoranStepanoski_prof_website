package com.paradox.zswebsite.repository;

import com.paradox.zswebsite.domain.PageContent;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PageContent entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PageContentRepository extends JpaRepository<PageContent, Long> {}
