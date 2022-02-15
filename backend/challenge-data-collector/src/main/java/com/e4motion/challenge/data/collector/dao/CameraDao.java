package com.e4motion.challenge.data.collector.dao;

import com.e4motion.challenge.data.collector.domain.entity.Camera;

public interface CameraDao {

	Camera get(String cameraId) throws Exception;
}
