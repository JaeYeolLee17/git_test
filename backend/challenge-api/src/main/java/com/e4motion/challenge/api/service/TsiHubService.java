package com.e4motion.challenge.api.service;

import com.e4motion.challenge.api.dto.TsiBrokerDto;
import com.e4motion.challenge.api.dto.TsiNodeDto;

import java.util.List;

public interface TsiHubService {

    List<TsiNodeDto> getNodeInfo();

    TsiBrokerDto getBrokerInfo();

}
