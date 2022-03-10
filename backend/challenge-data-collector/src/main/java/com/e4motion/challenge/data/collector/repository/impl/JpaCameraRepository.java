package com.e4motion.challenge.data.collector.repository.impl;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.e4motion.challenge.data.collector.domain.Camera;
import com.e4motion.challenge.data.collector.repository.CameraRepository;
import org.springframework.transaction.annotation.Transactional;

public interface JpaCameraRepository extends JpaRepository<Camera, String>, CameraRepository {

	@Transactional(readOnly = true)
	Optional<Camera> findByCameraId(String cameraId);
	
}
