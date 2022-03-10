package com.e4motion.challenge.data.provider.repository.impl;

import com.e4motion.challenge.data.common.repository.HBaseHelper;
import com.e4motion.challenge.data.common.dto.LaneDataDto;
import com.e4motion.challenge.data.common.dto.TrafficDataDto;
import com.e4motion.challenge.data.provider.dto.DataDto;
import com.e4motion.challenge.data.provider.dto.DataListDto;
import com.e4motion.challenge.data.provider.repository.DataRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.filter.RowFilter;
import org.apache.hadoop.hbase.filter.SubstringComparator;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.hadoop.hbase.HbaseTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Repository
public class HBaseDataRepository implements DataRepository, InitializingBean {

    private final Admin hbaseAdmin;

    private final HbaseTemplate hbaseTemplate;

    @Override
    public void afterPropertiesSet() throws Exception {
        HBaseHelper.createTable(hbaseAdmin);
    }

    @Override
    public DataListDto query(HashMap<String, Object> map) {

        String startTime = (String) map.get("startTime");
        String endTime = (String) map.get("endTime");
        String filterBy = (String) map.get("filterBy");
        String filterId = (String) map.get("filterId");
        int limit = (int) map.get("limit");

        Scan scan = new Scan();
        scan.setLimit(limit + 1);
        scan.withStartRow(Bytes.toBytes(startTime));
        if (endTime != null) {
            scan.withStopRow(Bytes.toBytes(endTime));
        }
        if (filterBy != null) {
            scan.setFilter(new RowFilter(CompareFilter.CompareOp.EQUAL, new SubstringComparator(filterId)));
        }

        List<DataDto> dataList = hbaseTemplate.find(HBaseHelper.TABLE_NAME, scan, (Result row, int rowNum) -> {
            DataDto data = new DataDto();
            data.setRowKey(Bytes.toString(row.getRow()));

            data.setV(Bytes.toString(row.getValue(HBaseHelper.CF_D, HBaseHelper.Q_V)));
            data.setC(Bytes.toString(row.getValue(HBaseHelper.CF_D, HBaseHelper.Q_C)));
            data.setI(Bytes.toString(row.getValue(HBaseHelper.CF_D, HBaseHelper.Q_I)));
            data.setR(Bytes.toString(row.getValue(HBaseHelper.CF_D, HBaseHelper.Q_RE)));
            data.setT(Bytes.toString(row.getValue(HBaseHelper.CF_D, HBaseHelper.Q_T)));

            TrafficDataDto td = new TrafficDataDto();
            td.setSt(Bytes.toString(row.getValue(HBaseHelper.CF_D, HBaseHelper.Q_ST)));
            td.setEt(Bytes.toString(row.getValue(HBaseHelper.CF_D, HBaseHelper.Q_ET)));
            td.setP(Bytes.toInt(row.getValue(HBaseHelper.CF_D, HBaseHelper.Q_P)));
            td.setU(HBaseHelper.toInts(row.getValue(HBaseHelper.CF_D, HBaseHelper.Q_U)));

            ArrayList<LaneDataDto> ld = new ArrayList<>();
            for (byte[] cf_lane : HBaseHelper.CF_L) {
                byte[] b = row.getValue(cf_lane, HBaseHelper.Q_LN);
                if (b != null) {
                    int ln = Bytes.toInt(b);
                    LaneDataDto lane = new LaneDataDto();
                    lane.setLn(ln);
                    lane.setQml(Bytes.toInt(row.getValue(cf_lane, HBaseHelper.Q_QML)));
                    lane.setQm(HBaseHelper.toInts(row.getValue(cf_lane, HBaseHelper.Q_QM)));
                    lane.setQal(Bytes.toFloat(row.getValue(cf_lane, HBaseHelper.Q_QAL)));
                    lane.setQa(HBaseHelper.toFloats(row.getValue(cf_lane, HBaseHelper.Q_QA)));
                    lane.setS(HBaseHelper.toInts(row.getValue(cf_lane, HBaseHelper.Q_S)));
                    lane.setL(HBaseHelper.toInts(row.getValue(cf_lane, HBaseHelper.Q_L)));
                    lane.setR(HBaseHelper.toInts(row.getValue(cf_lane, HBaseHelper.Q_R)));
                    ld.add(lane);
                }
            }
            td.setLd(ld);
            data.setTd(td);
            return data;
        });

        String nextTime = null;
        if (dataList.size() > limit) {
            nextTime = dataList.get(dataList.size() -1).getRowKey();
            dataList.remove(dataList.size() - 1);
        }

        return DataListDto.builder()
                .data(dataList)
                .nextTime(nextTime)
                .build();
    }
}
