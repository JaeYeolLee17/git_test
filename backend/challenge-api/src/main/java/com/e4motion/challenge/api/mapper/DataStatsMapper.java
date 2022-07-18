package com.e4motion.challenge.api.mapper;

import com.e4motion.challenge.api.domain.DataStats;
import com.e4motion.challenge.api.dto.DataStatsDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DataStatsMapper {

    @Mapping(target = "sr", expression = "java(new Integer[] {dataStats.getSr0(), dataStats.getSr1(), dataStats.getSr2(), dataStats.getSr3(), dataStats.getSr4()})")
    @Mapping(target = "qmsr", expression = "java(new Integer[] {dataStats.getQmsr0(), dataStats.getQmsr1(), dataStats.getQmsr2(), dataStats.getQmsr3(), dataStats.getQmsr4()})")
    @Mapping(target = "qtsr", expression = "java(new Integer[] {dataStats.getQtsr0(), dataStats.getQtsr1(), dataStats.getQtsr2(), dataStats.getQtsr3(), dataStats.getQtsr4()})")
    @Mapping(target = "lu", expression = "java(new Integer[] {dataStats.getLu0(), dataStats.getLu1(), dataStats.getLu2(), dataStats.getLu3(), dataStats.getLu4()})")
    @Mapping(target = "qmlu", expression = "java(new Integer[] {dataStats.getQmlu0(), dataStats.getQmlu1(), dataStats.getQmlu2(), dataStats.getQmlu3(), dataStats.getQmlu4()})")
    @Mapping(target = "qtlu", expression = "java(new Integer[] {dataStats.getQtlu0(), dataStats.getQtlu1(), dataStats.getQtlu2(), dataStats.getQtlu3(), dataStats.getQtlu4()})")
    DataStatsDto toDataStatsDto(DataStats dataStats);

    List<DataStatsDto> toDataStatsDto(List<DataStats> users);

}
