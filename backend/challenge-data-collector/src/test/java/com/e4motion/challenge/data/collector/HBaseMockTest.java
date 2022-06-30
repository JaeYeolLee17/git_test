package com.e4motion.challenge.data.collector;

import org.apache.hadoop.hbase.client.Admin;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.hadoop.hbase.HbaseTemplate;

public class HBaseMockTest {

    @MockBean
    Admin hbaseAdmin;

    @MockBean
    HbaseTemplate hbaseTemplate;

}