package com.e4motion.challenge.data.provider.repository.impl;

import com.e4motion.challenge.data.common.HBaseHelper;
import com.e4motion.challenge.data.provider.dto.DataDto;
import com.e4motion.challenge.data.provider.dto.DataListDto;
import com.e4motion.challenge.data.provider.repository.DataRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.filter.RowFilter;
import org.apache.hadoop.hbase.filter.SubstringComparator;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.data.hadoop.hbase.HbaseTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Repository
public class HbaseDataRepository implements DataRepository {

    private final HbaseTemplate hbaseTemplate;

    @Override
    public DataListDto query(HashMap<String, Object> map) {
        String startTime = "2022-02-22 14:00:00";
        String endTime = "2022-02-25 19:00:00";
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

        List<DataDto> data = hbaseTemplate.find(HBaseHelper.TABLE_NAME, scan, (Result r, int row) -> {
            log.debug(r.toString());
            return null;
        });

        return null;
    }

}
