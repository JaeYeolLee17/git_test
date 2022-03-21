package com.e4motion.challenge.api.service.impl;

import com.e4motion.challenge.api.domain.Camera;
import com.e4motion.challenge.api.dto.CameraDto;
import com.e4motion.challenge.api.mapper.CameraMapper;
import com.e4motion.challenge.api.repository.CameraRepository;
import com.e4motion.challenge.api.service.CameraService;
import com.e4motion.challenge.common.exception.customexception.CameraDuplicateException;
import com.e4motion.challenge.common.exception.customexception.CameraNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CameraServiceImpl implements CameraService {

    private final CameraRepository cameraRepository;
    private final PasswordEncoder passwordEncoder;
    private final CameraMapper cameraMapper;

    @Transactional
    public CameraDto create(CameraDto cameraDto) {

        cameraRepository.findByCameraId(cameraDto.getCameraId())
                .ifPresent(camera -> {
                    throw new CameraDuplicateException(CameraDuplicateException.CAMERA_ID_ALREADY_EXISTS);
                });

        Camera camera = Camera.builder()
                .cameraId(cameraDto.getCameraId())
                .intersectionId(cameraDto.getIntersection().getIntersectionId())
                .lat(cameraDto.getGps().getLat())
                .lng(cameraDto.getGps().getLng())
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

                    if (cameraDto.getGps() != null && cameraDto.getGps().getLat() != 0) {
                        camera.setLat(cameraDto.getGps().getLat());
                    }

                    if (cameraDto.getGps() != null && cameraDto.getGps().getLng() != 0) {
                        camera.setLng(cameraDto.getGps().getLng());
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

                    //TODO::cameraDto - road ??

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
