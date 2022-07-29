package com.e4motion.challenge.api.service;

import com.e4motion.challenge.api.dto.TsiBrokerDto;
import com.e4motion.challenge.api.dto.TsiNodeDto;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class TsiHubRunner implements ApplicationRunner {

    private final TsiService tsiService;
    private final TsiHubConsumer tsiKafkaConsumer;

    // TODO: 최소 실행 뿐 아니라 주기적으로 노드정보를 확인하고, 다시 Consumer 를 돌려야 하지 않을까?

    @Override
    public void run(ApplicationArguments args) throws Exception {

        List<TsiNodeDto> nodeDtos = tsiService.getNodeInfo();
        if (nodeDtos == null) {
            return;
        }

        tsiService.insertNodeInfo(nodeDtos);

        TsiBrokerDto brokerDto = tsiService.getBrokerInfo();
        if (brokerDto == null) {
            return;
        }

        tsiKafkaConsumer.start(brokerDto, nodeDtos);
    }
}
