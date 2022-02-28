package com.e4motion.challenge.data.collector.service.impl;

import com.e4motion.challenge.data.collector.dto.CameraDataDto;
import com.e4motion.challenge.data.collector.repository.DataRepository;
import com.e4motion.challenge.data.collector.service.DataService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class DataServiceImpl implements DataService {

    private final DataRepository dataRepository;

    @Transactional
    public void insert(CameraDataDto cameraDataDto) {

        dataRepository.insert(cameraDataDto);

        // TODO: save stat to postgresql.

    }

}
