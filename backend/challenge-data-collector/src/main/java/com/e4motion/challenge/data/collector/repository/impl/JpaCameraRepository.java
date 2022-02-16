package com.e4motion.challenge.data.collector.repository.impl;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.e4motion.challenge.data.collector.domain.Camera;
import com.e4motion.challenge.data.collector.repository.CameraRepository;

public interface JpaCameraRepository extends JpaRepository<Camera, Long>, CameraRepository {
	
	public Optional<Camera> findByCameraId(String cameraId);
	
}
