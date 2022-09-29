package com.e4motion.challenge.api.service.impl;

import com.e4motion.challenge.api.domain.Camera;
import com.e4motion.challenge.api.domain.CameraRoad;
import com.e4motion.challenge.api.domain.Intersection;
import com.e4motion.challenge.api.dto.CameraDto;
import com.e4motion.challenge.api.mapper.CameraMapper;
import com.e4motion.challenge.api.repository.CameraRepository;
import com.e4motion.challenge.api.repository.IntersectionRepository;
import com.e4motion.challenge.api.service.CameraService;
import com.e4motion.challenge.common.exception.customexception.CameraDuplicateException;
import com.e4motion.challenge.common.exception.customexception.CameraNotFoundException;
import com.e4motion.challenge.common.exception.customexception.IntersectionNotFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;
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
    private final IntersectionRepository intersectionRepository;
    private final CameraMapper cameraMapper;

    @Transactional
    public CameraDto create(CameraDto cameraDto) {

        checkIfCameraExists(cameraDto.getCameraNo());

        cameraDto.setPassword(passwordEncoder.encode(cameraDto.getPassword()));
        cameraDto.setSettingsUpdated(true);

        Camera camera = cameraMapper.toCamera(cameraDto);
        if (camera.getIntersection() != null) {
            camera.setIntersection(getIntersection(camera.getIntersection().getIntersectionNo()));
        }
        if (camera.getDirection() != null) {
            camera.setDirection(getIntersection(camera.getDirection().getIntersectionNo()));
        }

        if (camera.getRoad() != null) {
            camera.getRoad().setCamera(camera);
        }

        return cameraMapper.toCameraDto(cameraRepository.save(camera));
    }

    @Transactional
    public CameraDto update(String cameraNo, CameraDto cameraDto) {

        return cameraRepository.findByCameraNo(cameraNo)
                .map(camera -> {

                    boolean settingsUpdated = false;

                    if (cameraDto.getCameraNo() != null && !cameraDto.getCameraNo().equals(camera.getCameraNo())) {
                        checkIfCameraExists(cameraDto.getCameraNo());
                        camera.setCameraNo(cameraDto.getCameraNo());
                        settingsUpdated = true;
                    }

                    if (cameraDto.getPassword() != null) {
                        camera.setPassword(passwordEncoder.encode(cameraDto.getPassword()));
                        settingsUpdated = true;
                    }

                    if (cameraDto.getIntersection() != null) {
                        camera.setIntersection(getIntersection(cameraDto.getIntersection().getIntersectionNo()));
                        settingsUpdated = true;
                    }

                    if (cameraDto.getDirection() != null) {
                        camera.setDirection(getIntersection(cameraDto.getDirection().getIntersectionNo()));
                        settingsUpdated = true;
                    }

                    if (cameraDto.getGps() != null &&
                            cameraDto.getGps().getLat() != null && cameraDto.getGps().getLng() != null) {
                        camera.setLat(cameraDto.getGps().getLat());
                        camera.setLng(cameraDto.getGps().getLng());
                        settingsUpdated = true;
                    }

                    if (cameraDto.getDistance() != null) {
                        camera.setDistance(cameraDto.getDistance());
                        settingsUpdated = true;
                    }

                    if (cameraDto.getRtspUrl() != null) {
                        camera.setRtspUrl(cameraDto.getRtspUrl());
                        settingsUpdated = true;
                    }

                    if (cameraDto.getRtspId() != null) {
                        camera.setRtspId(cameraDto.getRtspId());
                        settingsUpdated = true;
                    }

                    if (cameraDto.getRtspPassword() != null) {
                        camera.setRtspPassword(cameraDto.getRtspPassword());
                        settingsUpdated = true;
                    }

                    if (cameraDto.getServerUrl() != null) {
                        camera.setServerUrl(cameraDto.getServerUrl());
                        settingsUpdated = true;
                    }

                    if (cameraDto.getSendCycle() != null) {
                        camera.setSendCycle(cameraDto.getSendCycle());
                        settingsUpdated = true;
                    }

                    if (cameraDto.getCollectCycle() != null) {
                        camera.setCollectCycle(cameraDto.getCollectCycle());
                        settingsUpdated = true;
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

                    if (cameraDto.getLastDataTime() != null) {
                        camera.setLastDataTime(cameraDto.getLastDataTime());
                    }

                    if (cameraDto.getRoad() != null) {
                        try {
                            CameraRoad cameraRoad = cameraMapper.toCameraRoad(cameraDto.getRoad());
                            if (camera.getRoad() != null) {
                                camera.getRoad().setStartLine(cameraRoad.getStartLine());
                                camera.getRoad().setLane(cameraRoad.getLane());
                                camera.getRoad().setUturn(cameraRoad.getUturn());
                                camera.getRoad().setCrosswalk(cameraRoad.getCrosswalk());
                                camera.getRoad().setDirection(cameraRoad.getDirection());
                            } else {
                                cameraRoad.setCamera(camera);
                                camera.setRoad(cameraRoad);
                            }
                            settingsUpdated = true;
                        } catch (JsonProcessingException e) {
                            // Do nothing.
                        }
                    }

                    if (settingsUpdated) {
                        camera.setSettingsUpdated(true);
                    }
                    return cameraMapper.toCameraDto(cameraRepository.saveAndFlush(camera));
                })
                .orElseThrow(() -> new CameraNotFoundException(CameraNotFoundException.INVALID_CAMERA_NO));
    }

    @Transactional
    public void delete(String cameraNo) {

        cameraRepository.deleteByCameraNo(cameraNo);
    }

    @Transactional(readOnly = true)
    public CameraDto get(String cameraNo) {

        return cameraMapper.toCameraDto(cameraRepository.findByCameraNo(cameraNo).orElse(null));
    }

    @Transactional(readOnly = true)
    public List<CameraDto> getList(String regionNo, String intersectionNo) {

        return cameraMapper.toCameraDto(cameraRepository.findAllByRegionNoIntersectionNo(regionNo, intersectionNo));
    }

    private void checkIfCameraExists(String cameraNo) {

        cameraRepository.findByCameraNo(cameraNo)
                .ifPresent(intersection -> {
                    throw new CameraDuplicateException(CameraDuplicateException.CAMERA_NO_ALREADY_EXISTS);
                });
    }

    private Intersection getIntersection(String intersectionNo) {

        return intersectionRepository.findByIntersectionNo(intersectionNo)
                .orElseThrow(() -> new IntersectionNotFoundException(IntersectionNotFoundException.INVALID_INTERSECTION_NO));
    }
}
