package com.e4motion.challenge.data.collector.service.impl;

import com.e4motion.challenge.data.collector.dto.DataDto;
import com.e4motion.challenge.data.collector.repository.DataRepository;
import com.e4motion.challenge.data.collector.service.DataService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

@RequiredArgsConstructor
@Service
public class DataServiceImpl implements DataService {

    private final DataRepository dataRepository;

    @Transactional
    public void insert(DataDto dataDto) {

        dataRepository.insert(dataDto);

        // TODO: save stat to postgresql.

    }

    public List<DataDto> query(HashMap<String, Object> map) {

        return dataRepository.query(map);
    }
}
