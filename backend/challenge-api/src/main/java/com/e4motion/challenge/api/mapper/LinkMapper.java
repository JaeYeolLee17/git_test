package com.e4motion.challenge.api.mapper;

import com.e4motion.challenge.api.domain.Link;
import com.e4motion.challenge.api.domain.LinkGps;
import com.e4motion.challenge.api.dto.GpsDto;
import com.e4motion.challenge.api.dto.LinkDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LinkMapper {

    LinkDto toLinkDto(Link link);

    List<LinkDto> toLinkDto(List<Link> links);

    List<LinkGps> toGps(List<GpsDto> gpsDtos);
}
