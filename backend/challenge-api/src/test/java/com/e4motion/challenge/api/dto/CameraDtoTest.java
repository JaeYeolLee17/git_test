package com.e4motion.challenge.api.dto;

import com.e4motion.challenge.api.TestDataHelper;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CameraDtoTest {

	@Test
	public void testToString() {

		CameraDto cameraDto = TestDataHelper.getCameraDto1();
		assertThat(cameraDto.toString()).isNotNull();
	}
}
