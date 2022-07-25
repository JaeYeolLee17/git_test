package com.e4motion.challenge.api.service;

import com.e4motion.challenge.api.dto.TsiBrokerInfo;
import com.e4motion.challenge.api.dto.TsiNodeInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class TsiRunner implements ApplicationRunner {

    private final TsiService tsiService;
    private final TsiKafkaConsumer tsiKafkaConsumer;

    @Override
    public void run(ApplicationArguments args) throws Exception {

//        List<TsiNodeInfo> nodeInfo = tsiService.getNodeInfos();
//        if (nodeInfo == null) {
//            return;
//        }
//
//        tsiService.insertNodeInfos(nodeInfo);
//
//        TsiBrokerInfo brokerInfo = tsiService.getBrokerInfo();
//        if (brokerInfo == null) {
//            return;
//        }
//
//        tsiKafkaConsumer.start(brokerInfo, nodeInfo);
    }
}
