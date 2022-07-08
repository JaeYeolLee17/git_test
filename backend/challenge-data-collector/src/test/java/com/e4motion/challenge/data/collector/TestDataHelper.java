package com.e4motion.challenge.data.collector;

import com.e4motion.challenge.data.collector.dto.CameraDataDto;
import com.e4motion.challenge.data.common.dto.LaneDataDto;
import com.e4motion.challenge.data.common.dto.TrafficDataDto;

import java.util.ArrayList;
import java.util.List;

public class TestDataHelper {

    public static CameraDataDto getDataDto() {

        List<LaneDataDto> ld = new ArrayList<>();
        ld.add(LaneDataDto.builder()
                .ln(1)
                .qml(5)
                .qm(new Integer[5])
                .qal(6.3f)
                .qa(new Float[5] )
                .s(new Integer[5])
                .l(new Integer[5])
                .r(new Integer[5])
                .build());

        List<TrafficDataDto> td = new ArrayList<>();
        td.add(TrafficDataDto.builder()
                .st("2022-04-01 11:59:00")
                .et("2022-04-01 12:00:00")
                .p(1)
                .u(new Integer[5])
                .ld(ld)
                .build());

        return CameraDataDto.builder()
                .v("1.0")
                .c("C0001")
                .i("I0001")
                .r("R01")
                .t("2022-04-01 12:00:00")
                .td(td)
                .build();
    }
}
