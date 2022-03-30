package com.e4motion.challenge.api.service.impl;

import com.e4motion.challenge.api.domain.Camera;
import com.e4motion.challenge.api.domain.Road;
import com.e4motion.challenge.api.dto.CameraDto;
import com.e4motion.challenge.api.dto.RoadDto;
import com.e4motion.challenge.api.mapper.CameraMapper;
import com.e4motion.challenge.api.mapper.RoadMapper;
import com.e4motion.challenge.api.repository.CameraRepository;
import com.e4motion.challenge.api.repository.RoadRepository;
import com.e4motion.challenge.api.service.CameraService;
import com.e4motion.challenge.common.exception.customexception.CameraDuplicateException;
import com.e4motion.challenge.common.exception.customexception.CameraNotFoundException;
import com.e4motion.challenge.common.exception.customexception.RoadNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CameraServiceImpl implements CameraService {

    private final CameraRepository cameraRepository;
    private final RoadRepository roadRepository;
    private final PasswordEncoder passwordEncoder;
    private final CameraMapper cameraMapper;
    private final RoadMapper roadMapper;

    @Transactional
    public CameraDto create(CameraDto cameraDto) {

        cameraRepository.findByCameraId(cameraDto.getCameraId())
                .ifPresent(camera -> {
                    throw new CameraDuplicateException(CameraDuplicateException.CAMERA_ID_ALREADY_EXISTS);
                });

        Camera camera = Camera.builder()
                .cameraId(cameraDto.getCameraId())
                .intersectionId(cameraDto.getIntersection().getIntersectionId())
                .gps(cameraDto.getGps())
                .direction(cameraDto.getDirection().getIntersectionId())
                .rtspUrl(cameraDto.getRtspUrl())
                .serverUrl(cameraDto.getServerUrl())
                .collectCycle(cameraDto.getCollectCycle())
                .password(passwordEncoder.encode(cameraDto.getPassword()))
                .rtspId(cameraDto.getRtspId())
                .rtspPassword(passwordEncoder.encode(cameraDto.getRtspPassword()))
                .sendCycle(cameraDto.getSendCycle())
                .distance(cameraDto.getDistance())
                .settingsUpdated(cameraDto.isSettingsUpdated())
                .lastDataTime(cameraDto.getLastDataTime())
                .smallWidth(cameraDto.getSmallWidth())
                .smallHeight(cameraDto.getSmallHeight())
                .largeWidth(cameraDto.getLargeWidth())
                .largeHeight(cameraDto.getLargeHeight())
                .degree(cameraDto.getDegree())
                .road(Road.builder()
                        .cameraId(cameraDto.getRoad().getCameraId())
                        .startLine(cameraDto.getRoad().getStartLine())
                        .uturn(cameraDto.getRoad().getUturn())
                        .crosswalk(cameraDto.getRoad().getCrosswalk())
                        .lane(cameraDto.getRoad().getLane())
                        .direction(cameraDto.getRoad().getDirection())
                        .build())
                .build();

        return cameraMapper.toCameraDto(cameraRepository.save(camera));
    }

    @Transactional
    public CameraDto update(String cameraId, CameraDto cameraDto) {

        return cameraRepository.findByCameraId(cameraId)
                .map(camera -> {
                    if (cameraDto.getIntersection() != null &&
                            cameraDto.getIntersection().getIntersectionId() != null) {
                        camera.setIntersectionId(cameraDto.getIntersection().getIntersectionId());
                    }

                    if (cameraDto.getGps() != null) {
                        camera.setGps(cameraDto.getGps());
                    }

                    if (cameraDto.getDirection() != null &&
                            cameraDto.getDirection().getIntersectionId() != null) {
                        camera.setDirection(cameraDto.getDirection().getIntersectionId());
                    }

                    if (cameraDto.getRtspUrl() != null) {
                        camera.setRtspUrl(cameraDto.getRtspUrl());
                    }

                    if (cameraDto.getServerUrl() != null) {
                        camera.setServerUrl(cameraDto.getServerUrl());
                    }

                    if (cameraDto.getCollectCycle() != null) {
                        camera.setCollectCycle(cameraDto.getCollectCycle());
                    }

                    if (cameraDto.getPassword() != null) {
                        camera.setPassword(passwordEncoder.encode(camera.getPassword()));
                    }

                    if (cameraDto.getRtspId() != null) {
                        camera.setRtspId(cameraDto.getRtspId());
                    }

                    if (cameraDto.getRtspPassword() != null) {
                        camera.setRtspPassword(passwordEncoder.encode(cameraDto.getRtspPassword()));
                    }

                    if (cameraDto.getSendCycle() != null) {
                        camera.setSendCycle(cameraDto.getSendCycle());
                    }

                    if (cameraDto.getDistance() != null) {
                        camera.setDistance(cameraDto.getDistance());
                    }

                    camera.setSettingsUpdated(true);

                    if (cameraDto.getLastDataTime() != null) {
                        camera.setLastDataTime(cameraDto.getLastDataTime());
                    }

                    if (cameraDto.getSmallWidth() != null) {
                        camera.setSmallWidth(cameraDto.getSmallWidth());
                    }

                    if (cameraDto.getSmallHeight() != null) {
                        camera.setSmallHeight(cameraDto.getSmallHeight());
                    }

                    if (cameraDto.getLargeWidth() != null) {
                        camera.setLargeWidth(cameraDto.getLargeWidth());
                    }

                    if (cameraDto.getLargeHeight() != null) {
                        camera.setLargeHeight(cameraDto.getLargeHeight());
                    }

                    if (cameraDto.getDegree() != null) {
                        camera.setDegree(cameraDto.getDegree());
                    }

                    if (cameraDto.getRoad() != null
                            && cameraDto.getRoad().getCameraId() != null
                            && cameraDto.getCameraId() == cameraDto.getRoad().getCameraId()) {

                        if (cameraDto.getRoad().getStartLine() != null) {
                            camera.getRoad().setStartLine(cameraDto.getRoad().getStartLine());
                        }

                        if (cameraDto.getRoad().getUturn() != null) {
                            camera.getRoad().setUturn(cameraDto.getRoad().getUturn());
                        }

                        if (cameraDto.getRoad().getCrosswalk() != null) {
                            camera.getRoad().setCrosswalk(cameraDto.getRoad().getCrosswalk());
                        }

                        if (cameraDto.getRoad().getLane() != null) {
                            camera.getRoad().setLane(cameraDto.getRoad().getLane());
                        }

                        if (cameraDto.getRoad().getDirection() != null) {
                            camera.getRoad().setDirection(cameraDto.getRoad().getDirection());
                        }
                    }

                    return cameraMapper.toCameraDto(cameraRepository.save(camera));
                }).orElseThrow(() -> new CameraNotFoundException(CameraNotFoundException.INVALID_CAMERA_ID));
    }

    @Transactional
    public void delete(String cameraId) {

        cameraRepository.deleteByCameraId(cameraId);
    }

    @Transactional(readOnly = true)
    public CameraDto get(String cameraId) {

        return cameraMapper.toCameraDto(cameraRepository.findByCameraId(cameraId).orElse(null));
    }

    @Transactional(readOnly = true)
    public List<CameraDto> getList() {

        return cameraMapper.toCameraDto(cameraRepository.findAll());
    }
}
