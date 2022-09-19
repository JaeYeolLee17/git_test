package com.e4motion.challenge.api.service.impl;

import com.e4motion.challenge.api.dto.TsiBrokerDto;
import com.e4motion.challenge.api.dto.TsiBrokerResponseDto;
import com.e4motion.challenge.api.dto.TsiNodeDto;
import com.e4motion.challenge.api.dto.TsiNodeResponseDto;
import com.e4motion.challenge.api.service.TsiHubService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class TsiHubServiceImpl implements TsiHubService {

    private final String RESULT_SUCCESS = "1000";

    private final RestTemplate restTemplate;

    @Value("${tsihub.node-info.url}")
    String nodeInfoUrl;

    @Value("${tsihub.broker-info.url}")
    String brokerInfoUrl;

    @Value("${tsihub.api-token}")
    String apiToken;

    public List<TsiNodeDto> getNodeInfo() {

        List<TsiNodeDto> nodeDtos = null;
        try {
            ResponseEntity<TsiNodeResponseDto> entity = restTemplate.exchange(nodeInfoUrl + apiToken,
                    HttpMethod.POST, null, TsiNodeResponseDto.class);
            if (entity.getStatusCode() == HttpStatus.OK) {
                TsiNodeResponseDto response = entity.getBody();
                if (RESULT_SUCCESS.equals(response.getResultCode())) {
                    log.debug(response.getResultData().toString());
                    nodeDtos = response.getResultData();
                } else {
                    log.info("getNodeInfo " + response.getResultCode() + ", " + response.getResultDesc());
                }
            }
        } catch (Exception e) {
            log.info(e.toString());
        }

        return nodeDtos;
    }

    public TsiBrokerDto getBrokerInfo() {

        TsiBrokerDto brokerDto = null;
        try {
            ResponseEntity<TsiBrokerResponseDto> entity = restTemplate.exchange(brokerInfoUrl + apiToken,
                    HttpMethod.POST, null, TsiBrokerResponseDto.class);
            if (entity.getStatusCode() == HttpStatus.OK) {
                TsiBrokerResponseDto response = entity.getBody();
                if (RESULT_SUCCESS.equals(response.getResultCode())) {
                    log.debug(response.getResultData().toString());
                    brokerDto = response.getResultData();
                } else {
                    log.info("getBrokerInfo : " + response.getResultCode() + ", " + response.getResultDesc());
                }
            }
        } catch (Exception e) {
            log.info(e.toString());
        }

        return brokerDto;
    }
}
