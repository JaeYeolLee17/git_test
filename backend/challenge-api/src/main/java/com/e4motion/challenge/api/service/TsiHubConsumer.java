package com.e4motion.challenge.api.service;

import com.e4motion.challenge.api.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerEndpoint;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.config.MethodKafkaListenerEndpoint;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.messaging.handler.annotation.support.DefaultMessageHandlerMethodFactory;
import org.springframework.stereotype.Service;

import java.nio.ByteBuffer;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TimeZone;

@Slf4j
@RequiredArgsConstructor
@Service
public class TsiHubConsumer {

    private final static int CONSUMER_ID = 0;

    private final KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;
    private final KafkaListenerContainerFactory<?> kafkaListenerContainerFactory;
    private final KafkaProperties kafkaProperties;
    private final TsiService tsiService;
    private final ApplicationEventPublisher applicationEventPublisher;

    public void start(TsiBrokerDto brokerDto, List<TsiNodeDto> nodeDtos) throws NoSuchMethodException {

        stop();
        kafkaProperties.getConsumer().getBootstrapServers().clear();
        kafkaProperties.getConsumer().getBootstrapServers().add(getBootStrapServer(brokerDto));

        KafkaListenerEndpoint kafkaListenerEndpoint = createKafkaListenerEndpoint(getTopics(nodeDtos));
        kafkaListenerEndpointRegistry.registerListenerContainer(
                kafkaListenerEndpoint,
                kafkaListenerContainerFactory,
                true);
    }

    public void stop() {

        MessageListenerContainer listenerContainer = kafkaListenerEndpointRegistry.getListenerContainer(getConsumerId());
        if (listenerContainer != null) {
            listenerContainer.stop();
        }
    }

    private String getBootStrapServer(TsiBrokerDto brokerDto) {

        return brokerDto.getHost() + ":" + brokerDto.getPort();
    }

    private String[] getTopics(List<TsiNodeDto> nodeDtos) {

        return nodeDtos.stream()
                .map(tsiNodeDto -> String.valueOf(tsiNodeDto.getNode_id()))
                .toArray(String[]::new);
    }

    private String getConsumerId() {
        return kafkaProperties.getConsumer().getClientId() + "-" + CONSUMER_ID;
    }

    private KafkaListenerEndpoint createKafkaListenerEndpoint(String... topics) throws NoSuchMethodException {

        MethodKafkaListenerEndpoint<String, String> kafkaListenerEndpoint = createDefaultMethodKafkaListenerEndpoint(topics);
        kafkaListenerEndpoint.setBean(new KafkaMessageListener());
        kafkaListenerEndpoint.setMethod(KafkaMessageListener.class.getMethod("onMessage", ConsumerRecord.class));

        return kafkaListenerEndpoint;
    }

    private MethodKafkaListenerEndpoint<String, String> createDefaultMethodKafkaListenerEndpoint(String... topics) {

        MethodKafkaListenerEndpoint<String, String> kafkaListenerEndpoint = new MethodKafkaListenerEndpoint<>();
        kafkaListenerEndpoint.setId(getConsumerId());
        kafkaListenerEndpoint.setGroupId(kafkaProperties.getConsumer().getGroupId());
        kafkaListenerEndpoint.setAutoStartup(true);
        kafkaListenerEndpoint.setTopics(topics);
        kafkaListenerEndpoint.setClientIdPrefix(kafkaProperties.getConsumer().getClientId());
        kafkaListenerEndpoint.setMessageHandlerMethodFactory(new DefaultMessageHandlerMethodFactory());

        return kafkaListenerEndpoint;
    }

    private class KafkaMessageListener implements MessageListener<String, byte[]> {

        @Override
        public void onMessage(ConsumerRecord<String, byte[]> record) {

            TsiHubDto tsiHubDto = parseTsi(record.value());
            if (tsiHubDto != null) {
                if (tsiService.upsert(tsiHubDto)) {
                    applicationEventPublisher.publishEvent(tsiHubDto.getNodeId());
                }
            }
        }
    }

    // protected for unit tests.
    protected TsiHubDto parseTsi(byte[] data) {

        byte[] b0_3 = Arrays.copyOfRange(data, 0, 4);
        long nodeId = ByteBuffer.wrap(b0_3).getInt();

        byte b4 = data[4];
        Boolean manual = (b4 & 0x1) > 0;
        Boolean flashing = (b4 & 0x2) > 0;
        Boolean lightsOut = (b4 & 0x4) > 0;
        Boolean response = (b4 & 0x8) > 0;
        Boolean transition = (b4 & 0x10) > 0;

        byte b5 = data[5];
        Boolean errorContradiction = (b5 & 0x1) > 0;
        Boolean errorCenter = (b5 & 0x2) > 0;
        Boolean errorScu = (b5 & 0x4) > 0;

        byte b6 = data[6];
        Integer cycleCount = b6 & 0xFF;

        byte b7 = data[7];
        int signalCount = b7 & 0x7F;

        byte[] b8_11 = Arrays.copyOfRange(data, 8, 12);
        long time = ByteBuffer.wrap(b8_11).getInt();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(
                Instant.ofEpochSecond(time),
                TimeZone.getDefault().toZoneId());

        TsiHubDto tsiHubDto = TsiHubDto.builder()
                .nodeId(nodeId)
                .manual(manual)
                .flashing(flashing)
                .lightsOut(lightsOut)
                .response(response)
                .transition(transition)
                .errorContradiction(errorContradiction)
                .errorCenter(errorCenter)
                .errorScu(errorScu)
                .cycleCounter(cycleCount)
                .signalCount(signalCount)
                .time(localDateTime)
                .tsiSignals(new ArrayList<>())
                .build();

        int index = 12;
        for (int i = 0; i < signalCount; i++) {

            byte b0 = data[index++];
            int info = (b0 & 0xF0) >> 4;

            byte b1 = data[index++];
            int timeReliability = (b1 & 0x80) >> 7;
            Boolean person = (b1 & 0x40) > 0;
            int status = b1 & 0x07;

            byte b2 = data[index++];
            Integer displayTime = b2 & 0xFF;

            byte b3 = data[index++];
            Integer remainTime = b3 & 0xFF;

            byte bLast = data[index++];
            Integer direction = bLast & 0xFF;

            if (info >= TsiSignalInfo.values().length ||
                    timeReliability >= TsiTimeReliability.values().length ||
                    status >= TsiSignalStatus.values().length ||
                    direction.equals(0)) {
                return null;
            }

            tsiHubDto.getTsiSignals().add(
                    TsiHubSignalDto.builder()
                            .info(TsiSignalInfo.values()[info])
                            .timeReliability(TsiTimeReliability.values()[timeReliability])
                            .person(person)
                            .status(TsiSignalStatus.values()[status])
                            .displayTime(displayTime)
                            .remainTime(remainTime)
                            .direction(direction)
                            .build()
            );
        }
        return tsiHubDto;
    }
}
