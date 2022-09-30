package com.e4motion.challenge.api.service;

import com.e4motion.challenge.api.dto.TsiBrokerDto;
import com.e4motion.challenge.api.dto.TsiNodeDto;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Profile("!test")
@Component
public class TsiHubRunner implements ApplicationRunner {

    private final TsiHubService tsiHubService;
    private final TsiHubConsumer tsiKafkaConsumer;
    private final TsiService tsiService;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        // TODO: 최소 실행 뿐 아니라 주기적으로 노드정보를 확인하고, 다시 Consumer 를 돌려야 함.
        // TODO: 예비사업 운영서버와의 충돌로 인해 일시적으로 막아둠.
        //startTsiHub();
    }

    private void startTsiHub() throws Exception {
        List<TsiNodeDto> nodeDtos = tsiHubService.getNodeInfo();
        if (nodeDtos == null) {
            return;
        }

        tsiService.insertNodeInfo(nodeDtos);

        TsiBrokerDto brokerDto = tsiHubService.getBrokerInfo();
        if (brokerDto == null) {
            return;
        }

        tsiKafkaConsumer.start(brokerDto, nodeDtos);
    }
}
