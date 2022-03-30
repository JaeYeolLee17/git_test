package com.e4motion.challenge.api.repository;

import com.e4motion.challenge.api.domain.Road;

import java.util.List;
import java.util.Optional;

public interface RoadRepository {

    Road save(Road road);

    List<Road> findAll();

    void deleteAll();

    long count();

    Optional<Road> findByRoadId(String roadId);

    void deleteByRoadId(String cameraId);
}
