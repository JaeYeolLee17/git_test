package com.e4motion.challenge.data.collector.service.impl;

import com.e4motion.challenge.data.collector.domain.Camera;
import com.e4motion.challenge.data.collector.repository.CameraRepository;
import com.e4motion.challenge.data.collector.service.CameraService;
import com.e4motion.common.exception.customexception.CameraNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CameraServiceImpl implements CameraService {

    private final CameraRepository cameraRepository;

    @Transactional(readOnly = true)
    public boolean getSettingsUpdated(String cameraId) {

        return cameraRepository.findByCameraId(cameraId)
                .map(Camera::isSettingsUpdated)
                .orElseThrow(() -> new CameraNotFoundException(CameraNotFoundException.INVALID_CAMERA_ID));
    }
}
