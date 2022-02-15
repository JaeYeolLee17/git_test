package com.e4motion.challenge.data.collector.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.e4motion.challenge.data.collector.domain.Camera;

public interface CameraRepository extends JpaRepository<Camera, Long> {
	
	public Optional<Camera> findByCameraId(String cameraId);
	
}
