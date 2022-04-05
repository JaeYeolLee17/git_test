package com.e4motion.challenge.api.mapper;

import com.e4motion.challenge.api.domain.Region;
import com.e4motion.challenge.api.domain.RegionGps;
import com.e4motion.challenge.api.dto.GpsDto;
import com.e4motion.challenge.api.dto.RegionDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RegionMapper {

    RegionDto toRegionDto(Region region);

    List<RegionDto> toRegionDto(List<Region> regions);

    Region toRegion (RegionDto regionDto);

    List<RegionGps> toGps (List<GpsDto> gpsDto);
}
