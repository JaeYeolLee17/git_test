package com.e4motion.challenge.api.service;

import com.e4motion.challenge.api.dto.IntersectionDto;

import java.util.List;

public interface IntersectionService {

    IntersectionDto create(IntersectionDto intersectionDto);

    IntersectionDto update(String intersectionNo, IntersectionDto intersectionDto);

    void delete(String intersectionNo);

    IntersectionDto get(String intersectionNo);

    List<IntersectionDto> getList(String regionNo);
}
