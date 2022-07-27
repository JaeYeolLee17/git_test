package com.e4motion.challenge.api.service;

import com.e4motion.challenge.api.dto.TsiBrokerInfo;
import com.e4motion.challenge.api.dto.TsiNodeInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class TsiKafkaConsumer {

    private final TsiService tsiService;

    public void start(TsiBrokerInfo brokerInfo, List<TsiNodeInfo> nodeInfos) {

        log.debug("TsiKafkaConsumer start..." + brokerInfo + ", " + nodeInfos);
    }

//    @KafkaListener(topics = "exam", groupId = "foo")
//    public void consume(String message) {
//        log.debug(message);
//
//        // TODO: tsiService.insert()
//    }
}
