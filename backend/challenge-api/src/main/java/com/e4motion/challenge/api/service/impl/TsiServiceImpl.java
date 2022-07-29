package com.e4motion.challenge.api.service.impl;

import com.e4motion.challenge.api.domain.Tsi;
import com.e4motion.challenge.api.domain.TsiNode;
import com.e4motion.challenge.api.domain.TsiSignal;
import com.e4motion.challenge.api.dto.*;
import com.e4motion.challenge.api.mapper.TsiMapper;
import com.e4motion.challenge.api.repository.TsiNodeRepository;
import com.e4motion.challenge.api.repository.TsiRepository;
import com.e4motion.challenge.api.repository.TsiSignalRepository;
import com.e4motion.challenge.api.service.TsiService;
import com.e4motion.challenge.common.domain.TsiFilterBy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class TsiServiceImpl implements TsiService {

    private final String RESULT_SUCCESS = "1000";

    private final TsiRepository tsiRepository;

    private final TsiSignalRepository tsiSignalRepository;

    private final TsiMapper tsiMapper;

    private final TsiNodeRepository tsiNodeRepository;

    private final RestTemplate restTemplate;

    @Value("${tsihub.node-info.url}")
    String nodeInfoUrl;

    @Value("${tsihub.broker-info.url}")
    String brokerInfoUrl;

    @Value("${tsihub.api-token}")
    String apiToken;

    @Transactional
    public void upsertTsi(TsiHubDto tsiHubDto) {

        Optional<Tsi> tsiOptional = tsiRepository.findByNodeId(tsiHubDto.getNodeId());
        if (tsiOptional.isPresent()) {
            updateTsi(tsiOptional.get(), tsiHubDto);
        } else {
            insertTsi(tsiHubDto);
        }
    }

    private void insertTsi(TsiHubDto tsiHubDto) {

        Tsi tsi = tsiMapper.toTsi(tsiHubDto);
        for (TsiSignal tsiSignal : tsi.getTsiSignals()) {
            tsiSignal.setTsi(tsi);
        }

        tsiRepository.save(tsi);
        tsiSignalRepository.saveAll(tsi.getTsiSignals());
    }

    private void updateTsi(Tsi tsi, TsiHubDto tsiHubDto) {

        tsi.setManual(tsiHubDto.getManual());
        tsi.setFlashing(tsiHubDto.getFlashing());
        tsi.setLightsOut(tsiHubDto.getLightsOut());
        tsi.setResponse(tsiHubDto.getResponse());
        tsi.setTransition(tsiHubDto.getTransition());
        tsi.setErrorContradiction(tsiHubDto.getErrorContradiction());
        tsi.setErrorCenter(tsiHubDto.getErrorCenter());
        tsi.setErrorScu(tsiHubDto.getErrorScu());
        tsi.setCycleCounter(tsiHubDto.getCycleCounter());
        tsi.setTime(tsiHubDto.getTime());

        tsiRepository.save(tsi);

        List<TsiSignal> tsiSignals = tsiSignalRepository.findAllByTsi_NodeId(tsi.getNodeId());

        for (TsiHubSignalDto tsiSignalDto : tsiHubDto.getTsiSignals()) {

            TsiSignal tsiSignal;
            Optional<TsiSignal> tsiSignalOptional = tsiSignalRepository.findByTsi_NodeIdAndInfoAndDirection(
                    tsi.getNodeId(), tsiSignalDto.getInfo(), tsiSignalDto.getDirection());
            if (tsiSignalOptional.isPresent()) {

                tsiSignal = tsiSignalOptional.get();
                tsiSignals.remove(tsiSignal);

                tsiSignal.setTimeReliability(tsiSignalDto.getTimeReliability());
                tsiSignal.setPerson(tsiSignalDto.getPerson());
                tsiSignal.setStatus(tsiSignalDto.getStatus());
                tsiSignal.setDisplayTime(tsiSignalDto.getDisplayTime());
                tsiSignal.setRemainTime(tsiSignalDto.getRemainTime());

            } else {

                tsiSignal = tsiMapper.toTsiSignal(tsiSignalDto);
                tsiSignal.setTsi(tsi);
            }

            tsiSignalRepository.save(tsiSignal);
        }

        tsiSignalRepository.deleteAll(tsiSignals);
    }

    @Transactional(readOnly = true)
    public List<TsiDto> getTsiList(TsiFilterBy filterBy, String filterValue) {

        return tsiRepository.getTsiList(filterBy, filterValue);
    }

    @Transactional
    public void insertNodeInfo(List<TsiNodeDto> nodeDtos) {

        List<TsiNode> tisNodes =  nodeDtos.stream()
                .map(nodeDto -> tsiNodeRepository.findByNodeId(nodeDto.getNode_id())
                        .map(node -> {
                            node.setNodeName(nodeDto.getNode_name());
                            node.setLat(nodeDto.getLatitude());
                            node.setLng(nodeDto.getLongitude());
                            return node;
                        })
                        .orElseGet(() -> TsiNode.builder()
                                .nodeId(nodeDto.getNode_id())
                                .nodeName(nodeDto.getNode_name())
                                .lat(nodeDto.getLatitude())
                                .lng(nodeDto.getLongitude())
                                .build()))
                .collect(Collectors.toList());

        tsiNodeRepository.saveAll(tisNodes);
    }

    public List<TsiNodeDto> getNodeInfo() {

        List<TsiNodeDto> nodeDtos = null;
        try {
            ResponseEntity<TsiNodeResponse> entity = restTemplate.exchange(nodeInfoUrl + apiToken,
                    HttpMethod.POST, null, TsiNodeResponse.class);
            if (entity.getStatusCode() == HttpStatus.OK) {
                TsiNodeResponse response = entity.getBody();
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
            ResponseEntity<TsiBrokerResponse> entity = restTemplate.exchange(brokerInfoUrl + apiToken,
                    HttpMethod.POST, null, TsiBrokerResponse.class);
            if (entity.getStatusCode() == HttpStatus.OK) {
                TsiBrokerResponse response = entity.getBody();
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
