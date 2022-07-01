package com.e4motion.challenge.api;

import com.e4motion.challenge.api.domain.*;
import com.e4motion.challenge.api.dto.GpsDto;
import com.e4motion.challenge.api.dto.RegionDto;
import com.e4motion.challenge.api.dto.UserDto;
import com.e4motion.challenge.api.dto.UserUpdateDto;
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
                .regionName("테스트구역")
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
                .regionName("테스트구역")
                .build();

        List<RegionGps> gps = new ArrayList<>();
        gps.add(RegionGps.builder().regionGpsId(4L).region(region).latitude(35.4).longitude(128.4).gpsOrder(1).build());
        gps.add(RegionGps.builder().regionGpsId(5L).region(region).latitude(35.5).longitude(128.5).gpsOrder(2).build());
        gps.add(RegionGps.builder().regionGpsId(6L).region(region).latitude(35.6).longitude(128.6).gpsOrder(3).build());
        region.setGps(gps);

        return region;
    }
}
