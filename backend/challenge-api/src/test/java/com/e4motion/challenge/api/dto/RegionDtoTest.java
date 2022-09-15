package com.e4motion.challenge.api.dto;

import com.e4motion.challenge.api.TestDataHelper;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RegionDtoTest {

    @Test
    public void testToString() {

        RegionDto regionDto = TestDataHelper.getRegionDto1();
        assertThat(regionDto.toString()).isNotNull();
    }
}