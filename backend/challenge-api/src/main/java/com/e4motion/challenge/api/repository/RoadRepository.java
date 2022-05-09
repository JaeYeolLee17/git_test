package com.e4motion.challenge.api.repository;

import com.e4motion.challenge.api.domain.Road;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface RoadRepository extends JpaRepository<Road, String> {

    @Transactional(readOnly = true)
    Optional<Road> findByRoadId(String roadId);

    @Transactional
    void deleteByRoadId(String roadId);

}
