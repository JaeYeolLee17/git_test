package com.e4motion.challenge.data.provider.service;

import com.e4motion.challenge.data.provider.dto.DataListDto;

import java.util.HashMap;

public interface DataService {

    DataListDto query(HashMap<String, Object> map);

}
