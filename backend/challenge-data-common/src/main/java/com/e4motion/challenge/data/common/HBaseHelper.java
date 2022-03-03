package com.e4motion.challenge.data.common;

import com.e4motion.challenge.data.common.dto.TrafficDataDto;
import lombok.RequiredArgsConstructor;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class HBaseHelper implements InitializingBean {

    public static final String TABLE_NAME = "traffic_data_2";

    public static final String ROWKEY_DELIMITER = " ";

    public static final byte[] CF_D = Bytes.toBytes("d");
    public static final ArrayList<byte[]> CF_L = new ArrayList<>();

    public static final byte[] Q_V = Bytes.toBytes("v");
    public static final byte[] Q_C = Bytes.toBytes("c");
    public static final byte[] Q_I = Bytes.toBytes("i");
    public static final byte[] Q_RE = Bytes.toBytes("r");
    public static final byte[] Q_T = Bytes.toBytes("t");
    public static final byte[] Q_ST = Bytes.toBytes("st");
    public static final byte[] Q_ET = Bytes.toBytes("et");
    public static final byte[] Q_P = Bytes.toBytes("p");
    public static final byte[] Q_U = Bytes.toBytes("u");

    public static final byte[] Q_LN = Bytes.toBytes("ln");
    public static final byte[] Q_QML = Bytes.toBytes("qml");
    public static final byte[] Q_QM = Bytes.toBytes("qm");
    public static final byte[] Q_QAL = Bytes.toBytes("qal");
    public static final byte[] Q_QA = Bytes.toBytes("qa");
    public static final byte[] Q_S = Bytes.toBytes("s");
    public static final byte[] Q_L = Bytes.toBytes("l");
    public static final byte[] Q_R = Bytes.toBytes("r");

    private final Admin hbaseAdmin;

    @Override
    public void afterPropertiesSet() throws Exception {
        for (int i = TrafficDataDto.MIN_LANE; i <= TrafficDataDto.MAX_LANE; i++) {
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

    public static byte[] getRowKey(String st, String c, String i, String r) {
        return Bytes.toBytes(st + ROWKEY_DELIMITER + c + ROWKEY_DELIMITER + i + ROWKEY_DELIMITER + r);
    }

    public static byte[] toBytes(Integer[] data) {
        return toBytes(Arrays.stream(data).mapToInt(Integer::intValue).toArray());
    }

    public static byte[] toBytes(Float[] data) {
        return toBytes(Arrays.stream(data).map(v -> (int)(v * 100)).toArray(Integer[]::new));
    }

    public static byte[] toBytes(int[] data) {
        ByteBuffer bBuf = ByteBuffer.allocate(data.length * 4);
        IntBuffer iBuf = bBuf.asIntBuffer();
        iBuf.put(data);
        return bBuf.array();
    }

    public static Integer[] toInts(byte[] data) {
        IntBuffer iBuf = ByteBuffer.wrap(data).asIntBuffer();
        int[] array = new int[iBuf.remaining()];
        iBuf.get(array);
        return Arrays.stream(array).boxed().toArray(Integer[]::new);
    }

    public static Float[] toFloats(byte[] data) {
        Integer[] ints = toInts(data);
        return Arrays.stream(ints).map(v -> v / 100f).toArray(Float[]::new);
    }

}
