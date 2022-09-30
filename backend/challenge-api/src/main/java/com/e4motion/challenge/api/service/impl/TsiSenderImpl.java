package com.e4motion.challenge.api.service.impl;

import com.e4motion.challenge.api.domain.Intersection;
import com.e4motion.challenge.api.dto.TsiDto;
import com.e4motion.challenge.api.repository.IntersectionRepository;
import com.e4motion.challenge.api.repository.TsiRepository;
import com.e4motion.challenge.api.service.TsiSender;
import com.e4motion.challenge.common.constant.TsiFilterBy;
import com.e4motion.challenge.common.exception.customexception.IntersectionNotFoundException;
import com.e4motion.challenge.common.security.SecurityHelper;
import com.e4motion.challenge.common.utils.DateTimeHelper;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class TsiSenderImpl implements TsiSender {

    private final static long SSE_TIMEOUT = 1000*60*5;
    private final static String SSE_NAME = "tsi";
    private final static String SPACE = " ";

    private final IntersectionRepository intersectionRepository;
    private final TsiRepository tsiRepository;
    private final SecurityHelper securityHelper;
    private final ConcurrentHashMap<String, EmitterInfo> emitters = new ConcurrentHashMap<>();

    public SseEmitter subscribe(TsiFilterBy filterBy, String filterValue) {

        EmitterInfo emitterInfo = getEmitterInfo();
        makeNodeIds(emitterInfo, filterBy, filterValue);

        return emitterInfo.emitter;
    }

    public void unsubscribe(String emitterId) {

        removeEmitterInfo(emitterId);
    }

    private EmitterInfo getEmitterInfo() {

        String emitterId = getEmitterId();

        SseEmitter emitter = new SseEmitter(SSE_TIMEOUT);
        emitter.onCompletion(() -> {
            log.debug("emitter.onCompletion() " + emitterId);
            removeEmitterInfo(emitterId);
        });
        emitter.onTimeout(() -> {
            log.debug("emitter.onTimeout() " + emitterId);
            removeEmitterInfo(emitterId);
        });
        emitter.onError(error -> {
            log.debug("emitter.onError() " + emitterId);
            removeEmitterInfo(emitterId);
        });

        EmitterInfo emitterInfo = EmitterInfo.builder()
                .emitterId(emitterId)
                .emitter(emitter)
                .build();

        emitters.put(emitterId, emitterInfo);

        return emitterInfo;
    }

    private void makeNodeIds(EmitterInfo emitterInfo, TsiFilterBy filterBy, String filterValue) {

        if (TsiFilterBy.NODE.equals(filterBy)) {

            emitterInfo.setNodeIds(Collections.singletonList(Long.parseLong(filterValue)));

        } else if (TsiFilterBy.INTERSECTION.equals(filterBy)) {

            Intersection intersection = intersectionRepository.findByIntersectionNo(filterValue)
                    .orElseThrow(() -> new IntersectionNotFoundException(IntersectionNotFoundException.INVALID_INTERSECTION_NO));
            emitterInfo.setNodeIds(Collections.singletonList(intersection.getNationalId()));

        } else if (TsiFilterBy.REGION.equals(filterBy)) {

            List<Intersection> intersections = intersectionRepository.findAllByRegion_RegionNo(filterValue, null);
            emitterInfo.setNodeIds(intersections.stream()
                    .map(Intersection::getNationalId)
                    .collect(Collectors.toList()));

        }
    }

    private void removeEmitterInfo(String emitterId) {
        emitters.remove(emitterId);
    }

    private String getEmitterId() {
        return securityHelper.getLoginUsername() + SPACE + DateTimeHelper.formatLocalDateTime(LocalDateTime.now());
    }

    private String getSseId(String emitterId) {
        return emitterId.split(SPACE)[0] + SPACE + DateTimeHelper.formatLocalDateTime(LocalDateTime.now());
    }

    private void send(String sseId, SseEmitter emitter, TsiDto tsiDto) {

        log.debug("send() " + sseId);
        try {
            emitter.send(SseEmitter.event()
                    .id(sseId)
                    .name(SSE_NAME)
                    .data(tsiDto)
                    .build());
        } catch (Exception e) {
            log.debug("send() " + e);
        }
    }

    @Async
    @EventListener
    private void onTsiEvent(Long nodeId) {

        log.debug("onTsiEvent() " + nodeId);
        tsiRepository.getList(TsiFilterBy.NODE, nodeId.toString()).stream().findFirst()
                .ifPresent(tsiDto -> emitters.forEach((emitterId, emitterInfo) -> {
                    if (emitterInfo.nodeIds == null || emitterInfo.nodeIds.contains(tsiDto.getNodeId())) {
                        send(getSseId(emitterId), emitterInfo.getEmitter(), tsiDto);
                    }
                }));
    }

    @Getter
    @Setter
    @ToString
    @Builder
    private static class EmitterInfo {
        private String emitterId;
        private SseEmitter emitter;
        private List<Long> nodeIds;
    }

}
