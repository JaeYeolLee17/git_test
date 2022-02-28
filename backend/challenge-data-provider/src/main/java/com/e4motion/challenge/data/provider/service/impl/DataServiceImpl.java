package com.e4motion.challenge.data.provider.service.impl;

import com.e4motion.challenge.data.provider.dto.DataListDto;
import com.e4motion.challenge.data.provider.repository.DataRepository;
import com.e4motion.challenge.data.provider.service.DataService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@RequiredArgsConstructor
@Service
public class DataServiceImpl implements DataService {

    private final DataRepository dataRepository;

    public DataListDto query(HashMap<String, Object> map) {

        return dataRepository.query(map);
    }

}
