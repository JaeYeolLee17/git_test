package com.e4motion.challenge.api.service.impl;

import com.e4motion.challenge.api.dto.TsiBrokerInfo;
import com.e4motion.challenge.api.dto.TsiBrokerInfoResponse;
import com.e4motion.challenge.api.dto.TsiNodeInfo;
import com.e4motion.challenge.api.dto.TsiNodeInfoResponse;
import com.e4motion.challenge.api.service.TsiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class TsiServiceImpl implements TsiService {

    private final String RESULT_SUCCESS = "1000";

    private final RestTemplate restTemplate;

    @Value("${tsihub.node-info.url}")
    String nodeInfoUrl;

    @Value("${tsihub.broker-info.url}")
    String brokerInfoUrl;

    @Value("${tsihub.api-token}")
    String apiToken;

    @Transactional
    public void insert() {

    }

    @Transactional(readOnly = true)
    public List<Object> getList() {

        return new ArrayList<>();
    }

    @Transactional
    public void insertNodeInfos(List<TsiNodeInfo> nodeInfo) {

    }

    public List<TsiNodeInfo> getNodeInfos() {

        List<TsiNodeInfo> nodeInfos = null;
        try {
            ResponseEntity<TsiNodeInfoResponse> entity = restTemplate.exchange(nodeInfoUrl + apiToken,
                    HttpMethod.POST, null, TsiNodeInfoResponse.class);
            if (entity.getStatusCode() == HttpStatus.OK) {
                TsiNodeInfoResponse response = entity.getBody();
                if (RESULT_SUCCESS.equals(response.getResultCode())) {
                    log.debug(response.getResultData().toString());
                    nodeInfos = response.getResultData();
                } else {
                    log.info("getNodeInfos " + response.getResultCode() + ", " + response.getResultDesc());
                }
            }
        } catch (Exception e) {
            log.info(e.toString());
        }

        return nodeInfos;
    }

    public TsiBrokerInfo getBrokerInfo() {

        TsiBrokerInfo brokerInfo = null;
        try {
            ResponseEntity<TsiBrokerInfoResponse> entity = restTemplate.exchange(brokerInfoUrl + apiToken,
                    HttpMethod.POST, null, TsiBrokerInfoResponse.class);
            if (entity.getStatusCode() == HttpStatus.OK) {
                TsiBrokerInfoResponse response = entity.getBody();
                if (RESULT_SUCCESS.equals(response.getResultCode())) {
                    log.debug(response.getResultData().toString());
                    brokerInfo = response.getResultData();
                } else {
                    log.info("getBrokerInfo : " + response.getResultCode() + ", " + response.getResultDesc());
                }
            }
        } catch (Exception e) {
            log.info(e.toString());
        }

        return brokerInfo;
    }
}
