package com.paradox.zswebsite.repository;

import com.paradox.zswebsite.domain.ContactMessage;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ContactMessage entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ContactMessageRepository extends JpaRepository<ContactMessage, Long> {}
