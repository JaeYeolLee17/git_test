package com.e4motion.challenge.api.service.impl;

import com.e4motion.challenge.api.dto.StatsDailyDto;
import com.e4motion.challenge.api.dto.StatsLinkDto;
import com.e4motion.challenge.api.dto.StatsPeriodDto;
import com.e4motion.challenge.common.constant.DailyGroupBy;
import com.e4motion.challenge.common.constant.FilterBy;
import com.e4motion.challenge.common.constant.GroupBy;
import com.e4motion.challenge.api.dto.StatsMfdDto;
import com.e4motion.challenge.api.repository.DataStatsRepository;
import com.e4motion.challenge.api.service.DataStatsService;
import com.e4motion.challenge.common.constant.Period;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class DataStatsServiceImpl implements DataStatsService {

    private final DataStatsRepository dataStatsRepository;

    @Transactional(readOnly = true)
    public List<StatsLinkDto> getLinkStats(LocalDateTime startTime, LocalDateTime endTime, FilterBy filterBy, String filterValue) {

        return dataStatsRepository.getLinkStats(startTime, endTime, filterBy, filterValue);
    }

    @Transactional(readOnly = true)
    public List<StatsMfdDto> getMdfStats(LocalDateTime startTime, LocalDateTime endTime, Integer dayOfWeek, GroupBy groupBy, FilterBy filterBy, String filterValue) {

        return dataStatsRepository.getMdfStats(startTime, endTime, dayOfWeek, groupBy, filterBy, filterValue);
    }

    @Transactional(readOnly = true)
    public List<StatsPeriodDto> getPeriodStats(LocalDateTime startTime, LocalDateTime endTime, Period period, GroupBy groupBy, FilterBy filterBy, String filterValue) {

        return dataStatsRepository.getPeriodStats(startTime, endTime, period, groupBy, filterBy, filterValue);
    }

    @Transactional(readOnly = true)
    public List<StatsDailyDto> getDailyStats(LocalDate date, DailyGroupBy groupBy, FilterBy filterBy, String filterValue) {

        return dataStatsRepository.getDailyStats(date, groupBy, filterBy, filterValue);
    }
}
