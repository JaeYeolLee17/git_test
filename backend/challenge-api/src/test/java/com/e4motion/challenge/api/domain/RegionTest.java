package com.e4motion.challenge.api.domain;

import com.e4motion.challenge.api.TestDataHelper;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RegionTest {

    @Test
    public void testToString() {

        Region region = TestDataHelper.getRegion1();
        assertThat(region.toString()).isNotNull();
    }
}