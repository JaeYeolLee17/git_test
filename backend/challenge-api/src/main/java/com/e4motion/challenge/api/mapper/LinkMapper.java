package com.e4motion.challenge.api.mapper;

import com.e4motion.challenge.api.domain.Intersection;
import com.e4motion.challenge.api.domain.Link;
import com.e4motion.challenge.api.dto.GpsDto;
import com.e4motion.challenge.api.dto.IntersectionDto;
import com.e4motion.challenge.api.dto.LinkDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", imports = {GpsDto.class, Collectors.class})
public interface LinkMapper {

    @Mapping(target = "gps", expression = MappingExpressions.TO_LINK_DTO_GPS)
    LinkDto toLinkDto(Link link);

    @Mapping(target = "region", ignore = true)
    @Mapping(target = "gps", expression = MappingExpressions.TO_INTERSECTION_DTO_GPS)
    @Mapping(target = "cameras", ignore = true)
    IntersectionDto toIntersectionDto(Intersection intersection);

    List<LinkDto> toLinkDto(List<Link> links);

    @Mapping(target = "linkId", ignore = true)
    @Mapping(target = "gps", ignore = true)                         // should map manually because of link, gpsOrder.
    Link toLink (LinkDto linkDto);

    @Mapping(target = "intersectionId", ignore = true)              // mapping intersectionNo only.
    @Mapping(target = "intersectionName", ignore = true)
    @Mapping(target = "latitude", ignore = true)
    @Mapping(target = "longitude", ignore = true)
    @Mapping(target = "region", ignore = true)
    @Mapping(target = "nationalId", ignore = true)
    @Mapping(target = "cameras", ignore = true)
    Intersection toIntersection(IntersectionDto intersectionDto);
}
