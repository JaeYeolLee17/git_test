package com.e4motion.challenge.data.collector.service;

import com.e4motion.challenge.data.collector.dto.DataDto;

import java.util.HashMap;
import java.util.List;

public interface DataService {

    void insert(DataDto dataDto);

    List<DataDto> query(HashMap<String, Object> map);
}
