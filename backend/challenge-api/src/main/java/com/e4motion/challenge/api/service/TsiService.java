package com.e4motion.challenge.api.service;

import com.e4motion.challenge.api.dto.TsiBrokerDto;
import com.e4motion.challenge.api.dto.TsiDto;
import com.e4motion.challenge.api.dto.TsiHubDto;
import com.e4motion.challenge.api.dto.TsiNodeDto;
import com.e4motion.challenge.common.domain.TsiFilterBy;

import java.util.List;

public interface TsiService {

    void upsertTsi(TsiHubDto tsiHubDto);

    List<TsiDto> getTsiList(TsiFilterBy filterBy, String filterValue);

    void insertNodeInfo(List<TsiNodeDto> nodeDtos);

    List<TsiNodeDto> getNodeInfo();

    TsiBrokerDto getBrokerInfo();

}
