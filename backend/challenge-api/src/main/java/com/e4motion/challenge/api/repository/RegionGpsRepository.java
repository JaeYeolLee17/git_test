package com.e4motion.challenge.api.repository;

import com.e4motion.challenge.api.domain.RegionGps;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface RegionGpsRepository extends JpaRepository<RegionGps, Long> {

    @Transactional(readOnly = true)
    List<RegionGps> findAllByRegion_RegionNoOrderByGpsOrder(String regionNo);

}
