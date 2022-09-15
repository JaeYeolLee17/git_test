package com.e4motion.challenge.api.domain;

import com.e4motion.challenge.api.TestDataHelper;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LinkTest {

    @Test
    public void testToString() {

        Link link = TestDataHelper.getLink1();
        assertThat(link.toString()).isNotNull();
    }
}