package com.e4motion.challenge.data.collector.repository;

import java.util.Optional;

import com.e4motion.challenge.data.collector.domain.Camera;

public interface CameraRepository {
	
	Optional<Camera> findByCameraId(String cameraId);
	
}
