package com.e4motion.challenge.data.provider.service.impl;

import com.e4motion.challenge.data.provider.dto.DataListDto;
import com.e4motion.challenge.data.provider.repository.DataRepository;
import com.e4motion.challenge.data.provider.service.DataService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;

@RequiredArgsConstructor
@Service
public class DataServiceImpl implements DataService {

    private final DataRepository dataRepository;

    public DataListDto get(HashMap<String, Object> map) {

        return dataRepository.get(map);
    }

    public void write(HashMap<String, Object> map, Writer writer) throws IOException {

        dataRepository.write(map, writer);
    }

}
