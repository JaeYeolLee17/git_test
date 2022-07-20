package com.e4motion.challenge.data.provider;

import com.e4motion.challenge.data.common.dto.LaneDataDto;
import com.e4motion.challenge.data.common.dto.TrafficDataDto;
import com.e4motion.challenge.data.provider.dto.DataDto;
import com.e4motion.challenge.data.provider.dto.DataListDto;

import java.util.ArrayList;
import java.util.List;

public class TestDataHelper {

    public static DataListDto getDataListDto() {

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

        TrafficDataDto td = TrafficDataDto.builder()
                .st("2022-04-01 11:59:00")
                .et("2022-04-01 12:00:00")
                .p(1)
                .u(new Integer[5])
                .ld(ld)
                .build();

        List<DataDto> dataDto = new ArrayList<>();
        dataDto.add(DataDto.builder()
                .v("1.0")
                .c("C0001")
                .i("I0001")
                .r("R01")
                .t("2022-04-01 12:00:00")
                .td(td)
                .build());
        dataDto.add(DataDto.builder()
                .v("1.0")
                .c("C0001")
                .i("I0001")
                .r("R01")
                .t("2022-04-01 12:01:00")
                .td(td)
                .build());

        return DataListDto.builder()
                .nextTime("2022-04-01 12:00:00 C0001 I0001 R01")
                .data(dataDto)
                .build();
    }
}
