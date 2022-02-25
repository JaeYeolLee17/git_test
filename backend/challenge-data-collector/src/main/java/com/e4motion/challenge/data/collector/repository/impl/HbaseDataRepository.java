package com.e4motion.challenge.data.collector.repository.impl;

import com.e4motion.challenge.data.common.HBaseHelper;
import com.e4motion.challenge.data.collector.dto.CameraDataDto;
import com.e4motion.challenge.data.common.dto.LaneDataDto;
import com.e4motion.challenge.data.common.dto.TrafficDataDto;
import com.e4motion.challenge.data.collector.repository.DataRepository;
import com.e4motion.common.exception.customexception.InvalidParamException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.data.hadoop.hbase.HbaseTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Repository
public class HbaseDataRepository implements DataRepository {

    private final HbaseTemplate hbaseTemplate;

    @Override
    public void insert(CameraDataDto cameraDataDto) {

        List<Put> puts = new ArrayList<>();
        try {
            for (TrafficDataDto td : cameraDataDto.getTd()) {
                byte[] rowKey = HBaseHelper.getRowKey(td.getSt(), cameraDataDto.getC(), cameraDataDto.getI(), cameraDataDto.getR());
                Put put = new Put(rowKey)
                        .addColumn(HBaseHelper.CF_D, HBaseHelper.Q_V, Bytes.toBytes(cameraDataDto.getV()))
                        .addColumn(HBaseHelper.CF_D, HBaseHelper.Q_C, Bytes.toBytes(cameraDataDto.getC()))
                        .addColumn(HBaseHelper.CF_D, HBaseHelper.Q_I, Bytes.toBytes(cameraDataDto.getI()))
                        .addColumn(HBaseHelper.CF_D, HBaseHelper.Q_RE, Bytes.toBytes(cameraDataDto.getR()))
                        .addColumn(HBaseHelper.CF_D, HBaseHelper.Q_T, Bytes.toBytes(cameraDataDto.getT()))
                        .addColumn(HBaseHelper.CF_D, HBaseHelper.Q_ST, Bytes.toBytes(td.getSt()))
                        .addColumn(HBaseHelper.CF_D, HBaseHelper.Q_ET, Bytes.toBytes(td.getEt()))
                        .addColumn(HBaseHelper.CF_D, HBaseHelper.Q_P, Bytes.toBytes(td.getP()))
                        .addColumn(HBaseHelper.CF_D, HBaseHelper.Q_U, HBaseHelper.toBytes(td.getU()));

                for (LaneDataDto ld : td.getLd()) {
                    int ln = ld.getLn();
                    if (ln < HBaseHelper.MAX_LANES) {
                        byte[] cf_lane = HBaseHelper.CF_L.get(ln);
                        put.addColumn(cf_lane, HBaseHelper.Q_LN, Bytes.toBytes(ln))
                                .addColumn(cf_lane, HBaseHelper.Q_QML, Bytes.toBytes(ld.getQml()))
                                .addColumn(cf_lane, HBaseHelper.Q_QM, HBaseHelper.toBytes(ld.getQm()))
                                .addColumn(cf_lane, HBaseHelper.Q_QAL, Bytes.toBytes(ld.getQal()))
                                .addColumn(cf_lane, HBaseHelper.Q_QA, HBaseHelper.toBytes(ld.getQa()))
                                .addColumn(cf_lane, HBaseHelper.Q_S, HBaseHelper.toBytes(ld.getS()))
                                .addColumn(cf_lane, HBaseHelper.Q_L, HBaseHelper.toBytes(ld.getL()))
                                .addColumn(cf_lane, HBaseHelper.Q_R, HBaseHelper.toBytes(ld.getR()));
                    }
                }
                puts.add(put);
            }
        } catch (Exception ex) {
            throw new InvalidParamException(InvalidParamException.INVALID_DATA);
        }

        hbaseTemplate.execute(HBaseHelper.TABLE_NAME, table -> {
            table.put(puts);
            return null;
        });
    }

}
