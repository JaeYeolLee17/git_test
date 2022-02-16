package com.e4motion.challenge.data.collector.dao.impl;

import org.springframework.stereotype.Repository;

import com.e4motion.challenge.data.collector.dao.CameraDao;
import com.e4motion.challenge.data.collector.domain.Camera;
import com.e4motion.challenge.data.collector.repository.CameraRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class CameraDaoImpl implements CameraDao {
	
    private final CameraRepository cameraRepository;
    
	public Camera get(String cameraId) throws Exception {
		return cameraRepository.findByCameraId(cameraId).orElse(null);
	}
}
