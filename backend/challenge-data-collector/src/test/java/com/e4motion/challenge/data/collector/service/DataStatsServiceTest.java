package com.e4motion.challenge.data.collector.service;

import com.e4motion.challenge.common.utils.DateTimeHelper;
import com.e4motion.challenge.data.collector.HBaseMockTest;
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
class DataStatsServiceTest extends HBaseMockTest {

    @Autowired
    DataStatsService dataStatsService;

    @Autowired
    DataStatsRepository dataStatsRepository;

    String expectedI = null;
    String expectedR = null;
    int expectedP = 0;
    int[] expectedSList = {0, 0, 0, 0, 0, 0};
    int[] expectedRList = {0, 0, 0, 0, 0, 0};
    int[] expectedLList = {0, 0, 0, 0, 0, 0};
    int[] expectedUList = {0, 0, 0, 0, 0, 0};
    int[] expectedQt = {0, 0, 0, 0, 0, 0};
    int expectedQtT = 0;
    
    //TODO: qm 관련 삭제 가능성
    int expectedQmsrLen = 0;
    int[] expectedQmsr = {0, 0, 0, 0, 0, 0};
    int expectedQmluLen = 0;
    int[] expectedQmlu = {0, 0, 0, 0, 0, 0};

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
                .u(new Integer[]{8, 0, 0, 0, 0, 0})
                .ld(Stream.of(laneDataDtos).collect(Collectors.toList()))
                .build();
    }

    private TrafficDataDto getTrafficDataDto2(LaneDataDto... laneDataDtos) {

        return TrafficDataDto.builder()
                .st("2022-07-13 12:29:50")
                .et("2022-07-13 12:30:00")
                .p(7)
                .u(new Integer[]{6, 0, 0, 0, 0, 0})
                .ld(Stream.of(laneDataDtos).collect(Collectors.toList()))
                .build();
    }

    private TrafficDataDto getTrafficDataDto3(LaneDataDto... laneDataDtos) {

        return TrafficDataDto.builder()
                .st("2022-07-13 12:30:00")
                .et("2022-07-13 12:30:10")
                .p(5)
                .u(new Integer[]{4, 0, 0, 0, 0, 0})
                .ld(Stream.of(laneDataDtos).collect(Collectors.toList()))
                .build();
    }

    private TrafficDataDto getTrafficDataDto4(LaneDataDto... laneDataDtos) {

        return TrafficDataDto.builder()
                .st("2022-07-13 12:30:10")
                .et("2022-07-13 12:30:20")
                .p(3)
                .u(new Integer[]{2, 0, 0, 0, 0, 0})
                .ld(Stream.of(laneDataDtos).collect(Collectors.toList()))
                .build();
    }

    private LaneDataDto getLaneDataDto1() {

        return LaneDataDto.builder()
                .ln(1)
                .qml(10)
                .qm(new Integer[]{10, 20, 30, 40, 50, 0})
                .qal(5.5F)
                .qa(new Float[]{1.1F, 2.2F, 3.3F, 4.4F, 5.5F, 0F})
                .s(new Integer[]{1, 2, 3, 4, 5, 0})
                .l(new Integer[]{2, 3, 4, 5, 6, 0})
                .r(new Integer[]{3, 4, 5, 6, 7, 0})
                .build();
    }

    private LaneDataDto getLaneDataDto2() {

        return LaneDataDto.builder()
                .ln(2)
                .qml(11)
                .qm(new Integer[]{11, 21, 31, 41, 51, 0})
                .qal(6.6F)
                .qa(new Float[]{2.2F, 3.3F, 4.4F, 5.5F, 6.6F, 0F})
                .s(new Integer[]{2, 3, 4, 5, 6, 0})
                .l(new Integer[]{3, 4, 5, 6, 7, 0})
                .r(new Integer[]{4, 5, 6, 7, 8, 0})
                .build();
    }

    private LaneDataDto getLaneDataDto3() {

        return LaneDataDto.builder()
                .ln(1)
                .qml(20)
                .qm(new Integer[]{60, 70, 80, 90, 100, 0})
                .qal(7.7F)
                .qa(new Float[]{3.3F, 4.4F, 5.5F, 6.6F, 7.7F, 0F})
                .s(new Integer[]{3, 4, 5, 6, 7, 0})
                .l(new Integer[]{4, 5, 6, 7, 8, 0})
                .r(new Integer[]{5, 6, 7, 8, 9, 0})
                .build();
    }

    private LaneDataDto getLaneDataDto4() {

        return LaneDataDto.builder()
                .ln(2)
                .qml(21)
                .qm(new Integer[]{61, 71, 81, 91, 101, 0})
                .qal(8.8F)
                .qa(new Float[]{4.4F, 5.5F, 6.6F, 7.7F, 8.8F, 0F})
                .s(new Integer[]{4, 5, 6, 7, 8, 0})
                .l(new Integer[]{5, 6, 7, 8, 9, 0})
                .r(new Integer[]{6, 7, 8, 9, 10, 0})
                .build();
    }

    private LaneDataDto getLaneDataDto5() {

        return LaneDataDto.builder()
                .ln(1)
                .qml(30)
                .qm(new Integer[]{70, 80, 90, 100, 110, 0})
                .qal(9.9F)
                .qa(new Float[]{5.5F, 6.6F, 7.7F, 8.8F, 9.9F, 0F})
                .s(new Integer[]{5, 6, 7, 8, 9, 0})
                .l(new Integer[]{6, 7, 8, 9, 0, 0})
                .r(new Integer[]{7, 8, 9, 0, 1, 0})
                .build();
    }

    private LaneDataDto getLaneDataDto6() {

        return LaneDataDto.builder()
                .ln(1)
                .qml(31)
                .qm(new Integer[]{71, 81, 91, 101, 111, 0})
                .qal(0.1F)
                .qa(new Float[]{6.6F, 7.7F, 8.8F, 9.9F, 0.1F, 0F})
                .s(new Integer[]{6, 7, 8, 9, 0, 0})
                .l(new Integer[]{7, 8, 9, 0, 1, 0})
                .r(new Integer[]{8, 9, 0, 1, 2, 0})
                .build();
    }

    private void clearExpectedDataStats() {
        expectedI = null;
        expectedR = null;
        expectedP = 0;
        Arrays.fill(expectedSList, 0);
        Arrays.fill(expectedRList, 0);
        Arrays.fill(expectedLList, 0);
        Arrays.fill(expectedUList, 0);
        Arrays.fill(expectedQt, 0);
        expectedQtT = 0;
        expectedQmsrLen = 0;
        Arrays.fill(expectedQmsr, 0);
        expectedQmluLen = 0;
        Arrays.fill(expectedQmlu, 0);
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

            for (int i = 0; i < 6; i++ ) {
                expectedUList[i] += td.getU()[i];
            }

            int qaT = (int)ChronoUnit.SECONDS.between(
                    DateTimeHelper.parseLocalDateTime(td.getSt()),
                    DateTimeHelper.parseLocalDateTime(td.getEt()));

            for (LaneDataDto ld : td.getLd()) {
                for (int i = 0; i < 6; i++ ) {
                    expectedSList[i] += (ld.getS()[i]);
                    expectedRList[i] += (ld.getR()[i]);
                    expectedLList[i] += ld.getL()[i];
                    expectedQt[i] += (int)(ld.getQa()[i] * qaT);
                }
                if (ld.getLn() == 1 && td.getLd().size() > 1) {     // left, uturn
                    if (expectedQmluLen < ld.getQml()) {
                        expectedQmluLen = ld.getQml();
                        for (int i = 0; i < 6; i++) {
                            expectedQmlu[i] = ld.getQm()[i];
                        }
                    }
                } else {    // straight, right
                    if (expectedQmsrLen < ld.getQml()) {
                        expectedQmsrLen = ld.getQml();
                        for (int i = 0; i < 6; i++) {
                            expectedQmsr[i] = ld.getQm()[i];
                        }
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

        assertThat(dataStats.getS0()).isEqualTo(expectedSList[0]);
        assertThat(dataStats.getS1()).isEqualTo(expectedSList[1]);
        assertThat(dataStats.getS2()).isEqualTo(expectedSList[2]);
        assertThat(dataStats.getS3()).isEqualTo(expectedSList[3]);
        assertThat(dataStats.getS4()).isEqualTo(expectedSList[4]);
        assertThat(dataStats.getS5()).isEqualTo(expectedSList[5]);

        assertThat(dataStats.getR0()).isEqualTo(expectedRList[0]);
        assertThat(dataStats.getR1()).isEqualTo(expectedRList[1]);
        assertThat(dataStats.getR2()).isEqualTo(expectedRList[2]);
        assertThat(dataStats.getR3()).isEqualTo(expectedRList[3]);
        assertThat(dataStats.getR4()).isEqualTo(expectedRList[4]);
        assertThat(dataStats.getR5()).isEqualTo(expectedRList[5]);

        assertThat(dataStats.getL0()).isEqualTo(expectedLList[0]);
        assertThat(dataStats.getL1()).isEqualTo(expectedLList[1]);
        assertThat(dataStats.getL2()).isEqualTo(expectedLList[2]);
        assertThat(dataStats.getL3()).isEqualTo(expectedLList[3]);
        assertThat(dataStats.getL4()).isEqualTo(expectedLList[4]);
        assertThat(dataStats.getL5()).isEqualTo(expectedLList[5]);

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

        assertThat(dataStats.getQt0()).isEqualTo(expectedQt[0]);
        assertThat(dataStats.getQt1()).isEqualTo(expectedQt[1]);
        assertThat(dataStats.getQt2()).isEqualTo(expectedQt[2]);
        assertThat(dataStats.getQt3()).isEqualTo(expectedQt[3]);
        assertThat(dataStats.getQt4()).isEqualTo(expectedQt[4]);
        assertThat(dataStats.getQt4()).isEqualTo(expectedQt[4]);

        assertThat(dataStats.getQtT()).isEqualTo(expectedQtT);
    }
}