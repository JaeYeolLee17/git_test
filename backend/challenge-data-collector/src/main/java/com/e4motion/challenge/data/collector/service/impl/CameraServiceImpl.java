package com.e4motion.challenge.data.collector.service.impl;

import com.e4motion.challenge.common.exception.customexception.CameraNotFoundException;
import com.e4motion.challenge.common.response.Response;
import com.e4motion.challenge.data.collector.domain.Camera;
import com.e4motion.challenge.data.collector.repository.CameraRepository;
import com.e4motion.challenge.data.collector.service.CameraService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Slf4j
@RequiredArgsConstructor
@Service
public class CameraServiceImpl implements CameraService {

    private final CameraRepository cameraRepository;

    private final RestTemplate restTemplate;

    @Value("${challenge-api.url}")
    String challengeApiUrl;

    @Transactional
    public void updateSettingsUpdated(String cameraNo, boolean settingsUpdated) {

        cameraRepository.findByCameraNo(cameraNo)
                .map(camera -> {
                    camera.setSettingsUpdated(settingsUpdated);
                    cameraRepository.save(camera);
                    return camera;
                })
                .orElseThrow(() -> new CameraNotFoundException(CameraNotFoundException.INVALID_CAMERA_NO));
    }

    @Transactional(readOnly = true)
    public boolean getSettingsUpdated(String cameraNo) {

        return cameraRepository.findByCameraNo(cameraNo)
                .map(Camera::getSettingsUpdated)
                .orElseThrow(() -> new CameraNotFoundException(CameraNotFoundException.INVALID_CAMERA_NO));
    }

    public Object get(String cameraNo, String authorization) {

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add(HttpHeaders.AUTHORIZATION, authorization);

        Response response = null;
        try {
            ResponseEntity<Response> entity = restTemplate.exchange(challengeApiUrl + cameraNo,
                    HttpMethod.GET,
                    new HttpEntity<>(headers),
                    Response.class);
            if (entity.getStatusCode() == HttpStatus.OK) {
                response = entity.getBody();
            }
        } catch (Exception e) {
            log.info(e.toString());
        }

        if (response == null) {
            throw new CameraNotFoundException(CameraNotFoundException.INVALID_CAMERA_NO);
        }

        return response.get("camera");
    }

}
