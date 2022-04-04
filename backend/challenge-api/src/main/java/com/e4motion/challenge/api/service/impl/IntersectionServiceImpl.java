package com.e4motion.challenge.api.service.impl;

import com.e4motion.challenge.api.domain.Intersection;
import com.e4motion.challenge.api.dto.IntersectionDto;
import com.e4motion.challenge.api.mapper.IntersectionMapper;
import com.e4motion.challenge.api.mapper.RegionMapper;
import com.e4motion.challenge.api.repository.IntersectionRepository;
import com.e4motion.challenge.api.service.IntersectionService;
import com.e4motion.challenge.common.exception.customexception.IntersectionDuplicationException;
import com.e4motion.challenge.common.exception.customexception.IntersectionNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class IntersectionServiceImpl implements IntersectionService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final IntersectionRepository intersectionRepository;
    private final IntersectionMapper intersectionMapper;
    private final RegionMapper regionMapper;

    @Transactional
    public IntersectionDto create(IntersectionDto intersectionDto) {

        intersectionRepository.findByIntersectionId(intersectionDto.getIntersectionId())
                .ifPresent(intersection -> {
                    throw new IntersectionDuplicationException(IntersectionDuplicationException.INTERSECTION_ID_ALREADY_EXISTS);
                });

        Intersection intersection = Intersection.builder()
                .intersectionId(intersectionDto.getIntersectionId())
                .intersectionName(intersectionDto.getIntersectionName())
                .latitude(intersectionDto.getLatitude())
                .longitude(intersectionDto.getLongitude())
                .region(regionMapper.toRegion(intersectionDto.getRegionDto()))
                .nationalId(intersectionDto.getNationalId())
                .build();

        logger.debug(" intersection : " + intersection);

        return intersectionMapper.toIntersectionDto(intersectionRepository.save(intersection));
    }

    @Transactional
    public IntersectionDto update(String intersectionId, IntersectionDto intersectionDto) {

        return intersectionRepository.findByIntersectionId(intersectionId)
                .map(intersection -> {
                    if (intersectionDto.getIntersectionName() != null) {
                        intersection.setIntersectionName(intersectionDto.getIntersectionName());
                    }

                    if (intersectionDto.getLatitude() != null) {
                        intersection.setLatitude(intersectionDto.getLatitude());
                    }

                    if (intersectionDto.getLongitude() != null) {
                        intersection.setLongitude(intersectionDto.getLongitude());
                    }

                    //TODO : Region ??
                    if (intersectionDto.getRegionDto() != null) {
                        intersection.setRegion(regionMapper.toRegion(intersectionDto.getRegionDto()));
                    }

                    if (intersectionDto.getNationalId() != null) {
                        intersection.setNationalId(intersectionDto.getNationalId());
                    }

                    return intersectionMapper.toIntersectionDto(intersectionRepository.save(intersection));
                }).orElseThrow(() -> new IntersectionNotFoundException(IntersectionNotFoundException.INVALID_INTERSECTION_ID));
    }

    @Transactional
    public void delete(String intersectionId) {

        intersectionRepository.deleteByIntersectionId(intersectionId);
    }

    @Transactional(readOnly = true)
    public IntersectionDto get(String intersectionId) {

        return intersectionMapper.toIntersectionDto(intersectionRepository.findByIntersectionId(intersectionId).orElse(null));
    }

    @Transactional(readOnly = true)
    public List<IntersectionDto> getList() {

        return intersectionMapper.toIntersectionDto(intersectionRepository.findAll());
    }
}
