package com.e4motion.challenge.api.repository;

import com.e4motion.challenge.api.dto.StatsDailyDto;
import com.e4motion.challenge.api.dto.StatsLinkDto;
import com.e4motion.challenge.api.dto.StatsPeriodDto;
import com.e4motion.challenge.common.domain.DailyGroupBy;
import com.e4motion.challenge.common.domain.FilterBy;
import com.e4motion.challenge.common.domain.GroupBy;
import com.e4motion.challenge.api.dto.StatsMfdDto;
import com.e4motion.challenge.common.domain.Period;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface DataStatsRepositoryCustom {

    List<StatsLinkDto> getLinkStats(LocalDateTime startTime, LocalDateTime endTime, FilterBy filterBy, String filterValue);

    List<StatsMfdDto> getMdfStats(LocalDateTime startTime, LocalDateTime endTime, Integer dayOfWeek, GroupBy groupBy, FilterBy filterBy, String filterValue);

    List<StatsPeriodDto> getPeriodStats(LocalDateTime startTime, LocalDateTime endTime, Period period, GroupBy groupBy, FilterBy filterBy, String filterValue);

    List<StatsDailyDto> getDailyStats(LocalDate date, DailyGroupBy groupBy, FilterBy filterBy, String filterValue);

}
