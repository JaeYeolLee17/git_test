package com.e4motion.challenge.api.repository.impl;

import com.e4motion.challenge.api.domain.Camera;
import com.e4motion.challenge.api.repository.CameraRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface JpaCameraRepository extends JpaRepository<Camera, String>, CameraRepository {

    @Transactional(readOnly = true)
    Optional<Camera> findByCameraId(String cameraId);

    @Transactional
    void deleteByCameraId(String cameraId);
}
