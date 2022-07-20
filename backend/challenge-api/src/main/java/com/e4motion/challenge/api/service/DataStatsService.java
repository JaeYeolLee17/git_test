package com.e4motion.challenge.api.service;


import com.e4motion.challenge.common.domain.FilterBy;
import com.e4motion.challenge.common.domain.GroupBy;
import com.e4motion.challenge.api.dto.StatsMfdDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface DataStatsService {

    void getLink(LocalDateTime startTime, LocalDateTime endTime, FilterBy filterBy, String filterId);

    List<StatsMfdDto> getMfd(LocalDateTime startTime, LocalDateTime endTime, GroupBy groupBy, FilterBy filterBy, String filterId, Integer dayOfWeek);

    void getPeriod(LocalDateTime startTime, LocalDateTime endTime, String byPeriod, GroupBy groupBy, FilterBy filterBy, String filterId);

    void getDaily(LocalDate date, GroupBy groupBy, FilterBy filterBy, String filterId);
}
