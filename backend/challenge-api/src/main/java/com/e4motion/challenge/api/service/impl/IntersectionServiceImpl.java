package com.e4motion.challenge.api.service.impl;

import com.e4motion.challenge.api.domain.Intersection;
import com.e4motion.challenge.api.domain.Region;
import com.e4motion.challenge.api.dto.IntersectionDto;
import com.e4motion.challenge.api.mapper.IntersectionMapper;
import com.e4motion.challenge.api.repository.IntersectionRepository;
import com.e4motion.challenge.api.repository.RegionRepository;
import com.e4motion.challenge.api.service.IntersectionService;
import com.e4motion.challenge.common.exception.customexception.IntersectionDuplicateException;
import com.e4motion.challenge.common.exception.customexception.IntersectionNotFoundException;
import com.e4motion.challenge.common.exception.customexception.RegionNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class IntersectionServiceImpl implements IntersectionService {

    private final IntersectionRepository intersectionRepository;
    private final RegionRepository regionRepository;
    private final IntersectionMapper intersectionMapper;

    @Transactional
    public IntersectionDto create(IntersectionDto intersectionDto) {

        intersectionRepository.findByIntersectionNo(intersectionDto.getIntersectionNo())
                .ifPresent(intersection -> {
                    throw new IntersectionDuplicateException(IntersectionDuplicateException.INTERSECTION_NO_ALREADY_EXISTS);
                });

        Intersection intersection = intersectionMapper.toIntersection(intersectionDto);
        if (intersection.getRegion() != null) {
            intersection.setRegion(getRegion(intersection.getRegion().getRegionNo()));
        }

        return intersectionMapper.toIntersectionDto(intersectionRepository.save(intersection));
    }

    @Transactional
    public IntersectionDto update(String intersectionNo, IntersectionDto intersectionDto) {

        return intersectionRepository.findByIntersectionNo(intersectionNo)
                .map(intersection -> {
                    if (intersectionDto.getIntersectionNo() != null) {
                        intersection.setIntersectionNo(intersectionDto.getIntersectionNo());
                    }

                    if (intersectionDto.getIntersectionName() != null) {
                        intersection.setIntersectionName(intersectionDto.getIntersectionName());
                    }

                    if (intersectionDto.getGps() != null &&
                            intersectionDto.getGps().getLat() != null && intersectionDto.getGps().getLng() != null) {
                        intersection.setLat(intersectionDto.getGps().getLat());
                        intersection.setLng(intersectionDto.getGps().getLng());
                    }

                    if (intersectionDto.getRegion() != null) {
                        intersection.setRegion(getRegion(intersectionDto.getRegion().getRegionNo()));
                    }

                    if (intersectionDto.getNationalId() != null) {
                        intersection.setNationalId(intersectionDto.getNationalId());
                    }

                    return intersectionMapper.toIntersectionDto(intersectionRepository.saveAndFlush(intersection));
                })
                .orElseThrow(() -> new IntersectionNotFoundException(IntersectionNotFoundException.INVALID_INTERSECTION_NO));
    }

    @Transactional
    public void delete(String intersectionNo) {

        intersectionRepository.deleteByIntersectionNo(intersectionNo);
    }

    @Transactional(readOnly = true)
    public IntersectionDto get(String intersectionNo) {

        return intersectionMapper.toIntersectionDto(intersectionRepository.findByIntersectionNo(intersectionNo).orElse(null));
    }

    @Transactional(readOnly = true)
    public List<IntersectionDto> getList(String regionNo) {

        Sort sort = Sort.by("intersectionNo").ascending();
        List<Intersection> intersections = regionNo != null ?
                intersectionRepository.findAllByRegion_RegionNo(regionNo, sort) : intersectionRepository.findAll(sort);

        return intersectionMapper.toIntersectionDto(intersections);
    }

    private Region getRegion(String regionNo) {

        return regionRepository.findByRegionNo(regionNo)
                .orElseThrow(() -> new RegionNotFoundException(RegionNotFoundException.INVALID_REGION_NO));
    }
}
