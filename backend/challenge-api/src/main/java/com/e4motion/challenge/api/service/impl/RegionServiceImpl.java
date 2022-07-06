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

import javax.persistence.EntityManager;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class RegionServiceImpl implements RegionService {

    private final RegionRepository regionRepository;
    private final RegionMapper regionMapper;
    private final EntityManager entityManager;

    @Transactional
    public RegionDto create(RegionDto regionDto) {

        regionRepository.findByRegionNo(regionDto.getRegionNo())
                .ifPresent(region -> {
                    throw new RegionDuplicateException(RegionDuplicateException.REGION_NO_ALREADY_EXISTS);
                });

        Region region = regionMapper.toRegion(regionDto);
        region.setGps(getRegionGps(regionDto, region));

        Region saved = regionRepository.save(region);
        entityManager.flush();

        return regionMapper.toRegionDto(saved);
    }

    @Transactional
    public RegionDto update(String regionNo, RegionDto regionDto) {

        return regionRepository.findByRegionNo(regionNo)
                .map(region -> {
                    if (regionDto.getRegionNo() != null) {
                        region.setRegionNo(region.getRegionNo());
                    }

                    if (regionDto.getRegionName() != null) {
                        region.setRegionName(region.getRegionName());
                    }

                    if (regionDto.getGps() != null) {
                        region.getGps().clear();
                        regionRepository.save(region);
                        entityManager.flush();

                        List<RegionGps> regionGps = getRegionGps(regionDto, region);
                        region.getGps().addAll(regionGps);
                    }

                    Region saved = regionRepository.save(region);
                    entityManager.flush();

                    return regionMapper.toRegionDto(saved);
                })
                .orElseThrow(() -> new RegionNotFoundException(RegionNotFoundException.INVALID_REGION_NO));
    }

    @Transactional
    public void delete(String regionNo) {

        regionRepository.deleteByRegionNo(regionNo);
    }

    @Transactional
    public RegionDto get(String regionNo) {

        return regionMapper.toRegionDto(regionRepository.findByRegionNo(regionNo).orElse(null));
    }

    @Transactional
    public List<RegionDto> getList() {

        Sort sort = Sort.by("regionNo").ascending();
        return regionMapper.toRegionDto(regionRepository.findAll(sort));
    }

    // public for unit test.
    public List<RegionGps> getRegionGps(RegionDto regionDto, Region region) {
        if (regionDto.getGps() != null) {
            AtomicInteger order = new AtomicInteger();
            return regionDto.getGps().stream().map(gps ->
                    RegionGps.builder()
                            .region(region)
                            .latitude(gps.getLatitude())
                            .longitude(gps.getLongitude())
                            .gpsOrder(order.incrementAndGet())
                            .build()).collect(Collectors.toList());
        }
        return null;
    }
}
