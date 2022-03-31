package com.e4motion.challenge.api.mapper;

import com.e4motion.challenge.api.domain.Gps;
import com.e4motion.challenge.api.domain.Region;
import com.e4motion.challenge.api.dto.GpsDto;
import com.e4motion.challenge.api.dto.RegionDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RegionMapper {

    RegionDto toRegionDto(Region region);

    List<RegionDto> toRegionDto(List<Region> regions);

    List<Gps> toGps(List<GpsDto> gpsDtos);
}
