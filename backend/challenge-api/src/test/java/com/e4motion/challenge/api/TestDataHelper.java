package com.e4motion.challenge.api;

import com.e4motion.challenge.api.domain.*;
import com.e4motion.challenge.api.dto.*;
import com.e4motion.challenge.common.constant.AuthorityName;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TestDataHelper {

    public static UserDto getUserDto1() {

        return UserDto.builder()
                .username("user1")
                .password("user12!@")
                .nickname("nickname1")
                .email("user1@email.com")
                .phone("01022223333")
                .disabled(false)
                .authority(AuthorityName.ROLE_USER)
                .build();
    }

    public static UserDto getUserDto2() {

        return UserDto.builder()
                .username("user2")
                .password("user12!@")
                .nickname("nickname2")
                .email("user2@email.com")
                .phone("01044445555")
                .disabled(null)
                .authority(AuthorityName.ROLE_USER)
                .build();
    }

    public static UserUpdateDto getUserUpdateDto() {

        return UserUpdateDto.builder()
                .username("user2updated")
                .oldPassword("user12!@")
                .newPassword("challenge1123!")
                .nickname("nicknameupdated")
                .email("emailupdated@email.com")
                .phone("01088889999")
                .disabled(false)
                .authority(AuthorityName.ROLE_ADMIN)
                .build();
    }

    public static User getUser1() {

        return getUser(1L, getUserDto1());
    }

    public static User getUser2() {

        return getUser(2L, getUserDto2());
    }

    private static User getUser(long id, UserDto userDto) {

        return User.builder()
                .userId(id)
                .username(userDto.getUsername())
                .password(userDto.getPassword())
                .nickname(userDto.getNickname())
                .email(userDto.getEmail())
                .phone(userDto.getPhone())
                .disabled(userDto.getDisabled())
                .authorities(Collections.singleton(new Authority(userDto.getAuthority())))
                .build();
    }

    public static RegionDto getRegionDto1() {

        RegionDto regionDto = RegionDto.builder()
                .regionNo("R01")
                .regionName("예비사업구역")
                .build();

        List<GpsDto> gps = new ArrayList<>();
        gps.add(GpsDto.builder().lat(35.1).lng(128.1).build());
        gps.add(GpsDto.builder().lat(35.2).lng(128.2).build());
        gps.add(GpsDto.builder().lat(35.3).lng(128.3).build());
        regionDto.setGps(gps);

        return regionDto;
    }

    public static RegionDto getRegionDto2() {

        RegionDto regionDto = RegionDto.builder()
                .regionNo("R02")
                .regionName("구역02")
                .build();

        List<GpsDto> gps = new ArrayList<>();
        gps.add(GpsDto.builder().lat(35.4).lng(128.4).build());
        gps.add(GpsDto.builder().lat(35.5).lng(128.5).build());
        gps.add(GpsDto.builder().lat(35.6).lng(128.6).build());
        regionDto.setGps(gps);

        return regionDto;
    }

    public static Region getRegion1() {

        return getRegion(1L, getRegionDto1());
    }

    public static Region getRegion2() {

        return getRegion(2L, getRegionDto2());
    }

    public static Region getRegion(long id, RegionDto regionDto) {

        Region region = Region.builder()
                .regionId(id)
                .regionNo(regionDto.getRegionNo())
                .regionName(regionDto.getRegionName())
                .build();

        List<RegionGps> gps = new ArrayList<>();
        for (GpsDto gpsDto: regionDto.getGps()) {
            int i = gps.size() + 1;
            gps.add(RegionGps.builder().regionGpsId((long)i).region(region).lat(gpsDto.getLat()).lng(gpsDto.getLng()).gpsOrder(i).build());
        }
        region.setGps(gps);

        return region;
    }

    public static IntersectionDto getIntersectionDto1() {

        return IntersectionDto.builder()
                .intersectionNo("I0001")
                .intersectionName("이현삼거리")
                .gps(GpsDto.builder().lat(35.8784855520347).lng(128.539885742538).build())
                .region(getRegionDto1())
                .nationalId(1520005300L)
                .build();
    }

    public static IntersectionDto getIntersectionDto2() {

        return IntersectionDto.builder()
                .intersectionNo("I0002")
                .intersectionName("배고개삼거리")
                .gps(GpsDto.builder().lat(35.8787525882359).lng(128.547154632069).build())
                .region(getRegionDto2())
                .nationalId(1520005400L)
                .build();
    }

    public static Intersection getIntersection1() {

        IntersectionDto intersectionDto = getIntersectionDto1();
        Region region = getRegion1();

        return Intersection.builder()
                .intersectionId(1L)
                .intersectionNo(intersectionDto.getIntersectionNo())
                .intersectionName(intersectionDto.getIntersectionName())
                .lat(intersectionDto.getGps().getLat())
                .lng(intersectionDto.getGps().getLng())
                .region(region)
                .nationalId(intersectionDto.getNationalId())
                .build();
    }

    public static Intersection getIntersection2() {

        IntersectionDto intersectionDto = getIntersectionDto2();
        Region region = getRegion2();

        return Intersection.builder()
                .intersectionId(2L)
                .intersectionNo(intersectionDto.getIntersectionNo())
                .intersectionName(intersectionDto.getIntersectionName())
                .lat(intersectionDto.getGps().getLat())
                .lng(intersectionDto.getGps().getLng())
                .region(region)
                .nationalId(intersectionDto.getNationalId())
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
                .cameraNo("C0001")
                .password("camera12!@")
                .intersection(intersectionDto1)
                .direction(intersectionDto2)
                .gps(GpsDto.builder().lat(35.8786655952179).lng(128.539900300697).build())
                .distance(100)
                .rtspUrl("rtsp://192.67.8.1:554/video1").rtspId(null).rtspPassword(null)
                .serverUrl("http://192.168.71.21:8080/challenge-data").sendCycle(60).collectCycle(10)
                .smallWidth(640).smallHeight(360).largeWidth(1280).largeHeight(710)
                .degree(null)
                .settingsUpdated(true)
                .lastDataTime(null)
                .road(cameraRoadDto)
                .build();
    }

    public static CameraDto getCameraDto2() {

        IntersectionDto intersectionDto1 = getIntersectionDto1();
        IntersectionDto intersectionDto2 = getIntersectionDto2();

        String[] lane = new String[]{"123.12 80.00 128.15 20.5 100.1 81.00 108.1 20.9"};
        Boolean[][] direction = new Boolean[][]{ new Boolean[]{}, new Boolean[]{}};
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
                .intersection(intersectionDto2)
                .direction(intersectionDto1)
                .gps(GpsDto.builder().lat(35.8785201012622).lng(35.8785201012622).build())
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
                .cameraNo("C0001")
                .password("camera12!@")
                .intersection(intersection1)
                .direction(intersection2)
                .lat(35.8786655952179).lng(128.539900300697)
                .distance(100)
                .rtspUrl("rtsp://192.67.8.1:554/video1").rtspId(null).rtspPassword(null)
                .serverUrl("http://192.168.71.21:8080/challenge-data").sendCycle(60).collectCycle(10)
                .smallWidth(640).smallHeight(360).largeWidth(1280).largeHeight(710)
                .degree(null)
                .settingsUpdated(true)
                .lastDataTime(null)
                .build();

        CameraRoad cameraRoad = CameraRoad.builder()
                .cameraRoadId(1L)
                .camera(camera)
                .startLine("125.70 85.12 84.10 12.87")
                .lane("[\"123.12 80.00 128.15 20.5 100.1 81.00 108.1 20.9\",\"100.12 80.00 108.15 20.5 80.1 81.00 88.1 20.10\"]")
                .uturn("123.12 80.00 128.15 20.5 100.1 81.00 108.1 20.6")
                .crosswalk("123.12 80.00 128.15 20.5 100.1 81.00 108.1 20.6")
                .direction("[[true,true,false],[true,false,false]]")
                .build();
        camera.setRoad(cameraRoad);

        return camera;
    }

    public static Camera getCamera2() {

        Intersection intersection1 = getIntersection1();
        Intersection intersection2 = getIntersection2();

        Camera camera = Camera.builder()
                .cameraId(2L)
                .cameraNo("C0002")
                .password("camera12!@")
                .intersection(intersection2)
                .direction(intersection1)
                .lat(35.8785201012622).lng(128.539299557313)
                .distance(100)
                .rtspUrl("rtsp://192.67.8.2:554/video1").rtspId(null).rtspPassword(null)
                .serverUrl("http://192.168.71.21:8080/challenge-data").sendCycle(60).collectCycle(10)
                .smallWidth(640).smallHeight(360).largeWidth(1280).largeHeight(710)
                .degree(null)
                .settingsUpdated(true)
                .lastDataTime(null)
                .build();

        CameraRoad cameraRoad = CameraRoad.builder()
                .cameraRoadId(2L)
                .camera(camera)
                .startLine("125.70 85.12 84.10 12.87")
                .lane("[\"123.12 80.00 128.15 20.5 100.1 81.00 108.1 20.9\"]")
                .uturn("123.12 80.00 128.15 20.5 100.1 81.00 108.1 20.6")
                .crosswalk("123.12 80.00 128.15 20.5 100.1 81.00 108.1 20.6")
                .direction("[[],[]]")
                .build();
        camera.setRoad(cameraRoad);

        return camera;
    }

    public static LinkDto getLinkDto1() {

        LinkDto linkDto = LinkDto.builder()
                .linkId(1L)
                .start(getIntersectionDto1())
                .end(getIntersectionDto2())
                .build();

        List<GpsDto> gps = new ArrayList<>();
        gps.add(GpsDto.builder().lat(35.1111).lng(128.11111).build());
        gps.add(GpsDto.builder().lat(35.2222).lng(128.22222).build());
        gps.add(GpsDto.builder().lat(35.3333).lng(128.33333).build());
        linkDto.setGps(gps);

        return linkDto;
    }

    public static Link getLink1() {

        Link link = Link.builder()
                .linkId(1L)
                .start(getIntersection1())
                .end(getIntersection2())
                .build();

        List<LinkGps> gps = new ArrayList<>();
        gps.add(LinkGps.builder().linkGpsId(1L).link(link).lat(35.1111).lng(128.1111).gpsOrder(1).build());
        gps.add(LinkGps.builder().linkGpsId(2L).link(link).lat(35.22222).lng(128.22222).gpsOrder(2).build());
        gps.add(LinkGps.builder().linkGpsId(3L).link(link).lat(35.33333).lng(128.33333).gpsOrder(3).build());
        link.setGps(gps);

        return link;
    }
}
