package com.e4motion.challenge.api.service;

import com.e4motion.challenge.api.dto.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class TsiServiceImplTest {

    @Autowired
    TsiService tsiService;

    @Test
    void upsertTsi() {

        TsiHubDto tsiHubDto1 = getTsiHubDto(1000000001L);
        TsiHubDto tsiHubDto2 = getTsiHubDto(1000000002L);

        tsiService.upsert(tsiHubDto1);
        tsiService.upsert(tsiHubDto2);

        List<TsiDto> tsiDtos = tsiService.getList(null, null);
        assertThat(tsiDtos.size()).isEqualTo(2);
        for (TsiDto tsiDto : tsiDtos) {
            assertThat(tsiDto.getTsiSignals().size()).isEqualTo(4);
            for (TsiSignalDto tsiSignalDto : tsiDto.getTsiSignals()) {
                assertThat(tsiSignalDto.getTsiSignalInfos().size()).isEqualTo(3);
            }
        }

        // add a direction
        tsiHubDto1.getTsiSignals().addAll(getTsiHubSignals(50, TsiSignalStatus.YELLOW_FLASHING));
        tsiHubDto1.setSignalCount(15);

        tsiService.upsert(tsiHubDto1);

        tsiDtos = tsiService.getList(null, null);
        assertThat(tsiDtos.size()).isEqualTo(2);
        assertThat(tsiDtos.get(0).getTsiSignals().size()).isEqualTo(5);
        for (TsiSignalDto tsiSignalDto : tsiDtos.get(0).getTsiSignals()) {
            assertThat(tsiSignalDto.getTsiSignalInfos().size()).isEqualTo(3);
        }

        // delete a direction
        List<TsiHubSignalDto> tsiHubSignalDtos =  tsiHubDto2.getTsiSignals().stream()
                .filter(tsiSignalDto -> tsiSignalDto.getDirection() != 10)
                .collect(Collectors.toList());
        tsiHubDto2.setTsiSignals(tsiHubSignalDtos);
        tsiHubDto1.setSignalCount(9);

        tsiService.upsert(tsiHubDto2);

        tsiDtos = tsiService.getList(null, null);
        assertThat(tsiDtos.size()).isEqualTo(2);

        assertThat(tsiDtos.get(1).getTsiSignals().size()).isEqualTo(3);
        for (TsiSignalDto tsiSignalDto : tsiDtos.get(1).getTsiSignals()) {
            assertThat(tsiSignalDto.getTsiSignalInfos().size()).isEqualTo(3);
        }
    }

    private TsiHubDto getTsiHubDto(Long nodeId) {

        List<TsiHubSignalDto> tsiHubSignalDtos = getTsiHubSignals(10, TsiSignalStatus.RED);
        tsiHubSignalDtos.addAll(getTsiHubSignals(20, TsiSignalStatus.YELLOW));
        tsiHubSignalDtos.addAll(getTsiHubSignals(30, TsiSignalStatus.GREEN));
        tsiHubSignalDtos.addAll(getTsiHubSignals(40, TsiSignalStatus.RED_FLASHING));

        return TsiHubDto.builder()
                .nodeId(nodeId)
                .transition(false)
                .response(false)
                .lightsOut(false)
                .flashing(false)
                .manual(false)
                .errorContradiction(false)
                .errorCenter(false)
                .errorScu(false)
                .cycleCounter(100)
                .signalCount(tsiHubSignalDtos.size())
                .time(LocalDateTime.now())
                .tsiSignals(tsiHubSignalDtos)
                .build();
    }

    private List<TsiHubSignalDto> getTsiHubSignals(Integer direction, TsiSignalStatus status) {
        List<TsiHubSignalDto> tsiHubSignalDtos = new ArrayList<>();
        tsiHubSignalDtos.add(TsiHubSignalDto.builder()
                .info(TsiSignalInfo.STRAIGHT)
                .timeReliability(TsiTimeReliability.FIXED)
                .person(false)
                .status(status)
                .displayTime(100)
                .remainTime(10)
                .direction(direction)
                .build());
        tsiHubSignalDtos.add(TsiHubSignalDto.builder()
                .info(TsiSignalInfo.LEFT)
                .timeReliability(TsiTimeReliability.FIXED)
                .person(false)
                .status(status)
                .displayTime(100)
                .remainTime(10)
                .direction(direction)
                .build());
        tsiHubSignalDtos.add(TsiHubSignalDto.builder()
                .info(TsiSignalInfo.PERSON)
                .timeReliability(TsiTimeReliability.FIXED)
                .person(false)
                .status(status)
                .displayTime(100)
                .remainTime(10)
                .direction(direction)
                .build());
        return tsiHubSignalDtos;
    }
}