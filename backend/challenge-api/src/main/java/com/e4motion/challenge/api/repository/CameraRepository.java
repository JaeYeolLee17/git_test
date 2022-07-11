package com.e4motion.challenge.api.repository;

import com.e4motion.challenge.api.domain.Camera;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface CameraRepository extends JpaRepository<Camera, Long>, CameraRepositoryCustom {

    @Transactional(readOnly = true)
    Optional<Camera> findByCameraNo(String cameraNo);

    @Transactional
    void deleteByCameraNo(String cameraNo);

}
