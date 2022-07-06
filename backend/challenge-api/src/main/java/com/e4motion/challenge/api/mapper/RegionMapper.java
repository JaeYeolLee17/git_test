package com.e4motion.challenge.api.mapper;

import com.e4motion.challenge.api.domain.Intersection;
import com.e4motion.challenge.api.domain.Region;
import com.e4motion.challenge.api.dto.GpsDto;
import com.e4motion.challenge.api.dto.IntersectionDto;
import com.e4motion.challenge.api.dto.RegionDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", imports = {GpsDto.class, Collectors.class})
public interface RegionMapper {

    @Mapping(target = "gps", expression = MappingExpression.TO_REGION_DTO_GPS)
    RegionDto toRegionDto(Region region);

    @Mapping(target = "region", ignore = true)                  // remove circular reference.
    @Mapping(target = "gps", expression = MappingExpression.TO_INTERSECTION_DTO_GPS)
    @Mapping(target = "cameras", ignore = true)
    IntersectionDto toIntersectionDto(Intersection intersection);

    List<RegionDto> toRegionDto(List<Region> regions);

    @Mapping(target = "regionId", ignore = true)
    @Mapping(target = "gps", ignore = true)                     // should map manually because of region, gpsOrder.
    @Mapping(target = "intersections", ignore = true)
    Region toRegion (RegionDto regionDto);

}
