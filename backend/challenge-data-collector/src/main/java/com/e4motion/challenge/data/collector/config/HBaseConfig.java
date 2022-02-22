package com.e4motion.challenge.data.collector.config;

import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.hadoop.hbase.HbaseTemplate;

import java.io.IOException;

import static org.apache.hadoop.hbase.HConstants.ZOOKEEPER_CLIENT_PORT;
import static org.apache.hadoop.hbase.HConstants.ZOOKEEPER_QUORUM;

@Configuration
public class HBaseConfig {

    @Value("${hbase.zookeeper.quorum}")
    private String zkQuorum;

    @Value("${hbase.zookeeper.property.clientPort}")
    private String zkPort;

    @Bean
    public org.apache.hadoop.conf.Configuration hBaseConfiguration() {
        org.apache.hadoop.conf.Configuration configuration = org.apache.hadoop.hbase.HBaseConfiguration.create();
        configuration.set(ZOOKEEPER_QUORUM, zkQuorum);
        configuration.set(ZOOKEEPER_CLIENT_PORT, zkPort);
        return configuration;
    }

    @Bean
    public HbaseTemplate hbaseTemplate(org.apache.hadoop.conf.Configuration configuration) {
        return new HbaseTemplate(configuration);
    }

    @Bean
    public Admin hbaseAdmin(org.apache.hadoop.conf.Configuration configuration) throws IOException {
        Connection connection = ConnectionFactory.createConnection(configuration);
        return connection.getAdmin();
    }
}
