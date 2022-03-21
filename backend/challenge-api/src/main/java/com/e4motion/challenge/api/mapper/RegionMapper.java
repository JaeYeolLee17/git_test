package com.e4motion.challenge.api.mapper;

import com.e4motion.challenge.api.domain.Region;
import com.e4motion.challenge.api.dto.RegionDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RegionMapper {
    @Mapping(target = "gps", ignore = true)
    RegionDto toRegionDto(Region region);

    List<RegionDto> toRegionDto(List<Region> regions);
}
