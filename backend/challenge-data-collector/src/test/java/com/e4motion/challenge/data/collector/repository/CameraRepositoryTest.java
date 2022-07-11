package com.e4motion.challenge.data.collector.repository;

import com.e4motion.challenge.data.collector.HBaseMockTest;
import com.e4motion.challenge.data.collector.domain.Camera;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class CameraRepositoryTest extends HBaseMockTest {

    @Autowired
    private CameraRepository cameraRepository;

    @Test
    void findByCameraNo() {

        String cameraNo = "C0001";
        Optional<Camera> camera = cameraRepository.findByCameraNo(cameraNo);
        assertThat(camera.isPresent()).isTrue();
        assertThat(camera.get().getSettingsUpdated()).isFalse();

        cameraNo = "C0002";
        camera = cameraRepository.findByCameraNo(cameraNo);
        assertThat(camera.isPresent()).isTrue();
        assertThat(camera.get().getSettingsUpdated()).isTrue();

        cameraNo = "C0003";
        camera = cameraRepository.findByCameraNo(cameraNo);
        assertThat(camera.isPresent()).isTrue();
        assertThat(camera.get().getSettingsUpdated()).isTrue();

        cameraNo = "C0004";
        camera = cameraRepository.findByCameraNo(cameraNo);
        assertThat(camera.isPresent()).isFalse();
    }
}