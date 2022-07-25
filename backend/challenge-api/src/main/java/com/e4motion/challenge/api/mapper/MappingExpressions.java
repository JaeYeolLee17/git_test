package com.e4motion.challenge.api.mapper;

public class MappingExpressions {

    public final static String TO_USER_DTO_AUTHORITY = "java(user.getAuthorities().isEmpty() ? null : user.getAuthorities().iterator().next().getAuthorityName())";
    public final static String TO_USER_AUTHORITIES = "java(Collections.singleton(new Authority(userDto.getAuthority())))";

    public final static String TO_REGION_DTO_GPS = "java(region.getGps() == null ? null : region.getGps().stream().map(gps -> GpsDto.builder().lat(gps.getLat()).lng(gps.getLng()).build()).collect(Collectors.toList()))";
    public final static String TO_INTERSECTION_DTO_GPS = "java(intersection.getLat() == null ? null : GpsDto.builder().lat(intersection.getLat()).lng(intersection.getLng()).build())";

    public final static String TO_INTERSECTION_LAT = "java(intersectionDto.getGps() == null ? null : intersectionDto.getGps().getLat())";
    public final static String TO_INTERSECTION_LNG = "java(intersectionDto.getGps() == null ? null : intersectionDto.getGps().getLng())";

    public final static String TO_CAMERA_DTO_GPS = "java(camera.getLat() == null ? null : GpsDto.builder().lat(camera.getLat()).lng(camera.getLng()).build())";
    public final static String TO_CAMERA_LAT = "java(cameraDto.getGps() == null ? null : cameraDto.getGps().getLat())";
    public final static String TO_CAMERA_LNG = "java(cameraDto.getGps() == null ? null : cameraDto.getGps().getLng())";

    public final static String TO_CAMERA_DTO_ROAD_LANE = "java(new ObjectMapper().readValue(cameraRoad.getLane(), String[].class))";
    public final static String TO_CAMERA_DTO_ROAD_DIRECTION = "java(new ObjectMapper().readValue(cameraRoad.getDirection(), Boolean[][].class))";

    public final static String TO_CAMERA_DTO_DIRECTION = "java(IntersectionDto.builder().intersectionNo(camera.getDirection().getIntersectionNo()).intersectionName(camera.getDirection().getIntersectionName()).build())";

    public final static String TO_CAMERA_ROAD_LANE = "java(new ObjectMapper().writeValueAsString(cameraRoadDto.getLane()))";
    public final static String TO_CAMERA_ROAD_DIRECTION = "java(new ObjectMapper().writeValueAsString(cameraRoadDto.getDirection()))";

    public final static String TO_LINK_DTO_GPS = "java(link.getGps() == null ? null : link.getGps().stream().map(gps -> GpsDto.builder().lat(gps.getLat()).lng(gps.getLng()).build()).collect(Collectors.toList()))";
}
