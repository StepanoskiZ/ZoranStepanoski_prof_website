package com.paradox.zswebsite.repository;

import com.paradox.zswebsite.domain.BusinessService;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

@Repository
public interface BusinessServiceRepository extends JpaRepository<BusinessService, Long> {

    @Query("select distinct bs from BusinessService bs left join fetch bs.media m order by m.id asc")
    List<BusinessService> findAllWithFirstMedia();

    @Query("select distinct bs from BusinessService bs left join fetch bs.media m order by m.id asc")
    List<BusinessService> findAllWithFirstMediaOrderByMediaId();

}
