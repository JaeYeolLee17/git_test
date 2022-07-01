package com.e4motion.challenge.api.service.impl;

import com.e4motion.challenge.api.dto.RegionDto;
import com.e4motion.challenge.api.mapper.RegionMapper;
import com.e4motion.challenge.api.repository.RegionRepository;
import com.e4motion.challenge.api.service.RegionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.List;

@RequiredArgsConstructor
@Service
public class RegionServiceImpl implements RegionService {

    private final RegionRepository regionRepository;
    private final RegionMapper regionMapper;
    private final EntityManager entityManager;

    @Override
    public RegionDto create(RegionDto regionDto) {
        return null;
    }

    @Override
    public RegionDto update(String regionNo, RegionDto regionDto) {
        return null;
    }

    @Override
    public void delete(String regionNo) {

    }

    @Override
    public RegionDto get(String regionNo) {
        return null;
    }

    @Override
    public List<RegionDto> getList() {
        return null;
    }
}
