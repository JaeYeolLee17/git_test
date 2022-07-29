package com.e4motion.challenge.api.service;

import com.e4motion.challenge.api.dto.TsiHubDto;
import com.e4motion.challenge.api.dto.TsiSignalInfo;
import com.e4motion.challenge.api.dto.TsiSignalStatus;
import com.e4motion.challenge.api.dto.TsiTimeReliability;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class TsiKafkaConsumerTest {

    @Autowired
    TsiHubConsumer tsiKafkaConsumer;

    @Test
    void parseTsi() {

        byte[] data = new byte[] {
                93, -105, 57, 84, 0, 0, 7, -122, 96, 118, 30, -71, 16, 3, -128, 121,
                20, 32, 1, 121, 84, 20, 32, 1, -125, 124, 30, 48, 1, -120, 126, 30, 16, 3, 88, 81, 40, 48, 3,
                24, 19, 40
        };

        TsiHubDto tsiHubDto = tsiKafkaConsumer.parseTsi(data);

        assertThat(tsiHubDto.getNodeId()).isEqualTo(1570191700);

        assertThat(tsiHubDto.getTransition()).isFalse();
        assertThat(tsiHubDto.getResponse()).isFalse();
        assertThat(tsiHubDto.getLightsOut()).isFalse();
        assertThat(tsiHubDto.getFlashing()).isFalse();
        assertThat(tsiHubDto.getManual()).isFalse();

        assertThat(tsiHubDto.getErrorScu()).isFalse();
        assertThat(tsiHubDto.getErrorCenter()).isFalse();
        assertThat(tsiHubDto.getErrorContradiction()).isFalse();

        assertThat(tsiHubDto.getCycleCounter()).isEqualTo(7);
        assertThat(tsiHubDto.getSignalCount()).isEqualTo(6);
        assertThat(tsiHubDto.getTime()).isEqualTo(LocalDateTime.of(2021, 4, 14, 7, 44, 9));

        assertThat(tsiHubDto.getTsiSignals().size()).isEqualTo(6);

        assertThat(tsiHubDto.getTsiSignals().get(0).getInfo()).isEqualTo(TsiSignalInfo.STRAIGHT);
        assertThat(tsiHubDto.getTsiSignals().get(0).getTimeReliability()).isEqualTo(TsiTimeReliability.FIXED);
        assertThat(tsiHubDto.getTsiSignals().get(0).getPerson()).isFalse();
        assertThat(tsiHubDto.getTsiSignals().get(0).getStatus()).isEqualTo(TsiSignalStatus.GREEN);
        assertThat(tsiHubDto.getTsiSignals().get(0).getDisplayTime()).isEqualTo(128);
        assertThat(tsiHubDto.getTsiSignals().get(0).getRemainTime()).isEqualTo(121);
        assertThat(tsiHubDto.getTsiSignals().get(0).getDirection()).isEqualTo(20);

        assertThat(tsiHubDto.getTsiSignals().get(1).getInfo()).isEqualTo(TsiSignalInfo.LEFT);
        assertThat(tsiHubDto.getTsiSignals().get(1).getTimeReliability()).isEqualTo(TsiTimeReliability.FIXED);
        assertThat(tsiHubDto.getTsiSignals().get(1).getPerson()).isFalse();
        assertThat(tsiHubDto.getTsiSignals().get(1).getStatus()).isEqualTo(TsiSignalStatus.RED);
        assertThat(tsiHubDto.getTsiSignals().get(1).getDisplayTime()).isEqualTo(121);
        assertThat(tsiHubDto.getTsiSignals().get(1).getRemainTime()).isEqualTo(84);
        assertThat(tsiHubDto.getTsiSignals().get(1).getDirection()).isEqualTo(20);

        assertThat(tsiHubDto.getTsiSignals().get(2).getInfo()).isEqualTo(TsiSignalInfo.LEFT);
        assertThat(tsiHubDto.getTsiSignals().get(1).getTimeReliability()).isEqualTo(TsiTimeReliability.FIXED);
        assertThat(tsiHubDto.getTsiSignals().get(1).getPerson()).isFalse();
        assertThat(tsiHubDto.getTsiSignals().get(1).getStatus()).isEqualTo(TsiSignalStatus.RED);
        assertThat(tsiHubDto.getTsiSignals().get(2).getDisplayTime()).isEqualTo(131);
        assertThat(tsiHubDto.getTsiSignals().get(2).getRemainTime()).isEqualTo(124);
        assertThat(tsiHubDto.getTsiSignals().get(2).getDirection()).isEqualTo(30);

        assertThat(tsiHubDto.getTsiSignals().get(3).getInfo()).isEqualTo(TsiSignalInfo.PERSON);
        assertThat(tsiHubDto.getTsiSignals().get(1).getTimeReliability()).isEqualTo(TsiTimeReliability.FIXED);
        assertThat(tsiHubDto.getTsiSignals().get(1).getPerson()).isFalse();
        assertThat(tsiHubDto.getTsiSignals().get(1).getStatus()).isEqualTo(TsiSignalStatus.RED);
        assertThat(tsiHubDto.getTsiSignals().get(3).getDisplayTime()).isEqualTo(136);
        assertThat(tsiHubDto.getTsiSignals().get(3).getRemainTime()).isEqualTo(126);
        assertThat(tsiHubDto.getTsiSignals().get(3).getDirection()).isEqualTo(30);

        assertThat(tsiHubDto.getTsiSignals().get(4).getInfo()).isEqualTo(TsiSignalInfo.STRAIGHT);
        assertThat(tsiHubDto.getTsiSignals().get(0).getTimeReliability()).isEqualTo(TsiTimeReliability.FIXED);
        assertThat(tsiHubDto.getTsiSignals().get(0).getPerson()).isFalse();
        assertThat(tsiHubDto.getTsiSignals().get(0).getStatus()).isEqualTo(TsiSignalStatus.GREEN);
        assertThat(tsiHubDto.getTsiSignals().get(4).getDisplayTime()).isEqualTo(88);
        assertThat(tsiHubDto.getTsiSignals().get(4).getRemainTime()).isEqualTo(81);
        assertThat(tsiHubDto.getTsiSignals().get(4).getDirection()).isEqualTo(40);

        assertThat(tsiHubDto.getTsiSignals().get(5).getInfo()).isEqualTo(TsiSignalInfo.PERSON);
        assertThat(tsiHubDto.getTsiSignals().get(0).getTimeReliability()).isEqualTo(TsiTimeReliability.FIXED);
        assertThat(tsiHubDto.getTsiSignals().get(0).getPerson()).isFalse();
        assertThat(tsiHubDto.getTsiSignals().get(0).getStatus()).isEqualTo(TsiSignalStatus.GREEN);
        assertThat(tsiHubDto.getTsiSignals().get(5).getDisplayTime()).isEqualTo(24);
        assertThat(tsiHubDto.getTsiSignals().get(5).getRemainTime()).isEqualTo(19);
        assertThat(tsiHubDto.getTsiSignals().get(5).getDirection()).isEqualTo(40);
    }
}