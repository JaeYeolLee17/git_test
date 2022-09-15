package com.e4motion.challenge.api.domain;

import com.e4motion.challenge.api.TestDataHelper;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class IntersectionTest {

    @Test
    public void testToString() {

        Intersection intersection = TestDataHelper.getIntersection1();
        assertThat(intersection.toString()).isNotNull();
    }
}