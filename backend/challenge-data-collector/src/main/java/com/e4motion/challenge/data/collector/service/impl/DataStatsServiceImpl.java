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

import java.text.ParseException;
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

            try {
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
                            .orElse(getInitialDataStats(t, cameraDataDto.getC(), cameraDataDto.getI(), cameraDataDto.getR()));
                }

                dataStats.setP(dataStats.getP() + td.getP());

                dataStats.setLu0(dataStats.getLu0() + td.getU()[0]);
                dataStats.setLu1(dataStats.getLu1() + td.getU()[1]);
                dataStats.setLu2(dataStats.getLu2() + td.getU()[2]);
                dataStats.setLu3(dataStats.getLu3() + td.getU()[3]);
                dataStats.setLu4(dataStats.getLu4() + td.getU()[4]);

                int qaT = (int)ChronoUnit.SECONDS.between(st, et);
                for (LaneDataDto ld : td.getLd()) {

                    dataStats.setSr0(dataStats.getSr0() + ld.getS()[0] + ld.getR()[0]);
                    dataStats.setSr1(dataStats.getSr1() + ld.getS()[1] + ld.getR()[1]);
                    dataStats.setSr2(dataStats.getSr2() + ld.getS()[2] + ld.getR()[2]);
                    dataStats.setSr3(dataStats.getSr3() + ld.getS()[3] + ld.getR()[3]);
                    dataStats.setSr4(dataStats.getSr4() + ld.getS()[4] + ld.getR()[4]);

                    dataStats.setLu0(dataStats.getLu0() + ld.getL()[0]);
                    dataStats.setLu1(dataStats.getLu1() + ld.getL()[1]);
                    dataStats.setLu2(dataStats.getLu2() + ld.getL()[2]);
                    dataStats.setLu3(dataStats.getLu3() + ld.getL()[3]);
                    dataStats.setLu4(dataStats.getLu4() + ld.getL()[4]);

                    if (td.getLd().size() > 1 && ld.getLn() == 1 ) {    // left, uturn
                        if (dataStats.getQmluLen() < ld.getQml()) {
                            dataStats.setQmluLen(ld.getQml());
                            dataStats.setQmlu0(ld.getQm()[0]);
                            dataStats.setQmlu1(ld.getQm()[1]);
                            dataStats.setQmlu2(ld.getQm()[2]);
                            dataStats.setQmlu3(ld.getQm()[3]);
                            dataStats.setQmlu4(ld.getQm()[4]);
                        }
                        dataStats.setQtlu0(dataStats.getQtlu0() + (int)(ld.getQa()[0] * qaT));
                        dataStats.setQtlu1(dataStats.getQtlu1() + (int)(ld.getQa()[1] * qaT));
                        dataStats.setQtlu2(dataStats.getQtlu2() + (int)(ld.getQa()[2] * qaT));
                        dataStats.setQtlu3(dataStats.getQtlu3() + (int)(ld.getQa()[3] * qaT));
                        dataStats.setQtlu4(dataStats.getQtlu4() + (int)(ld.getQa()[4] * qaT));
                    } else {    // straight, right
                        if (dataStats.getQmsrLen() < ld.getQml()) {
                            dataStats.setQmsrLen(ld.getQml());
                            dataStats.setQmsr0(ld.getQm()[0]);
                            dataStats.setQmsr1(ld.getQm()[1]);
                            dataStats.setQmsr2(ld.getQm()[2]);
                            dataStats.setQmsr3(ld.getQm()[3]);
                            dataStats.setQmsr4(ld.getQm()[4]);
                        }
                        dataStats.setQtsr0(dataStats.getQtsr0() + (int)(ld.getQa()[0] * qaT));
                        dataStats.setQtsr1(dataStats.getQtsr1() + (int)(ld.getQa()[1] * qaT));
                        dataStats.setQtsr2(dataStats.getQtsr2() + (int)(ld.getQa()[2] * qaT));
                        dataStats.setQtsr3(dataStats.getQtsr3() + (int)(ld.getQa()[3] * qaT));
                        dataStats.setQtsr4(dataStats.getQtsr4() + (int)(ld.getQa()[4] * qaT));
                    }
                }

                dataStats.setQtT(dataStats.getQtT() + qaT);
                lastT = t;

            } catch (ParseException ignored) {

            }
        }

        if (dataStats != null) {
            return dataStatsRepository.save(dataStats).getId();
        }

        return null;
    }

    // public for unit tests
    public DataStats getInitialDataStats(LocalDateTime t, String c, String i, String r) {

        return DataStats.builder()
                .t(t)
                .c(c)
                .i(i)
                .r(r)
                .p(0)
                .sr0(0)
                .sr1(0)
                .sr2(0)
                .sr3(0)
                .sr4(0)
                .qmsrLen(0)
                .qmsr0(0)
                .qmsr1(0)
                .qmsr2(0)
                .qmsr3(0)
                .qmsr4(0)
                .qtsr0(0)
                .qtsr1(0)
                .qtsr2(0)
                .qtsr3(0)
                .qtsr4(0)
                .lu0(0)
                .lu1(0)
                .lu2(0)
                .lu3(0)
                .lu4(0)
                .qmluLen(0)
                .qmlu0(0)
                .qmlu1(0)
                .qmlu2(0)
                .qmlu3(0)
                .qmlu4(0)
                .qtlu0(0)
                .qtlu1(0)
                .qtlu2(0)
                .qtlu3(0)
                .qtlu4(0)
                .qtT(0)
                .build();
    }
}
