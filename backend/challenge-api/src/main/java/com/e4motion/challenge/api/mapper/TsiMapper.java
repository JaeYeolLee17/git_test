package com.e4motion.challenge.api.mapper;

import com.e4motion.challenge.api.domain.Tsi;
import com.e4motion.challenge.api.domain.TsiSignal;
import com.e4motion.challenge.api.dto.TsiHubDto;
import com.e4motion.challenge.api.dto.TsiHubSignalDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TsiMapper {

    @Mapping(target = "tsiId", ignore = true)
    Tsi toTsi(TsiHubDto tsoHubDto);

    @Mapping(target = "tsiSignalId", ignore = true)
    @Mapping(target = "tsi", ignore = true)                 // should map manually because of tsi.
    TsiSignal toTsiSignal(TsiHubSignalDto tsiHubSignalDto);

}
