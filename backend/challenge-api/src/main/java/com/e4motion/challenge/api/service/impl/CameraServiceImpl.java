package com.e4motion.challenge.api.service.impl;

import com.e4motion.challenge.api.domain.Camera;
import com.e4motion.challenge.api.domain.Intersection;
import com.e4motion.challenge.api.dto.CameraDto;
import com.e4motion.challenge.api.mapper.CameraMapper;
import com.e4motion.challenge.api.repository.CameraRepository;
import com.e4motion.challenge.api.repository.IntersectionRepository;
import com.e4motion.challenge.api.service.CameraService;
import com.e4motion.challenge.common.exception.customexception.CameraDuplicateException;
import com.e4motion.challenge.common.exception.customexception.IntersectionNotFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CameraServiceImpl implements CameraService {

    private final CameraRepository cameraRepository;
    private final PasswordEncoder passwordEncoder;
    private final IntersectionRepository intersectionRepository;
    private final CameraMapper cameraMapper;
    private final EntityManager entityManager;

    @Transactional
    public CameraDto create(CameraDto cameraDto) {    // TODO: test create, update...

        cameraRepository.findByCameraNo(cameraDto.getCameraNo())
                .ifPresent(intersection -> {
                    throw new CameraDuplicateException(CameraDuplicateException.CAMERA_ID_ALREADY_EXISTS);
                });

        cameraDto.setPassword(passwordEncoder.encode(cameraDto.getPassword()));

        Camera camera = cameraMapper.toCamera(cameraDto);
        if (camera.getIntersection() != null) {
            camera.setIntersection(getIntersection(camera.getIntersection().getIntersectionNo()));
        }
        if (camera.getDirection() != null) {
            camera.setDirection(getIntersection(camera.getDirection().getIntersectionNo()));
        }

        Camera saved = cameraRepository.save(camera);
        entityManager.flush();

        return cameraMapper.toCameraDto(saved);
    }

    @Transactional
    public CameraDto update(String cameraNo, CameraDto cameraDto) {

        return cameraRepository.findByCameraNo(cameraNo)
                .map(camera -> {

                    if (cameraDto.getCameraNo() != null) {
                        camera.setCameraNo(cameraDto.getCameraNo());
                    }

                    if (cameraDto.getPassword() != null) {      // TODO: old, new to update?
                        camera.setPassword(passwordEncoder.encode(cameraDto.getPassword()));
                    }

                    if (cameraDto.getIntersection() != null) {
                        camera.setIntersection(getIntersection(cameraDto.getIntersection().getIntersectionNo()));
                    }
                    if (cameraDto.getDirection() != null) {
                        camera.setDirection(getIntersection(cameraDto.getDirection().getIntersectionNo()));
                    }

                    if (cameraDto.getGps() != null) {
                        camera.setLatitude(cameraDto.getGps().getLatitude());
                        camera.setLongitude(cameraDto.getGps().getLongitude());
                    }

                    if (cameraDto.getDistance() != null) {
                        camera.setDistance(cameraDto.getDistance());
                    }

                    if (cameraDto.getRtspUrl() != null) {
                        camera.setRtspUrl(cameraDto.getRtspUrl());
                    }

                    if (cameraDto.getRtspId() != null) {
                        camera.setRtspId(cameraDto.getRtspId());
                    }

                    if (cameraDto.getRtspPassword() != null) {
                        camera.setRtspPassword(cameraDto.getRtspPassword());
                    }

                    if (cameraDto.getServerUrl() != null) {
                        camera.setServerUrl(cameraDto.getServerUrl());
                    }

                    if (cameraDto.getSendCycle() != null) {
                        camera.setSendCycle(cameraDto.getSendCycle());
                    }

                    if (cameraDto.getCollectCycle() != null) {
                        camera.setCollectCycle(cameraDto.getCollectCycle());
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

                    if (cameraDto.getSettingsUpdated() != null) {
                        camera.setSettingsUpdated(cameraDto.getSettingsUpdated());
                    }

                    if (cameraDto.getLastDataTime() != null) {
                        camera.setLastDataTime(cameraDto.getLastDataTime());
                    }

                    if (cameraDto.getRoad() != null) {
                        try {
                            camera.setRoad(cameraMapper.toCameraRoad(cameraDto.getRoad()));    // TODO: repository test!!!!!!
                        } catch (JsonProcessingException e) {
                            // Do nothing.
                        }
                    }

                    Camera saved = cameraRepository.save(camera);
                    entityManager.flush();

                    return cameraMapper.toCameraDto(saved);
                })
                .orElseThrow(() -> new IntersectionNotFoundException(IntersectionNotFoundException.INVALID_INTERSECTION_NO));
    }

    @Transactional
    public void delete(String cameraNo) {

        cameraRepository.deleteByCameraNo(cameraNo);
    }

    @Transactional
    public CameraDto get(String cameraNo) {

        return cameraMapper.toCameraDto(cameraRepository.findByCameraNo(cameraNo).orElse(null));
    }

    @Transactional
    public List<CameraDto> getList() {

        Sort sort = Sort.by("cameraNo").ascending();
        return cameraMapper.toCameraDto(cameraRepository.findAll(sort));
    }

    private Intersection getIntersection(String intersectionNo) {

        return intersectionRepository.findByIntersectionNo(intersectionNo)
                .orElseThrow(() -> new IntersectionNotFoundException(IntersectionNotFoundException.INVALID_INTERSECTION_NO));
    }
}
