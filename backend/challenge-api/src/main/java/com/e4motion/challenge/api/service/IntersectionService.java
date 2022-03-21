package com.e4motion.challenge.api.service;

import com.e4motion.challenge.api.dto.IntersectionDto;

import java.util.List;

public interface IntersectionService {

    IntersectionDto create(IntersectionDto intersectionDto);

    IntersectionDto update(String intersectionId, IntersectionDto intersectionDto);

    void delete(String intersectionId);

    IntersectionDto get(String intersectionId);

    List<IntersectionDto> getList();
}
