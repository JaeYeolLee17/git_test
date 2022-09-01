package com.e4motion.challenge.api.repository;

import com.e4motion.challenge.api.domain.CameraRoad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface CameraRoadRepository extends JpaRepository<CameraRoad, Long> {

    @Transactional(readOnly = true)
    Optional<CameraRoad> findByCamera_CameraId(Long cameraId);      // for unit tests.

}