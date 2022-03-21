package com.e4motion.challenge.api.repository;

import java.util.List;
import java.util.Optional;

import com.e4motion.challenge.api.domain.Camera;

public interface CameraRepository {

    Camera save(Camera camera);

    List<Camera> findAll();

    void deleteAll();

    long count();

    Optional<Camera> findByCameraId(String cameraId);

    void deleteByCameraId(String cameraId);
}
