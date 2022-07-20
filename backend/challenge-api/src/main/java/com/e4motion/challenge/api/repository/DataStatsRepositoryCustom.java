package com.e4motion.challenge.api.repository;

import com.e4motion.challenge.common.domain.FilterBy;
import com.e4motion.challenge.common.domain.GroupBy;
import com.e4motion.challenge.api.dto.StatsMfdDto;

import java.time.LocalDateTime;
import java.util.List;

public interface DataStatsRepositoryCustom {

    List<StatsMfdDto> getMdfStats(LocalDateTime startTime, LocalDateTime endTime, GroupBy groupBy, FilterBy filterBy, String filterId, Integer dayOfWeek);

}
