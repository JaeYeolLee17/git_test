package com.e4motion.challenge.api.mapper;

import com.e4motion.challenge.api.domain.Camera;
import com.e4motion.challenge.api.domain.CameraRoad;
import com.e4motion.challenge.api.domain.Intersection;
import com.e4motion.challenge.api.domain.Region;
import com.e4motion.challenge.api.dto.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", imports = {GpsDto.class, Collectors.class, ObjectMapper.class})
public interface IntersectionMapper {

    @Mapping(target = "gps", expression = MappingExpression.TO_INTERSECTION_DTO_GPS)
    IntersectionDto toIntersectionDto(Intersection intersection);

    @Mapping(target = "gps", ignore = true)
    @Mapping(target = "intersections", ignore = true)           // remove circular reference.
    RegionDto toRegionDto(Region region);

    @Mapping(target = "password", ignore = true)
    @Mapping(target = "gps", expression = MappingExpression.TO_CAMERA_DTO_GPS)
    @Mapping(target = "intersection", ignore = true)            // remove circular reference.
    @Mapping(target = "direction", ignore = true)               // remove circular reference.
    CameraDto toCameraDto(Camera camera);

    @Mapping(target = "lane", expression = MappingExpression.TO_CAMERA_DTO_ROAD_LANE)
    @Mapping(target = "direction", expression = MappingExpression.TO_CAMERA_DTO_ROAD_DIRECTION)
    CameraRoadDto toCameraRoadDto(CameraRoad cameraRoad) throws JsonProcessingException;

    List<IntersectionDto> toIntersectionDto(List<Intersection> regions);

    @Mapping(target = "intersectionId", ignore = true)
    @Mapping(target = "latitude", expression = MappingExpression.TO_INTERSECTION_LATITUDE)
    @Mapping(target = "longitude", expression = MappingExpression.TO_INTERSECTION_LONGITUDE)
    @Mapping(target = "cameras", ignore = true)
    Intersection toIntersection (IntersectionDto intersectionDto);

    @Mapping(target = "regionId", ignore = true)                // mapping regionNo only.
    @Mapping(target = "regionName", ignore = true)
    @Mapping(target = "gps", ignore = true)
    @Mapping(target = "intersections", ignore = true)
    Region toRegion(RegionDto regionDto);

}
