package com.e4motion.challenge.api.repository;

import com.e4motion.challenge.api.domain.Region;

import java.util.List;
import java.util.Optional;

public interface RegionRepository {

    Region save(Region link);

    List<Region> findAll();

    void deleteAll();

    long count();

    Optional<Region> findByRegionId(String regionId);

    void deleteByRegionId(String regionId);
}
