package com.e4motion.challenge.api.mapper;

import com.e4motion.challenge.api.domain.Road;
import com.e4motion.challenge.api.dto.RoadDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RoadMapper {
    RoadDto toRoadDto(Road road);

    List<RoadDto> toRoadDto(List<Road> roads);
}
