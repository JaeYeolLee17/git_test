package com.e4motion.challenge.api.mapper;

import com.e4motion.challenge.api.domain.Camera;
import com.e4motion.challenge.api.domain.Intersection;
import com.e4motion.challenge.api.domain.Region;
import com.e4motion.challenge.api.dto.CameraDto;
import com.e4motion.challenge.api.dto.GpsDto;
import com.e4motion.challenge.api.dto.IntersectionDto;
import com.e4motion.challenge.api.dto.RegionDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", imports = {GpsDto.class, Collectors.class})
public interface IntersectionMapper {

    @Mapping(target = "gps", expression = MappingExpressions.TO_INTERSECTION_DTO_GPS)
    IntersectionDto toIntersectionDto(Intersection intersection);

    @Mapping(target = "gps", ignore = true)
    @Mapping(target = "intersections", ignore = true)           // remove circular reference.
    RegionDto toRegionDto(Region region);

    @Mapping(target = "password", ignore = true)
    @Mapping(target = "gps", expression = MappingExpressions.TO_CAMERA_DTO_GPS)
    @Mapping(target = "intersection", ignore = true)            // remove circular reference.
    @Mapping(target = "direction", expression = MappingExpressions.TO_CAMERA_DTO_DIRECTION)  // remove circular reference.
    @Mapping(target = "road", ignore = true)
    CameraDto toCameraDto(Camera camera);

    List<IntersectionDto> toIntersectionDto(List<Intersection> regions);

    @Mapping(target = "intersectionId", ignore = true)
    @Mapping(target = "latitude", expression = MappingExpressions.TO_INTERSECTION_LATITUDE)
    @Mapping(target = "longitude", expression = MappingExpressions.TO_INTERSECTION_LONGITUDE)
    @Mapping(target = "cameras", ignore = true)
    Intersection toIntersection (IntersectionDto intersectionDto);

    @Mapping(target = "regionId", ignore = true)                // mapping regionNo only.
    @Mapping(target = "regionName", ignore = true)
    @Mapping(target = "gps", ignore = true)
    @Mapping(target = "intersections", ignore = true)
    Region toRegion(RegionDto regionDto);

}
