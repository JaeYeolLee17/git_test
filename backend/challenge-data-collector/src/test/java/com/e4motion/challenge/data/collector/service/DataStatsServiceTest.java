package com.e4motion.challenge.data.collector.service;

import com.e4motion.challenge.common.utils.DateTimeHelper;
import com.e4motion.challenge.data.collector.domain.DataStats;
import com.e4motion.challenge.data.collector.dto.CameraDataDto;
import com.e4motion.challenge.data.collector.repository.DataStatsRepository;
import com.e4motion.challenge.data.common.dto.LaneDataDto;
import com.e4motion.challenge.data.common.dto.TrafficDataDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class DataStatsServiceTest {

    @Autowired
    DataStatsService dataStatsService;

    @Autowired
    DataStatsRepository dataStatsRepository;

    String expectedI = null;
    String expectedR = null;
    int expectedP = 0;
    int[] expectedSr = {0, 0, 0, 0, 0};
    int expectedQmsrLen = 0;
    int[] expectedQmsr = {0, 0, 0, 0, 0};
    int[] expectedQtsr = {0, 0, 0, 0, 0};
    int[] expectedLu = {0, 0, 0, 0, 0};
    int expectedQmluLen = 0;
    int[] expectedQmlu = {0, 0, 0, 0, 0};
    int[] expectedQtlu = {0, 0, 0, 0, 0};
    int expectedQtT = 0;

    @BeforeEach
    void setUp() {
        clearExpectedDataStats();
    }

    @Test
    void insert() throws ParseException {

        CameraDataDto cameraDataDto = getCameraDataDto(
                getTrafficDataDto1(getLaneDataDto1(), getLaneDataDto2()),
                getTrafficDataDto2(getLaneDataDto3(), getLaneDataDto4()));

        dataStatsService.insert(cameraDataDto);

        Optional<DataStats> found =  dataStatsRepository.findByTAndC(
                DateTimeHelper.parseLocalDateTime("2022-07-13 12:15:00"), cameraDataDto.getC());
        assertThat(found.isPresent()).isTrue();

        makeExpectedDataStats(cameraDataDto, 0);

        assertDataStats(found.get());
    }

    @Test
    void insert_2times() throws ParseException {

        CameraDataDto cameraDataDto = getCameraDataDto(
                getTrafficDataDto1(getLaneDataDto1(), getLaneDataDto2()),
                getTrafficDataDto2(getLaneDataDto3(), getLaneDataDto4()),
                getTrafficDataDto3(getLaneDataDto5()),
                getTrafficDataDto4(getLaneDataDto6()));

        dataStatsService.insert(cameraDataDto);

        // 1st time
        Optional<DataStats> found =  dataStatsRepository.findByTAndC(
                DateTimeHelper.parseLocalDateTime("2022-07-13 12:15:00"), cameraDataDto.getC());
        assertThat(found.isPresent()).isTrue();

        makeExpectedDataStats(cameraDataDto, 0);

        assertDataStats(found.get());

        // 2nd time
        clearExpectedDataStats();

        found =  dataStatsRepository.findByTAndC(
                DateTimeHelper.parseLocalDateTime("2022-07-13 12:30:00"), cameraDataDto.getC());
        assertThat(found.isPresent()).isTrue();

        makeExpectedDataStats(cameraDataDto, 1);

        assertDataStats(found.get());
    }

    @Test
    void insert_sequentially() throws ParseException {

        // 1st td
        CameraDataDto cameraDataDto = getCameraDataDto(getTrafficDataDto1(getLaneDataDto1(), getLaneDataDto2()));

        dataStatsService.insert(cameraDataDto);

        Optional<DataStats> found =  dataStatsRepository.findByTAndC(
                DateTimeHelper.parseLocalDateTime("2022-07-13 12:15:00"), cameraDataDto.getC());
        assertThat(found.isPresent()).isTrue();

        makeExpectedDataStats(cameraDataDto, 0);

        assertDataStats(found.get());

        // 2nd td
        cameraDataDto = getCameraDataDto(getTrafficDataDto2(getLaneDataDto3(), getLaneDataDto4()));

        dataStatsService.insert(cameraDataDto);

        found =  dataStatsRepository.findByTAndC(
                DateTimeHelper.parseLocalDateTime("2022-07-13 12:15:00"), cameraDataDto.getC());
        assertThat(found.isPresent()).isTrue();

        makeExpectedDataStats(cameraDataDto, 0);

        assertDataStats(found.get());

        // 3rd td
        clearExpectedDataStats();

        cameraDataDto = getCameraDataDto(getTrafficDataDto3(getLaneDataDto5()));

        dataStatsService.insert(cameraDataDto);

        found =  dataStatsRepository.findByTAndC(
                DateTimeHelper.parseLocalDateTime("2022-07-13 12:30:00"), cameraDataDto.getC());
        assertThat(found.isPresent()).isTrue();

        makeExpectedDataStats(cameraDataDto, 0);

        assertDataStats(found.get());

        // 4th td
        cameraDataDto = getCameraDataDto(getTrafficDataDto4(getLaneDataDto6()));

        dataStatsService.insert(cameraDataDto);

        found =  dataStatsRepository.findByTAndC(
                DateTimeHelper.parseLocalDateTime("2022-07-13 12:30:00"), cameraDataDto.getC());
        assertThat(found.isPresent()).isTrue();

        makeExpectedDataStats(cameraDataDto, 0);

        assertDataStats(found.get());
    }

    private CameraDataDto getCameraDataDto(TrafficDataDto... trafficDataDtos) {

        return CameraDataDto.builder()
                .v("v1")
                .c("C0099")
                .i("I0088")
                .r("R77")
                .t("2022-07-13 12:30:20")
                .td(Stream.of(trafficDataDtos).collect(Collectors.toList()))
                .build();
    }

    private TrafficDataDto getTrafficDataDto1(LaneDataDto... laneDataDtos) {

        return TrafficDataDto.builder()
                .st("2022-07-13 12:29:40")
                .et("2022-07-13 12:29:50")
                .p(9)
                .u(new Integer[]{8, 0, 0, 0, 0})
                .ld(Stream.of(laneDataDtos).collect(Collectors.toList()))
                .build();
    }

    private TrafficDataDto getTrafficDataDto2(LaneDataDto... laneDataDtos) {

        return TrafficDataDto.builder()
                .st("2022-07-13 12:29:50")
                .et("2022-07-13 12:30:00")
                .p(7)
                .u(new Integer[]{6, 0, 0, 0, 0})
                .ld(Stream.of(laneDataDtos).collect(Collectors.toList()))
                .build();
    }

    private TrafficDataDto getTrafficDataDto3(LaneDataDto... laneDataDtos) {

        return TrafficDataDto.builder()
                .st("2022-07-13 12:30:00")
                .et("2022-07-13 12:30:10")
                .p(5)
                .u(new Integer[]{4, 0, 0, 0, 0})
                .ld(Stream.of(laneDataDtos).collect(Collectors.toList()))
                .build();
    }

    private TrafficDataDto getTrafficDataDto4(LaneDataDto... laneDataDtos) {

        return TrafficDataDto.builder()
                .st("2022-07-13 12:30:10")
                .et("2022-07-13 12:30:20")
                .p(3)
                .u(new Integer[]{2, 0, 0, 0, 0})
                .ld(Stream.of(laneDataDtos).collect(Collectors.toList()))
                .build();
    }

    private LaneDataDto getLaneDataDto1() {

        return LaneDataDto.builder()
                .ln(1)
                .qml(10)
                .qm(new Integer[]{10, 20, 30, 40, 50})
                .qal(5.5F)
                .qa(new Float[]{1.1F, 2.2F, 3.3F, 4.4F, 5.5F})
                .s(new Integer[]{1, 2, 3, 4, 5})
                .l(new Integer[]{2, 3, 4, 5, 6})
                .r(new Integer[]{3, 4, 5, 6, 7})
                .build();
    }

    private LaneDataDto getLaneDataDto2() {

        return LaneDataDto.builder()
                .ln(2)
                .qml(11)
                .qm(new Integer[]{11, 21, 31, 41, 51})
                .qal(6.6F)
                .qa(new Float[]{2.2F, 3.3F, 4.4F, 5.5F, 6.6F})
                .s(new Integer[]{2, 3, 4, 5, 6})
                .l(new Integer[]{3, 4, 5, 6, 7})
                .r(new Integer[]{4, 5, 6, 7, 8})
                .build();
    }

    private LaneDataDto getLaneDataDto3() {

        return LaneDataDto.builder()
                .ln(1)
                .qml(20)
                .qm(new Integer[]{60, 70, 80, 90, 100})
                .qal(7.7F)
                .qa(new Float[]{3.3F, 4.4F, 5.5F, 6.6F, 7.7F})
                .s(new Integer[]{3, 4, 5, 6, 7})
                .l(new Integer[]{4, 5, 6, 7, 8})
                .r(new Integer[]{5, 6, 7, 8, 9})
                .build();
    }

    private LaneDataDto getLaneDataDto4() {

        return LaneDataDto.builder()
                .ln(2)
                .qml(21)
                .qm(new Integer[]{61, 71, 81, 91, 101})
                .qal(8.8F)
                .qa(new Float[]{4.4F, 5.5F, 6.6F, 7.7F, 8.8F})
                .s(new Integer[]{4, 5, 6, 7, 8})
                .l(new Integer[]{5, 6, 7, 8, 9})
                .r(new Integer[]{6, 7, 8, 9, 10})
                .build();
    }

    private LaneDataDto getLaneDataDto5() {

        return LaneDataDto.builder()
                .ln(1)
                .qml(30)
                .qm(new Integer[]{70, 80, 90, 100, 110})
                .qal(9.9F)
                .qa(new Float[]{5.5F, 6.6F, 7.7F, 8.8F, 9.9F})
                .s(new Integer[]{5, 6, 7, 8, 9})
                .l(new Integer[]{6, 7, 8, 9, 0})
                .r(new Integer[]{7, 8, 9, 0, 1})
                .build();
    }

    private LaneDataDto getLaneDataDto6() {

        return LaneDataDto.builder()
                .ln(1)
                .qml(31)
                .qm(new Integer[]{71, 81, 91, 101, 111})
                .qal(0.1F)
                .qa(new Float[]{6.6F, 7.7F, 8.8F, 9.9F, 0.1F})
                .s(new Integer[]{6, 7, 8, 9, 0})
                .l(new Integer[]{7, 8, 9, 0, 1})
                .r(new Integer[]{8, 9, 0, 1, 2})
                .build();
    }

    private void clearExpectedDataStats() {
        expectedI = null;
        expectedR = null;
        expectedP = 0;
        Arrays.fill(expectedSr, 0);
        expectedQmsrLen = 0;
        Arrays.fill(expectedQmsr, 0);
        Arrays.fill(expectedQtsr, 0);
        Arrays.fill(expectedLu, 0);
        expectedQmluLen = 0;
        Arrays.fill(expectedQmlu, 0);
        Arrays.fill(expectedQtlu, 0);
        expectedQtT = 0;
    }

    private void makeExpectedDataStats(CameraDataDto cameraDataDto, int index) throws ParseException {

        expectedI = cameraDataDto.getI();
        expectedR = cameraDataDto.getR();

        LocalDateTime lastT = null;

        int idx = 0;
        for (TrafficDataDto td : cameraDataDto.getTd()) {

            LocalDateTime st = DateTimeHelper.parseLocalDateTime(td.getSt());
            LocalDateTime et = DateTimeHelper.parseLocalDateTime(td.getEt());
            int minute = st.getMinute()/15 * 15;
            LocalDateTime t = LocalDateTime.of(st.getYear(), st.getMonth(), st.getDayOfMonth(), st.getHour(), minute, 0);
            if (!t.equals(lastT)) {
                idx++;
            }
            lastT = t;

            if (idx != (index + 1)) {
                continue;
            }

            expectedP += td.getP();

            for (int i = 0; i < 5; i++ ) {
                expectedLu[i] += td.getU()[i];
            }

            int qaT = (int)ChronoUnit.SECONDS.between(
                    DateTimeHelper.parseLocalDateTime(td.getSt()),
                    DateTimeHelper.parseLocalDateTime(td.getEt()));

            for (LaneDataDto ld : td.getLd()) {
                for (int i = 0; i < 5; i++ ) {
                    expectedSr[i] += (ld.getS()[i] + ld.getR()[i]);
                    expectedLu[i] += ld.getL()[i];
                }

                if (ld.getLn() == 1 && td.getLd().size() > 1) {     // left, uturn
                    if (expectedQmluLen < ld.getQml()) {
                        expectedQmluLen = ld.getQml();
                        for (int i = 0; i < 5; i++) {
                            expectedQmlu[i] = ld.getQm()[i];
                        }
                    }
                    for (int i = 0; i < 5; i++) {
                        expectedQtlu[i] += (int)(ld.getQa()[i] * qaT);
                    }
                } else {    // straight, right
                    if (expectedQmsrLen < ld.getQml()) {
                        expectedQmsrLen = ld.getQml();
                        for (int i = 0; i < 5; i++) {
                            expectedQmsr[i] = ld.getQm()[i];
                        }
                    }
                    for (int i = 0; i < 5; i++) {
                        expectedQtsr[i] += (int)(ld.getQa()[i] * qaT);
                    }
                }
            }
            expectedQtT += qaT;
        }
    }

    private void assertDataStats(DataStats dataStats) {

        assertThat(dataStats.getI()).isEqualTo(expectedI);
        assertThat(dataStats.getR()).isEqualTo(expectedR);

        assertThat(dataStats.getP()).isEqualTo(expectedP);

        assertThat(dataStats.getSr0()).isEqualTo(expectedSr[0]);
        assertThat(dataStats.getSr1()).isEqualTo(expectedSr[1]);
        assertThat(dataStats.getSr2()).isEqualTo(expectedSr[2]);
        assertThat(dataStats.getSr3()).isEqualTo(expectedSr[3]);
        assertThat(dataStats.getSr4()).isEqualTo(expectedSr[4]);

        assertThat(dataStats.getLu0()).isEqualTo(expectedLu[0]);
        assertThat(dataStats.getLu1()).isEqualTo(expectedLu[1]);
        assertThat(dataStats.getLu2()).isEqualTo(expectedLu[2]);
        assertThat(dataStats.getLu3()).isEqualTo(expectedLu[3]);
        assertThat(dataStats.getLu4()).isEqualTo(expectedLu[4]);

        assertThat(dataStats.getQmsrLen()).isEqualTo(expectedQmsrLen);
        assertThat(dataStats.getQmsr0()).isEqualTo(expectedQmsr[0]);
        assertThat(dataStats.getQmsr1()).isEqualTo(expectedQmsr[1]);
        assertThat(dataStats.getQmsr2()).isEqualTo(expectedQmsr[2]);
        assertThat(dataStats.getQmsr3()).isEqualTo(expectedQmsr[3]);
        assertThat(dataStats.getQmsr4()).isEqualTo(expectedQmsr[4]);

        assertThat(dataStats.getQmluLen()).isEqualTo(expectedQmluLen);
        assertThat(dataStats.getQmlu0()).isEqualTo(expectedQmlu[0]);
        assertThat(dataStats.getQmlu1()).isEqualTo(expectedQmlu[1]);
        assertThat(dataStats.getQmlu2()).isEqualTo(expectedQmlu[2]);
        assertThat(dataStats.getQmlu3()).isEqualTo(expectedQmlu[3]);
        assertThat(dataStats.getQmlu4()).isEqualTo(expectedQmlu[4]);

        assertThat(dataStats.getQtsr0()).isEqualTo(expectedQtsr[0]);
        assertThat(dataStats.getQtsr1()).isEqualTo(expectedQtsr[1]);
        assertThat(dataStats.getQtsr2()).isEqualTo(expectedQtsr[2]);
        assertThat(dataStats.getQtsr3()).isEqualTo(expectedQtsr[3]);
        assertThat(dataStats.getQtsr4()).isEqualTo(expectedQtsr[4]);

        assertThat(dataStats.getQtlu0()).isEqualTo(expectedQtlu[0]);
        assertThat(dataStats.getQtlu1()).isEqualTo(expectedQtlu[1]);
        assertThat(dataStats.getQtlu2()).isEqualTo(expectedQtlu[2]);
        assertThat(dataStats.getQtlu3()).isEqualTo(expectedQtlu[3]);
        assertThat(dataStats.getQtlu4()).isEqualTo(expectedQtlu[4]);

        assertThat(dataStats.getQtT()).isEqualTo(expectedQtT);
    }
}