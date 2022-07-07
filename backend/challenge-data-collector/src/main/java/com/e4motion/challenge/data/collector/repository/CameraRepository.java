package com.e4motion.challenge.data.collector.repository;

import com.e4motion.challenge.data.collector.domain.Camera;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface CameraRepository extends JpaRepository<Camera, Long> {

	@Transactional(readOnly = true)
	Optional<Camera> findByCameraNo(String cameraNo);
}
