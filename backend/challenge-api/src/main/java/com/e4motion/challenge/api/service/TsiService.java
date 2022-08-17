package com.e4motion.challenge.api.service;

import com.e4motion.challenge.api.dto.TsiDto;
import com.e4motion.challenge.api.dto.TsiHubDto;
import com.e4motion.challenge.api.dto.TsiNodeDto;
import com.e4motion.challenge.common.domain.TsiFilterBy;

import java.util.List;

public interface TsiService {

    boolean upsert(TsiHubDto tsiHubDto);

    List<TsiDto> getList(TsiFilterBy filterBy, String filterValue);

    void insertNodeInfo(List<TsiNodeDto> nodeDtos);

}
