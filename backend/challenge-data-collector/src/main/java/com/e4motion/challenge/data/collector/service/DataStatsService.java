package com.e4motion.challenge.data.collector.service;

import com.e4motion.challenge.data.collector.dto.CameraDataDto;

import java.text.ParseException;

public interface DataStatsService {

    Long insert(CameraDataDto cameraDataDto);

}
