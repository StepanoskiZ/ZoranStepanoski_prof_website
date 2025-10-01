package com.paradox.zswebsite.repository;

import com.paradox.zswebsite.domain.BusinessService;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

@Repository
public interface BusinessServiceRepository extends JpaRepository<BusinessService, Long> {

    @Query("select distinct businessService from BusinessService businessService left join fetch businessService.media")
    List<BusinessService> findAllWithFirstMedia();

}
