package com.e4motion.challenge.data.collector.repository;

import com.e4motion.challenge.data.collector.domain.Camera;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface CameraRepository extends JpaRepository<Camera, String> {

	@Transactional(readOnly = true)
	Optional<Camera> findByCameraId(String cameraId);
	
}
