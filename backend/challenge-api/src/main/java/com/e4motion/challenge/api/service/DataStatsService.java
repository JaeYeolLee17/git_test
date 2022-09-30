package com.e4motion.challenge.api.service;

import com.e4motion.challenge.api.dto.StatsDailyDto;
import com.e4motion.challenge.api.dto.StatsLinkDto;
import com.e4motion.challenge.api.dto.StatsPeriodDto;
import com.e4motion.challenge.common.constant.DailyGroupBy;
import com.e4motion.challenge.common.constant.FilterBy;
import com.e4motion.challenge.common.constant.GroupBy;
import com.e4motion.challenge.api.dto.StatsMfdDto;
import com.e4motion.challenge.common.constant.Period;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface DataStatsService {

    List<StatsLinkDto> getLinkStats(LocalDateTime startTime, LocalDateTime endTime, FilterBy filterBy, String filterValue);

    List<StatsMfdDto> getMdfStats(LocalDateTime startTime, LocalDateTime endTime, Integer dayOfWeek, GroupBy groupBy, FilterBy filterBy, String filterValue);

    List<StatsPeriodDto> getPeriodStats(LocalDateTime startTime, LocalDateTime endTime, Period period, GroupBy groupBy, FilterBy filterBy, String filterValue);

    List<StatsDailyDto> getDailyStats(LocalDate date, DailyGroupBy groupBy, FilterBy filterBy, String filterValue);
}
