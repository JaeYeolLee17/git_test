package com.e4motion.challenge.data.provider.repository.impl;

import com.e4motion.challenge.data.common.dto.LaneDataDto;
import com.e4motion.challenge.data.common.dto.TrafficDataDto;
import com.e4motion.challenge.data.common.repository.HBaseHelper;
import com.e4motion.challenge.data.provider.dto.DataDto;
import com.e4motion.challenge.data.provider.dto.DataListDto;
import com.e4motion.challenge.data.provider.repository.DataRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
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

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

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
    public DataListDto get(HashMap<String, Object> map) {

        Scan scan = getScan(map);
        int limit = scan.getLimit();

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
        if (dataList.size() == limit) {
            nextTime = dataList.get(dataList.size() -1).getRowKey();
            dataList.remove(dataList.size() - 1);
        }

        return DataListDto.builder()
                .data(dataList)
                .nextTime(nextTime)
                .build();
    }

    public void write(HashMap<String, Object> map, Writer writer) throws IOException {

        CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT);
        csvPrinter.printRecord("NO", "V", "C", "I", "R", "T", "ST", "ET", "P", "U", "LN", "QML", "QM", "QAL", "QA", "S", "L", "R");

        Scan scan = getScan(map);
        final int limit = scan.getLimit();

        AtomicInteger i = new AtomicInteger();
        hbaseTemplate.find(HBaseHelper.TABLE_NAME, scan, (Result row, int rowNum) -> {

            if ((i.get() + 1) == limit) {   // the last
                csvPrinter.printRecord(Bytes.toString(row.getRow()));
            } else {
                for (byte[] cf_lane : HBaseHelper.CF_L) {
                    byte[] b = row.getValue(cf_lane, HBaseHelper.Q_LN);
                    if (b != null) {
                        int ln = Bytes.toInt(b);
                        if (ln == 1) {
                            csvPrinter.printRecord(
                                    i.get() + 1,
                                    Bytes.toString(row.getValue(HBaseHelper.CF_D, HBaseHelper.Q_V)),
                                    Bytes.toString(row.getValue(HBaseHelper.CF_D, HBaseHelper.Q_C)),
                                    Bytes.toString(row.getValue(HBaseHelper.CF_D, HBaseHelper.Q_I)),
                                    Bytes.toString(row.getValue(HBaseHelper.CF_D, HBaseHelper.Q_RE)),
                                    Bytes.toString(row.getValue(HBaseHelper.CF_D, HBaseHelper.Q_T)),
                                    Bytes.toString(row.getValue(HBaseHelper.CF_D, HBaseHelper.Q_ST)),
                                    Bytes.toString(row.getValue(HBaseHelper.CF_D, HBaseHelper.Q_ET)),
                                    Bytes.toInt(row.getValue(HBaseHelper.CF_D, HBaseHelper.Q_P)),
                                    Arrays.toString(HBaseHelper.toInts(row.getValue(HBaseHelper.CF_D, HBaseHelper.Q_U))),
                                    ln,
                                    Bytes.toInt(row.getValue(cf_lane, HBaseHelper.Q_QML)),
                                    Arrays.toString(HBaseHelper.toInts(row.getValue(cf_lane, HBaseHelper.Q_QM))),
                                    Bytes.toFloat(row.getValue(cf_lane, HBaseHelper.Q_QAL)),
                                    Arrays.toString(HBaseHelper.toFloats(row.getValue(cf_lane, HBaseHelper.Q_QA))),
                                    Arrays.toString(HBaseHelper.toInts(row.getValue(cf_lane, HBaseHelper.Q_S))),
                                    Arrays.toString(HBaseHelper.toInts(row.getValue(cf_lane, HBaseHelper.Q_L))),
                                    Arrays.toString(HBaseHelper.toInts(row.getValue(cf_lane, HBaseHelper.Q_R))));
                        } else {
                            csvPrinter.printRecord(
                                    i.get() + 1,
                                    null,
                                    null,
                                    null,
                                    null,
                                    null,
                                    null,
                                    null,
                                    null,
                                    null,
                                    ln,
                                    Bytes.toInt(row.getValue(cf_lane, HBaseHelper.Q_QML)),
                                    Arrays.toString(HBaseHelper.toInts(row.getValue(cf_lane, HBaseHelper.Q_QM))),
                                    Bytes.toFloat(row.getValue(cf_lane, HBaseHelper.Q_QAL)),
                                    Arrays.toString(HBaseHelper.toFloats(row.getValue(cf_lane, HBaseHelper.Q_QA))),
                                    Arrays.toString(HBaseHelper.toInts(row.getValue(cf_lane, HBaseHelper.Q_S))),
                                    Arrays.toString(HBaseHelper.toInts(row.getValue(cf_lane, HBaseHelper.Q_L))),
                                    Arrays.toString(HBaseHelper.toInts(row.getValue(cf_lane, HBaseHelper.Q_R))));
                        }
                    }
                }
            }
            i.getAndIncrement();
            return null;
        });
    }

    private Scan getScan(HashMap<String, Object> map) {

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
        return scan;
    }
}
