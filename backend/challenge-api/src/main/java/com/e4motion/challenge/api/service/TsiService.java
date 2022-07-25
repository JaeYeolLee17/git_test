package com.e4motion.challenge.api.service;

import com.e4motion.challenge.api.dto.TsiBrokerInfo;
import com.e4motion.challenge.api.dto.TsiNodeInfo;

import java.util.List;

public interface TsiService {

    void insert();

    List<Object> getList();

    void insertNodeInfos(List<TsiNodeInfo> nodeInfo);

    List<TsiNodeInfo> getNodeInfos();

    TsiBrokerInfo getBrokerInfo();

}
