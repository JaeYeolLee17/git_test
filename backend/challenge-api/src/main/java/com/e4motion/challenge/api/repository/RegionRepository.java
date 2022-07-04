package com.e4motion.challenge.api.repository;

import com.e4motion.challenge.api.domain.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface RegionRepository extends JpaRepository<Region, Long> {

    @Transactional(readOnly = true)
    Optional<Region> findByRegionNo(String regionNo);

    @Transactional
    void deleteByRegionNo(String regionNo);

}
