package com.e4motion.challenge.api;

import com.e4motion.challenge.api.domain.*;
import com.e4motion.challenge.api.dto.*;
import com.e4motion.challenge.common.domain.AuthorityName;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TestDataHelper {

    public static UserDto getUserDto1() {
        return UserDto.builder()
                .username("user1")
                .password("challenge12!@")
                .nickname("nickname1")
                .email("user1@email.com")
                .phone("01022223333")
                .enabled(true)
                .authority(AuthorityName.ROLE_USER)
                .build();
    }

    public static UserDto getUserDto2() {
        return UserDto.builder()
                .username("user2")
                .password("challenge12!@")
                .nickname("nickname2")
                .email("user2@email.com")
                .phone("01044445555")
                .enabled(true)
                .authority(AuthorityName.ROLE_USER)
                .build();
    }

    public static UserUpdateDto getUserUpdateDto() {
        return UserUpdateDto.builder()
                .username("user2updated")
                .oldPassword("challenge12!@")
                .newPassword("challenge12!@updated")
                .nickname("nicknameupdated")
                .email("emailupdated@email.com")
                .phone("01088889999")
                .enabled(true)
                .authority(AuthorityName.ROLE_ADMIN)
                .build();
    }

    public static User getUser1() {
        return User.builder()
                .userId(1L)
                .username("user1")
                .password("challenge1123!")
                .nickname("nickname1")
                .email("user1@email.com")
                .phone("01022223333")
                .enabled(true)
                .authorities(Collections.singleton(new Authority(AuthorityName.ROLE_ADMIN)))
                .build();
    }

    public static User getUser2() {
        return User.builder()
                .userId(2L)
                .username("user2")
                .password("challenge12!@")
                .nickname("nickname2")
                .email("user2@email.com")
                .phone("01044445555")
                .enabled(true)
                .authorities(Collections.singleton(new Authority(AuthorityName.ROLE_USER)))
                .build();
    }

    public static UserDto getAdminUserDto() {
        return UserDto.builder()
                .username("admin")
                .password("challenge1123!")
                .nickname("adminname1")
                .enabled(true)
                .authority(AuthorityName.ROLE_ADMIN)
                .build();
    }

    public static UserDto getManagerUserDto() {
        return UserDto.builder()
                .username("manager")
                .password("challenge1123!")
                .nickname("managername1")
                .enabled(true)
                .authority(AuthorityName.ROLE_MANAGER)
                .build();
    }

    public static RegionDto getRegionDto1() {
        RegionDto regionDto = RegionDto.builder()
                .regionNo("R01")
                .regionName("예비사업구역")
                .build();

        List<GpsDto> gps = new ArrayList<>();
        gps.add(GpsDto.builder().latitude(35.1).longitude(128.1).build());
        gps.add(GpsDto.builder().latitude(35.2).longitude(128.2).build());
        gps.add(GpsDto.builder().latitude(35.3).longitude(128.3).build());
        regionDto.setGps(gps);

        return regionDto;
    }

    public static Region getRegion1() {
        Region region = Region.builder()
                .regionId(1L)
                .regionNo("R01")
                .regionName("예비사업구역")
                .build();

        List<RegionGps> gps = new ArrayList<>();
        gps.add(RegionGps.builder().regionGpsId(1L).region(region).latitude(35.1).longitude(128.1).gpsOrder(1).build());
        gps.add(RegionGps.builder().regionGpsId(2L).region(region).latitude(35.2).longitude(128.2).gpsOrder(2).build());
        gps.add(RegionGps.builder().regionGpsId(3L).region(region).latitude(35.3).longitude(128.3).gpsOrder(3).build());
        region.setGps(gps);

        return region;
    }

    public static RegionDto getRegionDto2() {
        RegionDto regionDto = RegionDto.builder()
                .regionNo("R02")
                .regionName("구역02")
                .build();

        List<GpsDto> gps = new ArrayList<>();
        gps.add(GpsDto.builder().latitude(35.4).longitude(128.4).build());
        gps.add(GpsDto.builder().latitude(35.5).longitude(128.5).build());
        gps.add(GpsDto.builder().latitude(35.6).longitude(128.6).build());
        regionDto.setGps(gps);

        return regionDto;
    }

    public static Region getRegion2() {
        Region region = Region.builder()
                .regionId(2L)
                .regionNo("R02")
                .regionName("구역02")
                .build();

        List<RegionGps> gps = new ArrayList<>();
        gps.add(RegionGps.builder().regionGpsId(4L).region(region).latitude(35.4).longitude(128.4).gpsOrder(1).build());
        gps.add(RegionGps.builder().regionGpsId(5L).region(region).latitude(35.5).longitude(128.5).gpsOrder(2).build());
        gps.add(RegionGps.builder().regionGpsId(6L).region(region).latitude(35.6).longitude(128.6).gpsOrder(3).build());
        region.setGps(gps);

        return region;
    }

    public static IntersectionDto getIntersectionDto1() {
        return IntersectionDto.builder()
                .intersectionNo("I0001")
                .intersectionName("이현삼거리")
                .gps(GpsDto.builder().latitude(35.8784855520347).longitude(128.539885742538).build())
                .region(getRegionDto1())
                .nationalId(1520005300L)
                .build();
    }

    public static Intersection getIntersection1() {
        return Intersection.builder()
                .intersectionId(1L)
                .intersectionNo("I0001")
                .intersectionName("이현삼거리")
                .latitude(35.8784855520347)
                .longitude(128.539885742538)
                .region(getRegion1())
                .nationalId(1520005300L)
                .build();
    }

    public static IntersectionDto getIntersectionDto2() {
        return IntersectionDto.builder()
                .intersectionNo("I0002")
                .intersectionName("배고개삼거리")
                .gps(GpsDto.builder().latitude(35.8787525882359).longitude(128.547154632069).build())
                .region(getRegionDto2())
                .nationalId(1520005400L)
                .build();
    }

    public static Intersection getIntersection2() {
        return Intersection.builder()
                .intersectionId(2L)
                .intersectionNo("I0002")
                .intersectionName("배고개삼거리")
                .latitude(35.8787525882359)
                .longitude(128.547154632069)
                .region(getRegion2())
                .nationalId(1520005400L)
                .build();
    }

    public static CameraDto getCameraDto1() {

        IntersectionDto intersectionDto1 = getIntersectionDto1();
        IntersectionDto intersectionDto2 = getIntersectionDto2();

        String[] lane = new String[]{"123.12 80.00 128.15 20.5 100.1 81.00 108.1 20.9","100.12 80.00 108.15 20.5 80.1 81.00 88.1 20.10"};
        Boolean[][] direction = new Boolean[][]{ new Boolean[]{true, true, false}, new Boolean[]{true, false, false}};
        CameraRoadDto cameraRoadDto = CameraRoadDto.builder()
                .startLine("125.70 85.12 84.10 12.87")
                .lane(lane)
                .uturn("123.12 80.00 128.15 20.5 100.1 81.00 108.1 20.6")
                .crosswalk("123.12 80.00 128.15 20.5 100.1 81.00 108.1 20.6")
                .direction(direction)
                .build();

        return CameraDto.builder()
                .cameraNo("C0002")
                .password("camera12!@")
                .intersection(intersectionDto1)
                .direction(intersectionDto2)
                .gps(GpsDto.builder().latitude(35.8785201012622).longitude(35.8785201012622).build())
                .distance(100)
                .rtspUrl("rtsp://192.67.8.2:554/video1").rtspId(null).rtspPassword(null)
                .serverUrl("http://192.168.71.21:8080/challenge-data").sendCycle(60).collectCycle(10)
                .smallWidth(640).smallHeight(360).largeWidth(1280).largeHeight(710)
                .degree(null)
                .settingsUpdated(true)
                .lastDataTime(null)
                .road(cameraRoadDto)
                .build();
    }

    public static Camera getCamera1() {

        Intersection intersection1 = getIntersection1();
        Intersection intersection2 = getIntersection2();

        Camera camera = Camera.builder()
                .cameraId(1L)
                .cameraNo("C0002")
                .password("camera12!@")
                .intersection(intersection1)
                .direction(intersection2)
                .latitude(35.8785201012622).longitude(128.539299557313)
                .distance(100)
                .rtspUrl("rtsp://192.67.8.2:554/video1").rtspId(null).rtspPassword(null)
                .serverUrl("http://192.168.71.21:8080/challenge-data").sendCycle(60).collectCycle(10)
                .smallWidth(640).smallHeight(360).largeWidth(1280).largeHeight(710)
                .degree(null)
                .settingsUpdated(true)
                .lastDataTime(null)
                .build();

        CameraRoad cameraRoadDto = CameraRoad.builder()
                .cameraRoadId(1L)
                .camera(camera)
                .startLine("125.70 85.12 84.10 12.87")
                .lane("[\"123.12 80.00 128.15 20.5 100.1 81.00 108.1 20.9\",\"100.12 80.00 108.15 20.5 80.1 81.00 88.1 20.10\"]")
                .uturn("123.12 80.00 128.15 20.5 100.1 81.00 108.1 20.6")
                .crosswalk("123.12 80.00 128.15 20.5 100.1 81.00 108.1 20.6")
                .direction("[[true,true,false],[true,false,false]]")
                .build();
        camera.setRoad(cameraRoadDto);

        return camera;
    }
}
