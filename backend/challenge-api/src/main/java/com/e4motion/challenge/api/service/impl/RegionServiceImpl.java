package com.e4motion.challenge.api.service.impl;

import com.e4motion.challenge.api.domain.Region;
import com.e4motion.challenge.api.dto.RegionDto;
import com.e4motion.challenge.api.mapper.RegionMapper;
import com.e4motion.challenge.api.repository.RegionRepository;
import com.e4motion.challenge.api.service.RegionService;
import com.e4motion.challenge.common.exception.customexception.RegionDuplicationException;
import com.e4motion.challenge.common.exception.customexception.RegionNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class RegionServiceImpl implements RegionService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final RegionRepository regionRepository;
    private final RegionMapper regionMapper;

    @Transactional
    public RegionDto create(RegionDto regionDto) {

        regionRepository.findByRegionId(regionDto.getRegionId())
                .ifPresent(intersection -> {
                    throw new RegionDuplicationException(RegionDuplicationException.REGION_ID_ALREADY_EXISTS);
                });

        Region region = Region.builder()
                .regionId(regionDto.getRegionId())
                .regionName(regionDto.getRegionName())
                .gps(regionMapper.toGps(regionDto.getGps()))
                .build();

        return regionMapper.toRegionDto(regionRepository.save(region));
    }

    @Transactional
    public RegionDto update(String regionId, RegionDto regionDto) {

        return regionRepository.findByRegionId(regionId)
                .map(region -> {
                    if (regionDto.getRegionName() != null) {
                        region.setRegionName(regionDto.getRegionName());
                    }

                    //TODO: gps update ??
//                    if (regionDto.getGps() != null) {
//                        region.setGps(regionMapper.toGps(regionDto.getGps()));
//                    }

                    return regionMapper.toRegionDto(regionRepository.save(region));
                }).orElseThrow(() -> new RegionNotFoundException(RegionNotFoundException.INVALID_REGION_ID));
    }

    @Transactional
    public void delete(String regionId) {

        regionRepository.deleteByRegionId(regionId);
    }

    @Transactional
    public RegionDto get(String regionId) {

        return regionMapper.toRegionDto(regionRepository.findByRegionId(regionId).orElse(null));
    }

    @Transactional
    public List<RegionDto> getList() {

        return regionMapper.toRegionDto(regionRepository.findAll());
    }
}
