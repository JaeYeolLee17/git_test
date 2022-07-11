package com.e4motion.challenge.api.service;

import com.e4motion.challenge.api.dto.CameraDto;

import java.util.List;

public interface CameraService {

    CameraDto create(CameraDto cameraDto);

    CameraDto update(String cameraNo, CameraDto cameraDto);

    void delete(String cameraNo);

    CameraDto get(String cameraNo);

    List<CameraDto> getList(String regionNo, String intersectionNo);
}
