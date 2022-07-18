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
public interface CameraMapper {

    @Mapping(target = "password", ignore = true)
    @Mapping(target = "gps", expression = MappingExpressions.TO_CAMERA_DTO_GPS)
    CameraDto toCameraDto(Camera camera);

    @Mapping(target = "gps", expression = MappingExpressions.TO_INTERSECTION_DTO_GPS)
    @Mapping(target = "cameras", ignore = true)                     // remove circular reference.
    IntersectionDto toIntersectionDto(Intersection intersection);

    @Mapping(target = "gps", ignore = true)
    @Mapping(target = "intersections", ignore = true)               // remove circular reference.
    RegionDto toRegionDto(Region region);

    @Mapping(target = "lane", expression = MappingExpressions.TO_CAMERA_DTO_ROAD_LANE)
    @Mapping(target = "direction", expression = MappingExpressions.TO_CAMERA_DTO_ROAD_DIRECTION)
    CameraRoadDto toCameraRoadDto(CameraRoad cameraRoad) throws JsonProcessingException;

    List<CameraDto> toCameraDto(List<Camera> cameras);

    @Mapping(target = "cameraId", ignore = true)
    @Mapping(target = "latitude", expression = MappingExpressions.TO_CAMERA_LATITUDE)
    @Mapping(target = "longitude", expression = MappingExpressions.TO_CAMERA_LONGITUDE)
    Camera toCamera(CameraDto cameraDto);

    @Mapping(target = "intersectionId", ignore = true)              // mapping intersectionNo only.
    @Mapping(target = "intersectionName", ignore = true)
    @Mapping(target = "latitude", ignore = true)
    @Mapping(target = "longitude", ignore = true)
    @Mapping(target = "region", ignore = true)
    @Mapping(target = "nationalId", ignore = true)
    @Mapping(target = "cameras", ignore = true)
    Intersection toIntersection(IntersectionDto intersectionDto);

    @Mapping(target = "cameraRoadId", ignore = true)
    @Mapping(target = "camera", ignore = true)                      // should map manually.
    @Mapping(target = "lane", expression = MappingExpressions.TO_CAMERA_ROAD_LANE)
    @Mapping(target = "direction", expression = MappingExpressions.TO_CAMERA_ROAD_DIRECTION)
    CameraRoad toCameraRoad(CameraRoadDto cameraRoadDto) throws JsonProcessingException;
}
