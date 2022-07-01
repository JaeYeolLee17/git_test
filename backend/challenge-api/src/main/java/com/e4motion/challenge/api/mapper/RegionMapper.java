package com.e4motion.challenge.api.mapper;

import com.e4motion.challenge.api.domain.Region;
import com.e4motion.challenge.api.domain.RegionGps;
import com.e4motion.challenge.api.dto.GpsDto;
import com.e4motion.challenge.api.dto.RegionDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.stream.Collectors;
import java.util.concurrent.atomic.AtomicInteger;

@Mapper(componentModel = "spring", imports = {GpsDto.class, RegionGps.class, AtomicInteger.class, Collectors.class})
public interface RegionMapper {

    @Mapping(target = "gps", expression = "java(region.getGps().stream().map(gps -> GpsDto.builder().latitude(gps.getLatitude()).longitude(gps.getLongitude()).build()).collect(Collectors.toList()))")
    RegionDto toRegionDto(Region region);

    List<RegionDto> toRegionDto(List<Region> regions);

    @Mapping(target = "regionId", ignore = true)
    @Mapping(target = "gps", ignore = true)         // should map manually because of region, gpsOrder.
    //@Mapping(target = "gps", expression = "java(regionDto.getGps().stream().map(gps -> RegionGps.builder().latitude(gps.getLatitude()).longitude(gps.getLongitude()).build()).collect(Collectors.toList()))")
    Region toRegion (RegionDto regionDto);

}
