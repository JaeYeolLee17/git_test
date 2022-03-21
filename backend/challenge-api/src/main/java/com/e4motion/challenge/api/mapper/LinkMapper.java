package com.e4motion.challenge.api.mapper;

import com.e4motion.challenge.api.domain.Link;
import com.e4motion.challenge.api.dto.LinkDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LinkMapper {
    @Mapping(target = "gps", ignore = true)
    LinkDto toLinkDto(Link link);

    List<LinkDto> toLinkDto(List<Link> links);
}
