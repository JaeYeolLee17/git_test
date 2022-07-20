package com.e4motion.challenge.data.collector.service;

public interface CameraService {

    void updateSettingsUpdated(String cameraNo, boolean settingsUpdated);

    boolean getSettingsUpdated(String cameraNo);

    Object get(String cameraNo, String token);
}
