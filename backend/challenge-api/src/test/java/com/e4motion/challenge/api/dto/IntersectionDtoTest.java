package com.e4motion.challenge.api.dto;

import com.e4motion.challenge.api.TestDataHelper;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class IntersectionDtoTest {

    @Test
    public void testToString() {

        IntersectionDto intersectionDto = TestDataHelper.getIntersectionDto1();
        assertThat(intersectionDto.toString()).isNotNull();
    }
}