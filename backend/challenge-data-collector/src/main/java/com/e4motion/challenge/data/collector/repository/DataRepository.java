package com.e4motion.challenge.data.collector.repository;

import com.e4motion.challenge.data.collector.dto.DataDto;

import java.util.HashMap;
import java.util.List;

public interface DataRepository {

    void insert(DataDto data);

    List<DataDto> query(HashMap<String, Object> map);

}
