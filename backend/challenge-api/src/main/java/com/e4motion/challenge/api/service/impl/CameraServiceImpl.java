package com.e4motion.challenge.api.service.impl;

import com.e4motion.challenge.api.domain.Camera;
import com.e4motion.challenge.api.dto.CameraDto;
import com.e4motion.challenge.api.mapper.CameraMapper;
import com.e4motion.challenge.api.mapper.IntersectionMapper;
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
    private final IntersectionMapper intersectionMapper;

    @Transactional
    public CameraDto create(CameraDto cameraDto) {

        cameraRepository.findByCameraId(cameraDto.getCameraId())
                .ifPresent(camera -> {
                    throw new CameraDuplicateException(CameraDuplicateException.CAMERA_ID_ALREADY_EXISTS);
                });

        Camera camera = Camera.builder()
                .cameraId(cameraDto.getCameraId())
                .password(passwordEncoder.encode(cameraDto.getPassword()))
                .intersection(intersectionMapper.toIntersection(cameraDto.getIntersection()))
                .direction(intersectionMapper.toIntersection(cameraDto.getDirection()))
                .latitude(cameraDto.getLatitude())
                .longitude(cameraDto.getLongitude())
                .rtspUrl(cameraDto.getRtspUrl())
                .serverUrl(cameraDto.getServerUrl())
                .collectCycle(cameraDto.getCollectCycle())
                .rtspId(cameraDto.getRtspId())
                .rtspPassword(passwordEncoder.encode(cameraDto.getRtspPassword()))
                .sendCycle(cameraDto.getSendCycle())
                .distance(cameraDto.getDistance())
                .lastDataTime(cameraDto.getLastDataTime())
                .smallWidth(cameraDto.getSmallWidth())
                .smallHeight(cameraDto.getSmallHeight())
                .largeWidth(cameraDto.getLargeWidth())
                .largeHeight(cameraDto.getLargeHeight())
                .degree(cameraDto.getDegree())
                .settingsUpdated(cameraDto.isSettingsUpdated())
                .road(cameraMapper.toRoad(cameraDto.getRoad()))
                .build();

        return cameraMapper.toCameraDto(cameraRepository.save(camera));
    }

    @Transactional
    public CameraDto update(String cameraId, CameraDto cameraDto) {

        return cameraRepository.findByCameraId(cameraId)
                .map(camera -> {
                    if (cameraDto.getIntersection() != null &&
                            cameraDto.getIntersection().getIntersectionId() != null) {
                        camera.setIntersection(intersectionMapper.toIntersection(cameraDto.getIntersection()));
                    }

                    if (cameraDto.getDirection() != null &&
                            cameraDto.getDirection().getIntersectionId() != null) {
                        camera.setDirection(intersectionMapper.toIntersection(cameraDto.getDirection()));
                    }

                    if (cameraDto.getLatitude() != null) {
                        camera.setLatitude(cameraDto.getLatitude());
                    }

                    if (cameraDto.getLongitude() != null) {
                        camera.setLongitude(cameraDto.getLongitude());
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

                    camera.setSettingsUpdated(true);

                    //TODO : update road ??

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
