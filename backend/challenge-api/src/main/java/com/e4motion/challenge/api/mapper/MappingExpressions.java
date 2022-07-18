package com.e4motion.challenge.api.mapper;

public class MappingExpressions {

    public final static String TO_USER_DTO_AUTHORITY = "java(user.getAuthorities().isEmpty() ? null : user.getAuthorities().iterator().next().getAuthorityName())";
    public final static String TO_USER_AUTHORITIES = "java(Collections.singleton(new Authority(userDto.getAuthority())))";

    public final static String TO_REGION_DTO_GPS = "java(region.getGps() == null ? null : region.getGps().stream().map(gps -> GpsDto.builder().latitude(gps.getLatitude()).longitude(gps.getLongitude()).build()).collect(Collectors.toList()))";
    public final static String TO_INTERSECTION_DTO_GPS = "java(intersection.getLatitude() == null ? null : GpsDto.builder().latitude(intersection.getLatitude()).longitude(intersection.getLongitude()).build())";

    public final static String TO_INTERSECTION_LATITUDE = "java(intersectionDto.getGps() == null ? null : intersectionDto.getGps().getLatitude())";
    public final static String TO_INTERSECTION_LONGITUDE = "java(intersectionDto.getGps() == null ? null : intersectionDto.getGps().getLongitude())";

    public final static String TO_CAMERA_DTO_GPS = "java(camera.getLatitude() == null ? null : GpsDto.builder().latitude(camera.getLatitude()).longitude(camera.getLongitude()).build())";
    public final static String TO_CAMERA_LATITUDE = "java(cameraDto.getGps() == null ? null : cameraDto.getGps().getLatitude())";
    public final static String TO_CAMERA_LONGITUDE = "java(cameraDto.getGps() == null ? null : cameraDto.getGps().getLongitude())";

    public final static String TO_CAMERA_DTO_ROAD_LANE = "java(new ObjectMapper().readValue(cameraRoad.getLane(), String[].class))";
    public final static String TO_CAMERA_DTO_ROAD_DIRECTION = "java(new ObjectMapper().readValue(cameraRoad.getDirection(), Boolean[][].class))";

    public final static String TO_CAMERA_DTO_DIRECTION = "java(IntersectionDto.builder().intersectionNo(camera.getDirection().getIntersectionNo()).intersectionName(camera.getDirection().getIntersectionName()).build())";

    public final static String TO_CAMERA_ROAD_LANE = "java(new ObjectMapper().writeValueAsString(cameraRoadDto.getLane()))";
    public final static String TO_CAMERA_ROAD_DIRECTION = "java(new ObjectMapper().writeValueAsString(cameraRoadDto.getDirection()))";

    public final static String TO_LINK_DTO_GPS = "java(link.getGps() == null ? null : link.getGps().stream().map(gps -> GpsDto.builder().latitude(gps.getLatitude()).longitude(gps.getLongitude()).build()).collect(Collectors.toList()))";
}
