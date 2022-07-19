package com.e4motion.challenge.data.provider.service;

import com.e4motion.challenge.data.provider.dto.DataListDto;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;

public interface DataService {

    DataListDto get(HashMap<String, Object> map);

    void write(HashMap<String, Object> map, Writer writer) throws IOException;

}
