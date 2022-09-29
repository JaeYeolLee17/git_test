package com.e4motion.challenge.api.service.impl;

import com.e4motion.challenge.api.domain.Region;
import com.e4motion.challenge.api.domain.RegionGps;
import com.e4motion.challenge.api.dto.RegionDto;
import com.e4motion.challenge.api.mapper.RegionMapper;
import com.e4motion.challenge.api.repository.RegionRepository;
import com.e4motion.challenge.api.service.RegionService;
import com.e4motion.challenge.common.exception.customexception.RegionDuplicateException;
import com.e4motion.challenge.common.exception.customexception.RegionNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class RegionServiceImpl implements RegionService {

    private final RegionRepository regionRepository;
    private final RegionMapper regionMapper;

    @Transactional
    public RegionDto create(RegionDto regionDto) {

        checkIfRegionExists(regionDto.getRegionNo());

        Region region = regionMapper.toRegion(regionDto);
        region.setGps(makeRegionGps(regionDto, region));

        return regionMapper.toRegionDto(regionRepository.save(region));
    }

    @Transactional
    public RegionDto update(String regionNo, RegionDto regionDto) {

        return regionRepository.findByRegionNo(regionNo)
                .map(region -> {
                    if (regionDto.getRegionNo() != null && !regionDto.getRegionNo().equals(region.getRegionNo())) {
                        checkIfRegionExists(regionDto.getRegionNo());
                        region.setRegionNo(regionDto.getRegionNo());
                    }

                    if (regionDto.getRegionName() != null) {
                        region.setRegionName(regionDto.getRegionName());
                    }

                    if (regionDto.getGps() != null) {
                        region.getGps().clear();
                        regionRepository.saveAndFlush(region);

                        List<RegionGps> regionGps = makeRegionGps(regionDto, region);
                        region.getGps().addAll(regionGps);
                    }

                    return regionMapper.toRegionDto(regionRepository.saveAndFlush(region));
                })
                .orElseThrow(() -> new RegionNotFoundException(RegionNotFoundException.INVALID_REGION_NO));
    }

    @Transactional
    public void delete(String regionNo) {

        regionRepository.deleteByRegionNo(regionNo);
    }

    @Transactional(readOnly = true)
    public RegionDto get(String regionNo) {

        return regionMapper.toRegionDto(regionRepository.findByRegionNo(regionNo).orElse(null));
    }

    @Transactional(readOnly = true)
    public List<RegionDto> getList() {

        Sort sort = Sort.by("regionNo").ascending();
        return regionMapper.toRegionDto(regionRepository.findAll(sort));
    }

    private void checkIfRegionExists(String regionNo) {

        regionRepository.findByRegionNo(regionNo)
                .ifPresent(region -> {
                    throw new RegionDuplicateException(RegionDuplicateException.REGION_NO_ALREADY_EXISTS);
                });

    }

    private List<RegionGps> makeRegionGps(RegionDto regionDto, Region region) {
        if (regionDto.getGps() != null) {
            AtomicInteger order = new AtomicInteger();
            return regionDto.getGps().stream()
                    .filter(gps -> gps.getLat() != null && gps.getLng() != null)
                    .map(gps ->
                            RegionGps.builder()
                                    .region(region)
                                    .lat(gps.getLat())
                                    .lng(gps.getLng())
                                    .gpsOrder(order.incrementAndGet())
                                    .build())
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }
}
