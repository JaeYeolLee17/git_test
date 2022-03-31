package com.e4motion.challenge.api.repository.impl;

import com.e4motion.challenge.api.domain.Road;
import com.e4motion.challenge.api.repository.RoadRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface JpaRoadRepository extends JpaRepository<Road, String>, RoadRepository {

    @Transactional(readOnly = true)
    @EntityGraph(attributePaths = "authorities")
    Optional<Road> findByRoadId(String roadId);

    @Transactional
    @EntityGraph(attributePaths = "authorities")
    void deleteByRoadId(String roadId);
}
