package com.e4motion.challenge.api.service;

import com.e4motion.challenge.api.dto.RegionDto;

import java.util.List;

public interface RegionService {

    RegionDto create(RegionDto regionDto);

    RegionDto update(String regionId, RegionDto regionDto);

    void delete(String regionId);

    RegionDto get(String regionId);

    List<RegionDto> getList();
}
