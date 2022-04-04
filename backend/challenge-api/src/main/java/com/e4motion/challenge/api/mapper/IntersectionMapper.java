package com.e4motion.challenge.api.mapper;

import com.e4motion.challenge.api.domain.Intersection;
import com.e4motion.challenge.api.dto.IntersectionDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IntersectionMapper {

    @Mapping(source = "region", target = "regionDto")
    IntersectionDto toIntersectionDto(Intersection intersection);

    List<IntersectionDto> toIntersectionDto(List<Intersection> intersections);

    @Mapping(source = "regionDto", target = "region")
    Intersection toIntersection(IntersectionDto intersectionDto);
}
