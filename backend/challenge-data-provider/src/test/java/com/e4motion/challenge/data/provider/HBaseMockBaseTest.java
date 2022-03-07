package com.e4motion.challenge.data.provider;

import org.apache.hadoop.hbase.client.Admin;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.hadoop.hbase.HbaseTemplate;

public class HBaseMockBaseTest {

    @MockBean
    protected Admin hbaseAdmin;

    @MockBean
    protected HbaseTemplate hbaseTemplate;

}