package com.paradox.zswebsite.repository;

import com.paradox.zswebsite.domain.AboutMe;
import java.util.Optional;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;

@Repository
public interface AboutMeRepository extends JpaRepository<AboutMe, Long> {}
