package com.paradox.zswebsite.repository;

import com.paradox.zswebsite.domain.BlogPost;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the BlogPost entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BlogPostRepository extends JpaRepository<BlogPost, Long> {}
