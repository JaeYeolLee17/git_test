package com.e4motion.challenge.api.mapper;

import com.e4motion.challenge.api.domain.Intersection;
import com.e4motion.challenge.api.dto.IntersectionDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IntersectionMapper {
    @Mapping(target = "regionDto", ignore = true)
    IntersectionDto toIntersectionDto(Intersection intersection);

    List<IntersectionDto> toIntersectionDto(List<Intersection> intersections);
}
