package com.e4motion.challenge.data.collector.repository;

import com.e4motion.challenge.data.collector.HBaseMockTest;
import com.e4motion.challenge.data.collector.domain.Camera;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@Transactional
class CameraRepositoryTest extends HBaseMockTest {

    @Autowired
    private CameraRepository cameraRepository;

    @Test
    void findByCameraId() {

        String cameraId = "C0001";
        Optional<Camera> camera = cameraRepository.findByCameraId(cameraId);
        assertThat(camera.isPresent()).isTrue();
        assertThat(camera.get().getSettingsUpdated()).isFalse();

        cameraId = "C0002";
        camera = cameraRepository.findByCameraId(cameraId);
        assertThat(camera.isPresent()).isTrue();
        assertThat(camera.get().getSettingsUpdated()).isTrue();

        cameraId = "C0003";
        camera = cameraRepository.findByCameraId(cameraId);
        assertThat(camera.isPresent()).isTrue();
        assertThat(camera.get().getSettingsUpdated()).isTrue();

        cameraId = "C0004";
        camera = cameraRepository.findByCameraId(cameraId);
        assertThat(camera.isPresent()).isFalse();
    }
}