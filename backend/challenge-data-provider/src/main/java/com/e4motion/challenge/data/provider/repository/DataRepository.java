package com.e4motion.challenge.data.provider.repository;

import com.e4motion.challenge.data.provider.dto.DataListDto;

import java.util.HashMap;

public interface DataRepository {

    DataListDto query(HashMap<String, Object> map);

}
