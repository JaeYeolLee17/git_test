package com.e4motion.challenge.data.collector.service.impl;

import com.e4motion.challenge.common.utils.DateTimeHelper;
import com.e4motion.challenge.data.collector.domain.DataStats;
import com.e4motion.challenge.data.collector.dto.CameraDataDto;
import com.e4motion.challenge.data.collector.repository.DataStatsRepository;
import com.e4motion.challenge.data.collector.service.DataStatsService;
import com.e4motion.challenge.data.common.dto.LaneDataDto;
import com.e4motion.challenge.data.common.dto.TrafficDataDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@RequiredArgsConstructor
@Service
public class DataStatsServiceImpl implements DataStatsService {

    private final DataStatsRepository dataStatsRepository;

    private static final int TIME_INTERVAL = 15;

    @Transactional
    public Long insert(CameraDataDto cameraDataDto) {

        DataStats dataStats = null;
        LocalDateTime lastT = null;

        for(TrafficDataDto td : cameraDataDto.getTd()) {

            LocalDateTime st = DateTimeHelper.parseLocalDateTime(td.getSt());
            LocalDateTime et = DateTimeHelper.parseLocalDateTime(td.getEt());
            int minute = st.getMinute()/TIME_INTERVAL * TIME_INTERVAL;

            LocalDateTime t = LocalDateTime.of(st.getYear(), st.getMonth(), st.getDayOfMonth(), st.getHour(), minute, 0);
            if (!t.equals(lastT) && dataStats != null) {
                dataStatsRepository.save(dataStats);
                dataStats = null;
            }
            if (dataStats == null) {
                dataStats = dataStatsRepository.findByTAndC(t, cameraDataDto.getC())
                        .orElse(makeEmptyDataStats(t, cameraDataDto.getC(), cameraDataDto.getI(), cameraDataDto.getR()));
            }

            dataStats.setP(dataStats.getP() + td.getP());

            dataStats.setU0(dataStats.getU0() + td.getU()[0]);
            dataStats.setU1(dataStats.getU1() + td.getU()[1]);
            dataStats.setU2(dataStats.getU2() + td.getU()[2]);
            dataStats.setU3(dataStats.getU3() + td.getU()[3]);
            dataStats.setU4(dataStats.getU4() + td.getU()[4]);
            dataStats.setU5(dataStats.getU5() + td.getU()[5]);

            int qaT = (int)ChronoUnit.SECONDS.between(st, et);
            for (LaneDataDto ld : td.getLd()) {

                dataStats.setS0(dataStats.getS0() + ld.getS()[0]);
                dataStats.setS1(dataStats.getS1() + ld.getS()[1]);
                dataStats.setS2(dataStats.getS2() + ld.getS()[2]);
                dataStats.setS3(dataStats.getS3() + ld.getS()[3]);
                dataStats.setS4(dataStats.getS4() + ld.getS()[4]);
                dataStats.setS5(dataStats.getS5() + ld.getS()[5]);

                dataStats.setR0(dataStats.getR0() + ld.getR()[0]);
                dataStats.setR1(dataStats.getR1() + ld.getR()[1]);
                dataStats.setR2(dataStats.getR2() + ld.getR()[2]);
                dataStats.setR3(dataStats.getR3() + ld.getR()[3]);
                dataStats.setR4(dataStats.getR4() + ld.getR()[4]);
                dataStats.setR5(dataStats.getR5() + ld.getR()[5]);

                dataStats.setL0(dataStats.getL0() + ld.getL()[0]);
                dataStats.setL1(dataStats.getL1() + ld.getL()[1]);
                dataStats.setL2(dataStats.getL2() + ld.getL()[2]);
                dataStats.setL3(dataStats.getL3() + ld.getL()[3]);
                dataStats.setL4(dataStats.getL4() + ld.getL()[4]);
                dataStats.setL5(dataStats.getL5() + ld.getL()[5]);

                dataStats.setQt0(dataStats.getQt0() + (int)(ld.getQa()[0] * qaT));
                dataStats.setQt1(dataStats.getQt1() + (int)(ld.getQa()[1] * qaT));
                dataStats.setQt2(dataStats.getQt2() + (int)(ld.getQa()[2] * qaT));
                dataStats.setQt3(dataStats.getQt3() + (int)(ld.getQa()[3] * qaT));
                dataStats.setQt4(dataStats.getQt4() + (int)(ld.getQa()[4] * qaT));
                dataStats.setQt5(dataStats.getQt5() + (int)(ld.getQa()[4] * qaT));

                //TODO: qm 관련 삭제 가능성
                if (td.getLd().size() > 1 && ld.getLn() == 1 ) {    // left, uturn
                    if (dataStats.getQmluLen() < ld.getQml()) {
                        dataStats.setQmluLen(ld.getQml());
                        dataStats.setQmlu0(ld.getQm()[0]);
                        dataStats.setQmlu1(ld.getQm()[1]);
                        dataStats.setQmlu2(ld.getQm()[2]);
                        dataStats.setQmlu3(ld.getQm()[3]);
                        dataStats.setQmlu4(ld.getQm()[4]);
                    }
                } else {    // straight, right
                    if (dataStats.getQmsrLen() < ld.getQml()) {
                        dataStats.setQmsrLen(ld.getQml());
                        dataStats.setQmsr0(ld.getQm()[0]);
                        dataStats.setQmsr1(ld.getQm()[1]);
                        dataStats.setQmsr2(ld.getQm()[2]);
                        dataStats.setQmsr3(ld.getQm()[3]);
                        dataStats.setQmsr4(ld.getQm()[4]);
                    }
                }

            }

            dataStats.setQtT(dataStats.getQtT() + qaT);
            lastT = t;
        }

        if (dataStats != null) {
            return dataStatsRepository.save(dataStats).getId();
        }

        return null;
    }

    private DataStats makeEmptyDataStats(LocalDateTime t, String c, String i, String r) {

        return DataStats.builder()
                .t(t)
                .c(c)
                .i(i)
                .r(r)
                .p(0)
                .s0(0)
                .s1(0)
                .s2(0)
                .s3(0)
                .s4(0)
                .s5(0)
                .r0(0)
                .r1(0)
                .r2(0)
                .r3(0)
                .r4(0)
                .r5(0)
                .u0(0)
                .u1(0)
                .u2(0)
                .u3(0)
                .u4(0)
                .u5(0)
                .l0(0)
                .l1(0)
                .l2(0)
                .l3(0)
                .l4(0)
                .l5(0)
                .qt0(0)
                .qt1(0)
                .qt2(0)
                .qt3(0)
                .qt4(0)
                .qt5(0)
                .qtT(0)
                .qmsrLen(0)
                .qmsr0(0)
                .qmsr1(0)
                .qmsr2(0)
                .qmsr3(0)
                .qmsr4(0)
                .qmluLen(0)
                .qmlu0(0)
                .qmlu1(0)
                .qmlu2(0)
                .qmlu3(0)
                .qmlu4(0)
                .build();
    }
}
