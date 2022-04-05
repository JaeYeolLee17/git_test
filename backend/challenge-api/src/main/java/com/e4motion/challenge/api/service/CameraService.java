package com.e4motion.challenge.api.service;

import com.e4motion.challenge.api.dto.CameraDto;
import com.e4motion.challenge.api.dto.RoadDto;

import java.util.List;

public interface CameraService {

    CameraDto create(CameraDto cameraDto);

    CameraDto update(String cameraId, CameraDto cameraDto);

    void delete(String cameraId);

    CameraDto get(String cameraId);

    List<CameraDto> getList();
}
