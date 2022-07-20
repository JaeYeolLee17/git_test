package com.e4motion.challenge.api.service.impl;

import com.e4motion.challenge.common.domain.FilterBy;
import com.e4motion.challenge.common.domain.GroupBy;
import com.e4motion.challenge.api.dto.StatsMfdDto;
import com.e4motion.challenge.api.repository.DataStatsRepository;
import com.e4motion.challenge.api.service.DataStatsService;
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
    public void getLink(LocalDateTime startTime, LocalDateTime endTime, FilterBy filterBy, String filterId) {

    }

    @Transactional(readOnly = true)
    public List<StatsMfdDto> getMfd(LocalDateTime startTime, LocalDateTime endTime, GroupBy groupBy, FilterBy filterBy, String filterId, Integer dayOfWeek) {

        return dataStatsRepository.getMdfStats(startTime, endTime, groupBy, filterBy, filterId, dayOfWeek);
    }

    @Transactional(readOnly = true)
    public void getPeriod(LocalDateTime startTime, LocalDateTime endTime, String byPeriod, GroupBy groupBy, FilterBy filterBy, String filterId) {

    }

    @Transactional(readOnly = true)
    public void getDaily(LocalDate date, GroupBy groupBy, FilterBy filterBy, String filterId) {

    }
}
