package com.e4motion.challenge.api.service.impl;

import com.e4motion.challenge.api.domain.Tsi;
import com.e4motion.challenge.api.domain.TsiNode;
import com.e4motion.challenge.api.domain.TsiSignal;
import com.e4motion.challenge.api.dto.TsiDto;
import com.e4motion.challenge.api.dto.TsiHubDto;
import com.e4motion.challenge.api.dto.TsiHubSignalDto;
import com.e4motion.challenge.api.dto.TsiNodeDto;
import com.e4motion.challenge.api.mapper.TsiMapper;
import com.e4motion.challenge.api.repository.TsiNodeRepository;
import com.e4motion.challenge.api.repository.TsiRepository;
import com.e4motion.challenge.api.repository.TsiSignalRepository;
import com.e4motion.challenge.api.service.TsiService;
import com.e4motion.challenge.common.domain.TsiFilterBy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class TsiServiceImpl implements TsiService {

    private final TsiRepository tsiRepository;
    private final TsiSignalRepository tsiSignalRepository;
    private final TsiMapper tsiMapper;
    private final TsiNodeRepository tsiNodeRepository;

    @Transactional
    public boolean upsert(TsiHubDto tsiHubDto) {

        Optional<Tsi> tsiOptional = tsiRepository.findByNodeId(tsiHubDto.getNodeId());
        if (tsiOptional.isPresent()) {
            return update(tsiOptional.get(), tsiHubDto);
        } else {
            insert(tsiHubDto);
            return true;
        }
    }

    @Transactional(readOnly = true)
    public List<TsiDto> getList(TsiFilterBy filterBy, String filterValue) {

        return tsiRepository.getList(filterBy, filterValue);
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

    private void insert(TsiHubDto tsiHubDto) {

        Tsi tsi = tsiMapper.toTsi(tsiHubDto);
        for (TsiSignal tsiSignal : tsi.getTsiSignals()) {
            tsiSignal.setTsi(tsi);
        }

        tsiRepository.save(tsi);
        tsiSignalRepository.saveAll(tsi.getTsiSignals());
    }

    private boolean update(Tsi tsi, TsiHubDto tsiHubDto) {

        boolean updated = false;

        if (!tsi.getManual().equals(tsiHubDto.getManual()) ||
                !tsi.getFlashing().equals(tsiHubDto.getFlashing()) ||
                !tsi.getLightsOut().equals(tsiHubDto.getLightsOut()) ||
                !tsi.getResponse().equals(tsiHubDto.getResponse()) ||
                !tsi.getTransition().equals(tsiHubDto.getTransition()) ||
                !tsi.getErrorContradiction().equals(tsiHubDto.getErrorContradiction()) ||
                !tsi.getErrorCenter().equals(tsiHubDto.getErrorCenter()) ||
                !tsi.getErrorScu().equals(tsiHubDto.getErrorScu())) {

            tsi.setManual(tsiHubDto.getManual());
            tsi.setFlashing(tsiHubDto.getFlashing());
            tsi.setLightsOut(tsiHubDto.getLightsOut());
            tsi.setResponse(tsiHubDto.getResponse());
            tsi.setTransition(tsiHubDto.getTransition());
            tsi.setErrorContradiction(tsiHubDto.getErrorContradiction());
            tsi.setErrorCenter(tsiHubDto.getErrorCenter());
            tsi.setErrorScu(tsiHubDto.getErrorScu());

            updated = true;
        }

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

                if (!tsiSignal.getTimeReliability().equals(tsiSignalDto.getTimeReliability()) ||
                        !tsiSignal.getPerson().equals(tsiSignalDto.getPerson()) ||
                        !tsiSignal.getStatus().equals(tsiSignalDto.getStatus()) ||
                        !tsiSignal.getDisplayTime().equals(tsiSignalDto.getDisplayTime())) {

                    tsiSignal.setTimeReliability(tsiSignalDto.getTimeReliability());
                    tsiSignal.setPerson(tsiSignalDto.getPerson());
                    tsiSignal.setStatus(tsiSignalDto.getStatus());
                    tsiSignal.setDisplayTime(tsiSignalDto.getDisplayTime());

                    updated = true;
                }

                tsiSignal.setRemainTime(tsiSignalDto.getRemainTime());

            } else {

                tsiSignal = tsiMapper.toTsiSignal(tsiSignalDto);
                tsiSignal.setTsi(tsi);

                updated = true;
            }

            tsiSignalRepository.save(tsiSignal);
        }

        if (tsiSignals.size() > 0) {
            updated = true;
        }
        tsiSignalRepository.deleteAll(tsiSignals);

        return updated;
    }
}
