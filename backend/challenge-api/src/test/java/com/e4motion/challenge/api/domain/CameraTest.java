package com.e4motion.challenge.api.domain;

import com.e4motion.challenge.api.TestDataHelper;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CameraTest {

    @Test
    public void testToString() {

        Camera camera = TestDataHelper.getCamera1();
        assertThat(camera.toString()).isNotNull();
    }
}