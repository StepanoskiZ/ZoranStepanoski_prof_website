package com.paradox.zswebsite.repository;

import com.paradox.zswebsite.domain.AboutMe;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

@Repository
public interface AboutMeRepository extends JpaRepository<AboutMe, Long> {}
