package com.e4motion.challenge.data.collector.repository.impl;

import com.e4motion.challenge.data.collector.dto.DataDto;
import com.e4motion.challenge.data.collector.dto.LaneDataDto;
import com.e4motion.challenge.data.collector.dto.TrafficDataDto;
import com.e4motion.challenge.data.collector.repository.DataRepository;
import com.e4motion.common.exception.customexception.InvalidParamException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.filter.RowFilter;
import org.apache.hadoop.hbase.filter.SubstringComparator;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.hadoop.hbase.HbaseTemplate;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Repository
public class HbaseDataRepository implements DataRepository, InitializingBean {

    private static final String TABLE_NAME = "traffic_data_2";
    private static final String ROWKEY_DELIMITER = " ";
    private static final int MAX_LANES = 10;

    private static final byte[] CF_D = Bytes.toBytes("d");
    private static final ArrayList<byte[]> CF_L = new ArrayList<>();

    private static final byte[] Q_V = Bytes.toBytes("v");
    private static final byte[] Q_C = Bytes.toBytes("c");
    private static final byte[] Q_I = Bytes.toBytes("i");
    private static final byte[] Q_RE = Bytes.toBytes("r");
    private static final byte[] Q_T = Bytes.toBytes("t");
    private static final byte[] Q_ST = Bytes.toBytes("st");
    private static final byte[] Q_ET = Bytes.toBytes("et");
    private static final byte[] Q_P = Bytes.toBytes("p");
    private static final byte[] Q_U = Bytes.toBytes("u");

    private static final byte[] Q_LN = Bytes.toBytes("ln");
    private static final byte[] Q_QML = Bytes.toBytes("qml");
    private static final byte[] Q_QM = Bytes.toBytes("qm");
    private static final byte[] Q_QAL = Bytes.toBytes("qal");
    private static final byte[] Q_QA = Bytes.toBytes("qa");
    private static final byte[] Q_S = Bytes.toBytes("s");
    private static final byte[] Q_L = Bytes.toBytes("l");
    private static final byte[] Q_R = Bytes.toBytes("r");

    private final HbaseTemplate hbaseTemplate;
    private final Admin hbaseAdmin;

    @Override
    public void insert(DataDto data) {

        List<Put> puts = new ArrayList<>();
        try {
            for (TrafficDataDto td : data.getTd()) {
                byte[] rowKey = getRowKey(data, td);
                Put put = new Put(rowKey)
                        .addColumn(CF_D, Q_V, Bytes.toBytes(data.getV()))
                        .addColumn(CF_D, Q_C, Bytes.toBytes(data.getC()))
                        .addColumn(CF_D, Q_I, Bytes.toBytes(data.getI()))
                        .addColumn(CF_D, Q_RE, Bytes.toBytes(data.getR()))
                        .addColumn(CF_D, Q_T, Bytes.toBytes(data.getT()))
                        .addColumn(CF_D, Q_ST, Bytes.toBytes(td.getSt()))
                        .addColumn(CF_D, Q_ET, Bytes.toBytes(td.getEt()))
                        .addColumn(CF_D, Q_P, Bytes.toBytes(td.getP()))
                        .addColumn(CF_D, Q_U, toBytes(td.getU()));

                for (LaneDataDto ld : td.getLd()) {
                    int ln = ld.getLn();
                    if (ln < MAX_LANES) {
                        byte[] cf_lane = CF_L.get(ln);
                        put.addColumn(cf_lane, Q_LN, Bytes.toBytes(ln))
                                .addColumn(cf_lane, Q_QML, Bytes.toBytes(ld.getQml()))
                                .addColumn(cf_lane, Q_QM, toBytes(ld.getQm()))
                                .addColumn(cf_lane, Q_QAL, Bytes.toBytes(ld.getQal()))
                                .addColumn(cf_lane, Q_QA, toBytes(ld.getQa()))
                                .addColumn(cf_lane, Q_S, toBytes(ld.getS()))
                                .addColumn(cf_lane, Q_L, toBytes(ld.getL()))
                                .addColumn(cf_lane, Q_R, toBytes(ld.getR()));
                    }
                }
                puts.add(put);
            }
        } catch (Exception ex) {
            throw new InvalidParamException(InvalidParamException.INVALID_DATA);
        }

        hbaseTemplate.execute(TABLE_NAME, table -> {
            table.put(puts);
            return null;
        });
    }

    @Override
    public List<DataDto> query(HashMap<String, Object> map) {
        String startTime = "2022-02-22 14:00:00";
        String endTime = "2022-02-22 19:00:00";
        String filterBy = "camera";
        String filterId = "C0001";
        int limit = 100;

        Scan scan = new Scan();
        scan.setLimit(limit);
        scan.withStartRow(Bytes.toBytes(startTime));
        scan.withStopRow(Bytes.toBytes(endTime));
        if (filterBy != null) {
            scan.setFilter(new RowFilter(CompareFilter.CompareOp.EQUAL, new SubstringComparator(filterId)));
        }

        return hbaseTemplate.find(TABLE_NAME, scan, (Result r, int row) -> {
            log.debug(r.toString());
            return null;
        });
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        for (int i = 0; i < MAX_LANES; i++) {
            CF_L.add(Bytes.toBytes("l" + i));
        }
        createTable();
    }

    private void createTable() throws IOException {

        TableName tableName = TableName.valueOf(TABLE_NAME);
        if(!hbaseAdmin.tableExists(tableName)) {
            HTableDescriptor tableDescriptor = new HTableDescriptor(tableName);
            HColumnDescriptor columnDescriptor = new HColumnDescriptor(CF_D);
            tableDescriptor.addFamily(columnDescriptor);
            for (byte[] cf : CF_L) {
                columnDescriptor = new HColumnDescriptor(cf);
                tableDescriptor.addFamily(columnDescriptor);
            }
            hbaseAdmin.createTable(tableDescriptor);
        }
    }

    private byte[] getRowKey(DataDto data, TrafficDataDto td) {
        return Bytes.toBytes(td.getSt() +
                ROWKEY_DELIMITER +
                data.getC() +
                ROWKEY_DELIMITER +
                data.getI() +
                ROWKEY_DELIMITER +
                data.getR());
    }

    private byte[] toBytes(Integer[] data) {
        return toBytes(Arrays.stream(data).mapToInt(Integer::intValue).toArray());
    }

    private byte[] toBytes(Float[] data) {
        return toBytes(Arrays.stream(data).map(v -> (int)(v * 100)).toArray(Integer[]::new));
    }

    private byte[] toBytes(int[] data) {
        ByteBuffer bBuf = ByteBuffer.allocate(data.length * 4);
        IntBuffer iBuf = bBuf.asIntBuffer();
        iBuf.put(data);
        return bBuf.array();
    }
}
