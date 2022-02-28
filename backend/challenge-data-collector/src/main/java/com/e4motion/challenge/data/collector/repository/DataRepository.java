package com.e4motion.challenge.data.collector.repository;

import com.e4motion.challenge.data.collector.dto.CameraDataDto;

public interface DataRepository {

    void insert(CameraDataDto cameraDataDto);

}
