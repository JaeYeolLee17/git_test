package com.e4motion.challenge.data.collector.service.impl;

import com.e4motion.challenge.data.collector.dto.CameraDataDto;
import com.e4motion.challenge.data.collector.repository.DataRepository;
import com.e4motion.challenge.data.collector.service.DataService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class DataServiceImpl implements DataService {

    private final DataRepository dataRepository;

    public Boolean insert(CameraDataDto cameraDataDto) {

        return dataRepository.insert(cameraDataDto);
    }
}
