package com.e4motion.challenge.api.repository.impl;

import com.e4motion.challenge.api.domain.Region;
import com.e4motion.challenge.api.repository.RegionRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface JpaRegionRepository extends JpaRepository<Region, String>, RegionRepository {

    @Transactional(readOnly = true)
    Optional<Region> findByRegionId(String regionId);

    @Transactional
    void deleteByRegionId(String regionId);
}
