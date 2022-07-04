package com.e4motion.challenge.api.service;

import com.e4motion.challenge.api.dto.RegionDto;

import java.util.List;

public interface RegionService {

    RegionDto create(RegionDto regionDto);

    RegionDto update(String regionNo, RegionDto regionDto);

    void delete(String regionNo);

    RegionDto get(String regionNo);

    List<RegionDto> getList();
}
