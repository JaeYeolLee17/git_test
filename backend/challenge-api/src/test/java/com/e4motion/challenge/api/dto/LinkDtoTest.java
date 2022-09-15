package com.e4motion.challenge.api.dto;

import com.e4motion.challenge.api.TestDataHelper;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LinkDtoTest {

    @Test
    public void testToString() {

        LinkDto linkDto = TestDataHelper.getLinkDto1();
        assertThat(linkDto.toString()).isNotNull();
    }
}