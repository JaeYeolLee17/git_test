package com.e4motion.challenge.api.mapper;

import com.e4motion.challenge.api.domain.Camera;
import com.e4motion.challenge.api.domain.Road;
import com.e4motion.challenge.api.dto.CameraDto;
import com.e4motion.challenge.api.dto.IntersectionDto;
import com.e4motion.challenge.api.dto.RoadDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CameraMapper {

    CameraDto toCameraDto(Camera camera);

    List<CameraDto> toCameraDto(List<Camera> cameras);

    @Mapping(source = "lanes", target = "lanes")
    RoadDto toRoadDto(Road road);

    Road toRoad(RoadDto roadDto);
}
