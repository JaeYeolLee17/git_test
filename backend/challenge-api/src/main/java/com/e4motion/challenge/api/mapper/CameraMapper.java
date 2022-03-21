package com.e4motion.challenge.api.mapper;

import com.e4motion.challenge.api.domain.Camera;
import com.e4motion.challenge.api.dto.CameraDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CameraMapper {
    @Mapping(target = "direction", ignore = true)
    CameraDto toCameraDto(Camera camera);

    List<CameraDto> toCameraDto(List<Camera> cameras);
}
