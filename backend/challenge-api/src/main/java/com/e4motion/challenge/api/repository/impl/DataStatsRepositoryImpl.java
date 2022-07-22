package com.e4motion.challenge.api.repository.impl;

import com.e4motion.challenge.api.domain.QDataStats;
import com.e4motion.challenge.api.dto.*;
import com.e4motion.challenge.api.repository.DataStatsRepositoryCustom;
import com.e4motion.challenge.common.domain.DailyGroupBy;
import com.e4motion.challenge.common.domain.FilterBy;
import com.e4motion.challenge.common.domain.GroupBy;
import com.e4motion.challenge.common.domain.Period;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Repository
public class DataStatsRepositoryImpl implements DataStatsRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    QDataStats dataStats = QDataStats.dataStats;

    @Transactional(readOnly = true)
    public List<StatsMfdDto> getMdfStats(LocalDateTime startTime, LocalDateTime endTime, Integer dayOfWeek, GroupBy groupBy, FilterBy filterBy, String filterValue) {

        // projections
        Expression<?>[] projections;
        if (dayOfWeek != null) {
            projections = new Expression[] { dataStats.t.dayOfWeek() };
        } else {
            projections = new Expression[] { dataStats.t.year(), dataStats.t.month(), dataStats.t.dayOfMonth() };
        }

        if (GroupBy.CAMERA.equals(groupBy)) {
            projections = ArrayUtils.addAll(projections, dataStats.c);
        } else if (GroupBy.INTERSECTION.equals(groupBy)) {
            projections = ArrayUtils.addAll(projections, dataStats.i);
        } else if (GroupBy.REGION.equals(groupBy)) {
            projections = ArrayUtils.addAll(projections, dataStats.r);
        }

        int srluIndex = projections.length  + 2;
        projections = ArrayUtils.addAll(projections,
                dataStats.t.hour(),
                dataStats.t.minute(),
                dataStats.sr0
                        .add(dataStats.sr1)
                        .add(dataStats.sr2)
                        .add(dataStats.sr3)
                        .add(dataStats.sr4)
                        .add(dataStats.lu0)
                        .add(dataStats.lu1)
                        .add(dataStats.lu2)
                        .add(dataStats.lu3)
                        .add(dataStats.lu4).sum(),
                dataStats.qtsr0
                        .add(dataStats.qtsr1)
                        .add(dataStats.qtsr2)
                        .add(dataStats.qtsr3)
                        .add(dataStats.qtsr4)
                        .add(dataStats.qtlu0)
                        .add(dataStats.qtlu1)
                        .add(dataStats.qtlu2)
                        .add(dataStats.qtlu3)
                        .add(dataStats.qtlu4).sum());

        // predicate
        BooleanBuilder predicate = new BooleanBuilder();
        predicate.and(dataStats.t.goe(startTime)).and(dataStats.t.lt(endTime));

        if (filterValue != null) {
            if (FilterBy.CAMERA.equals(filterBy)) {
                predicate.and(dataStats.c.eq(filterValue));
            } else if (FilterBy.INTERSECTION.equals(filterBy)) {
                predicate.and(dataStats.i.eq(filterValue));
            } else if (FilterBy.REGION.equals(filterBy)) {
                predicate.and(dataStats.r.eq(filterValue));
            }
        }

        if (dayOfWeek != null) {
            predicate.and(dataStats.t.dayOfWeek().eq(dayOfWeek));
        }

        // group by
        Expression<?>[] groupBys;
        if (dayOfWeek != null) {
            groupBys = new Expression[] { dataStats.t.dayOfWeek() };
        } else {
            groupBys = new Expression[] { dataStats.t.year(), dataStats.t.month(), dataStats.t.dayOfMonth() };
        }

        if (GroupBy.CAMERA.equals(groupBy)) {
            groupBys = ArrayUtils.addAll(groupBys, dataStats.c);
        } else if (GroupBy.INTERSECTION.equals(groupBy)) {
            groupBys = ArrayUtils.addAll(groupBys, dataStats.i);
        } else if (GroupBy.REGION.equals(groupBy)) {
            groupBys = ArrayUtils.addAll(groupBys, dataStats.r);
        }

        groupBys = ArrayUtils.addAll(groupBys, dataStats.t.hour(), dataStats.t.minute());

        // order by
        OrderSpecifier<?>[] orderBys;
        if (dayOfWeek != null) {
            orderBys = new OrderSpecifier[] { dataStats.t.dayOfWeek().asc() };
        } else {
            orderBys = new OrderSpecifier[] { dataStats.t.year().asc(), dataStats.t.month().asc(), dataStats.t.dayOfMonth().asc() };
        }

        if (GroupBy.CAMERA.equals(groupBy)) {
            orderBys = ArrayUtils.addAll(orderBys, dataStats.c.asc());
        } else if (GroupBy.INTERSECTION.equals(groupBy)) {
            orderBys = ArrayUtils.addAll(orderBys, dataStats.i.asc());
        } else if (GroupBy.REGION.equals(groupBy)) {
            orderBys = ArrayUtils.addAll(orderBys, dataStats.r.asc());
        }

        orderBys = ArrayUtils.addAll(orderBys, dataStats.t.hour().asc(), dataStats.t.minute().asc());

        List<Tuple> tuples = queryFactory
                .select(projections)
                .from(dataStats)
                .where(predicate)
                .groupBy(groupBys)
                .orderBy(orderBys)
                .fetch();

        ArrayList<StatsMfdDto> statsMfdDtos = new ArrayList<>();
        StatsMfdDto statsMfdDto = null;
        Integer lastYear = null, year;
        Integer lastMonth = null, month;
        Integer lastDay = null, day;
        Integer lastDow = null, dow;
        String lastC = null, c;
        String lastI = null, i;
        String lastR = null, r;

        for (Tuple tuple : tuples) {
            year = tuple.get(dataStats.t.year());
            month = tuple.get(dataStats.t.month());
            day = tuple.get(dataStats.t.dayOfMonth());
            dow = tuple.get(dataStats.t.dayOfWeek());
            c = tuple.get(dataStats.c);
            i = tuple.get(dataStats.i);
            r = tuple.get(dataStats.r);

            if (!Objects.equals(lastYear, year) || !Objects.equals(lastMonth, month) || !Objects.equals(lastDay, day) ||
                    !Objects.equals(lastDow, dow) ||
                    !Objects.equals(c, lastC) || !Objects.equals(i, lastI) || !Objects.equals(r, lastR)) {
                lastYear =  year;
                lastMonth = month;
                lastDay = day;
                lastDow = dow;
                lastC = c;
                lastI = i;
                lastR = r;

                statsMfdDto = StatsMfdDto.builder()
                        .year(year)
                        .month(month)
                        .day(day)
                        .dayOfWeek(dow)
                        .cameraNo(c)
                        .intersectionNo(i)
                        .regionNo(r)
                        .data(new ArrayList<>())
                        .build();

                statsMfdDtos.add(statsMfdDto);
            }

            int j = 0;
            StatsMfdDataDto statsMfdDataDto = StatsMfdDataDto.builder()
                    .hour(tuple.get(dataStats.t.hour()))
                    .min(tuple.get(dataStats.t.minute()))
                    .srlu(tuple.get(srluIndex + j++, Integer.class))
                    .qtsrlu(tuple.get(srluIndex + j, Integer.class))
                    .build();

            assert statsMfdDto != null;
            statsMfdDto.getData().add(statsMfdDataDto);
        }

        return statsMfdDtos;
    }

    public List<StatsPeriodDto> getPeriodStats(LocalDateTime startTime, LocalDateTime endTime, Period period, GroupBy groupBy, FilterBy filterBy, String filterValue) {

        // projections
        Expression<?>[] projections;
        if (Period.YEAR.equals(period)) {
            projections = new Expression[] { dataStats.t.year() };
        } else if (Period.MONTH.equals(period)) {
            projections = new Expression[] { dataStats.t.year(), dataStats.t.month() };
        } else if (Period.DAY.equals(period)) {
            projections = new Expression[] { dataStats.t.year(), dataStats.t.month(), dataStats.t.dayOfMonth() };
        } else if (Period.DAY_OF_WEEK.equals(period)) {
            projections = new Expression[] { dataStats.t.dayOfWeek() };
        } else {
            projections = new Expression[] { dataStats.t.yearWeek() };
        }

        if (GroupBy.CAMERA.equals(groupBy)) {
            projections = ArrayUtils.addAll(projections, dataStats.c);
        } else if (GroupBy.INTERSECTION.equals(groupBy)) {
            projections = ArrayUtils.addAll(projections, dataStats.i);
        } else if (GroupBy.REGION.equals(groupBy)) {
            projections = ArrayUtils.addAll(projections, dataStats.r);
        }

        int pIndex = projections.length  + 1;
        projections = ArrayUtils.addAll(projections,
                dataStats.t.hour(),
                dataStats.p.sum(),
                dataStats.sr0
                        .add(dataStats.sr1)
                        .add(dataStats.sr2)
                        .add(dataStats.sr3)
                        .add(dataStats.sr4)
                        .add(dataStats.lu0)
                        .add(dataStats.lu1)
                        .add(dataStats.lu2)
                        .add(dataStats.lu3)
                        .add(dataStats.lu4).sum(),
                dataStats.qtsr0
                        .add(dataStats.qtsr1)
                        .add(dataStats.qtsr2)
                        .add(dataStats.qtsr3)
                        .add(dataStats.qtsr4)
                        .add(dataStats.qtlu0)
                        .add(dataStats.qtlu1)
                        .add(dataStats.qtlu2)
                        .add(dataStats.qtlu3)
                        .add(dataStats.qtlu4).sum());

        // predicate
        BooleanBuilder predicate = new BooleanBuilder();
        predicate.and(dataStats.t.goe(startTime)).and(dataStats.t.lt(endTime));

        if (filterValue != null) {
            if (FilterBy.CAMERA.equals(filterBy)) {
                predicate.and(dataStats.c.eq(filterValue));
            } else if (FilterBy.INTERSECTION.equals(filterBy)) {
                predicate.and(dataStats.i.eq(filterValue));
            } else if (FilterBy.REGION.equals(filterBy)) {
                predicate.and(dataStats.r.eq(filterValue));
            }
        }

        // group by
        Expression<?>[] groupBys;
        if (Period.YEAR.equals(period)) {
            groupBys = new Expression[] { dataStats.t.year() };
        } else if (Period.MONTH.equals(period)) {
            groupBys = new Expression[] { dataStats.t.year(), dataStats.t.month() };
        } else if (Period.DAY.equals(period)) {
            groupBys = new Expression[] { dataStats.t.year(), dataStats.t.month(), dataStats.t.dayOfMonth() };
        } else if (Period.DAY_OF_WEEK.equals(period)) {
            groupBys = new Expression[] { dataStats.t.dayOfWeek() };
        } else {
            groupBys = new Expression[] { dataStats.t.yearWeek() };
        }

        if (GroupBy.CAMERA.equals(groupBy)) {
            groupBys = ArrayUtils.addAll(groupBys, dataStats.c);
        } else if (GroupBy.INTERSECTION.equals(groupBy)) {
            groupBys = ArrayUtils.addAll(groupBys, dataStats.i);
        } else if (GroupBy.REGION.equals(groupBy)) {
            groupBys = ArrayUtils.addAll(groupBys, dataStats.r);
        }

        groupBys = ArrayUtils.addAll(groupBys, dataStats.t.hour());

        // order by
        OrderSpecifier<?>[] orderBys;
        if (Period.YEAR.equals(period)) {
            orderBys = new OrderSpecifier[] { dataStats.t.year().asc() };
        } else if (Period.MONTH.equals(period)) {
            orderBys = new OrderSpecifier[] { dataStats.t.year().asc(), dataStats.t.month().asc() };
        } else if (Period.DAY.equals(period)) {
            orderBys = new OrderSpecifier[] { dataStats.t.year().asc(), dataStats.t.month().asc(), dataStats.t.dayOfMonth().asc() };
        } else if (Period.DAY_OF_WEEK.equals(period)) {
            orderBys = new OrderSpecifier[] { dataStats.t.dayOfWeek().asc() };
        } else {
            orderBys = new OrderSpecifier[] { dataStats.t.yearWeek().asc() };
        }

        if (GroupBy.CAMERA.equals(groupBy)) {
            orderBys = ArrayUtils.addAll(orderBys, dataStats.c.asc());
        } else if (GroupBy.INTERSECTION.equals(groupBy)) {
            orderBys = ArrayUtils.addAll(orderBys, dataStats.i.asc());
        } else if (GroupBy.REGION.equals(groupBy)) {
            orderBys = ArrayUtils.addAll(orderBys, dataStats.r.asc());
        }

        orderBys = ArrayUtils.addAll(orderBys, dataStats.t.hour().asc());

        List<Tuple> tuples = queryFactory
                .select(projections)
                .from(dataStats)
                .where(predicate)
                .groupBy(groupBys)
                .orderBy(orderBys)
                .fetch();

        ArrayList<StatsPeriodDto> statsPeriodDtos = new ArrayList<>();
        StatsPeriodDto statsPeriodDto = null;
        Integer lastYear = null, year;
        Integer lastMonth = null, month;
        Integer lastDay = null, day;
        Integer lastDow = null, dow;
        Integer lastYw = null, yw;
        String lastC = null, c;
        String lastI = null, i;
        String lastR = null, r;

        for (Tuple tuple : tuples) {
            year = tuple.get(dataStats.t.year());
            month = tuple.get(dataStats.t.month());
            day = tuple.get(dataStats.t.dayOfMonth());
            dow = tuple.get(dataStats.t.dayOfWeek());
            yw = tuple.get(dataStats.t.yearWeek());
            c = tuple.get(dataStats.c);
            i = tuple.get(dataStats.i);
            r = tuple.get(dataStats.r);

            if (!Objects.equals(lastYear, year) || !Objects.equals(lastMonth, month) || !Objects.equals(lastDay, day) ||
                    !Objects.equals(lastDow, dow) || !Objects.equals(lastYw, yw) ||
                    !Objects.equals(c, lastC) || !Objects.equals(i, lastI) || !Objects.equals(r, lastR)) {
                lastYear = year;
                lastMonth = month;
                lastDay = day;
                lastDow = dow;
                lastYw = yw;
                lastC = c;
                lastI = i;
                lastR = r;

                if ( yw != null) {
                    year = yw/100;      // get year. 202226 -> 2022
                    yw = yw%100;        // get week. 202226 -> 26
                }

                statsPeriodDto = StatsPeriodDto.builder()
                        .year(year)
                        .month(month)
                        .day(day)
                        .dayOfWeek(dow)
                        .weekOfYear(yw)
                        .cameraNo(c)
                        .intersectionNo(i)
                        .regionNo(r)
                        .data(new ArrayList<>())
                        .build();

                statsPeriodDtos.add(statsPeriodDto);
            }

            int j = 0;
            StatsPeriodDataDto statsPeriodDataDto = StatsPeriodDataDto.builder()
                    .hour(tuple.get(dataStats.t.hour()))
                    .p(tuple.get(pIndex + j++, Integer.class))
                    .srlu(tuple.get(pIndex + j++, Integer.class))
                    .qtsrlu(tuple.get(pIndex + j, Integer.class))
                    .build();

            assert statsPeriodDto != null;
            statsPeriodDto.getData().add(statsPeriodDataDto);
        }

        return statsPeriodDtos;
    }

    public List<StatsDailyDto> getDailyStats(LocalDate date, DailyGroupBy groupBy, FilterBy filterBy, String filterValue) {

        // projections
        Expression<?>[] projections = new Expression[] { dataStats.t.year(), dataStats.t.month(), dataStats.t.dayOfMonth() };

        if (DailyGroupBy.CAMERA.equals(groupBy)) {
            projections = ArrayUtils.addAll(projections, dataStats.c);
        }

        int pIndex = projections.length  + 1;
        projections = ArrayUtils.addAll(projections, dataStats.t.hour(), dataStats.p.sum() );

        if (DailyGroupBy.CAMERA.equals(groupBy)) {
            projections = ArrayUtils.addAll(projections,
                    dataStats.sr0
                            .add(dataStats.sr1)
                            .add(dataStats.sr2)
                            .add(dataStats.sr3)
                            .add(dataStats.sr4)
                            .add(dataStats.lu0)
                            .add(dataStats.lu1)
                            .add(dataStats.lu2)
                            .add(dataStats.lu3)
                            .add(dataStats.lu4).sum(),
                    dataStats.qtsr0
                            .add(dataStats.qtsr1)
                            .add(dataStats.qtsr2)
                            .add(dataStats.qtsr3)
                            .add(dataStats.qtsr4)
                            .add(dataStats.qtlu0)
                            .add(dataStats.qtlu1)
                            .add(dataStats.qtlu2)
                            .add(dataStats.qtlu3)
                            .add(dataStats.qtlu4).sum());
        } else {
            projections = ArrayUtils.addAll(projections,
                    dataStats.sr0.add(dataStats.lu0).sum(),
                    dataStats.sr1.add(dataStats.lu1).sum(),
                    dataStats.sr2.add(dataStats.lu2).sum(),
                    dataStats.sr3.add(dataStats.lu3).sum(),
                    dataStats.sr4.add(dataStats.lu4).sum(),
                    dataStats.qtsr0.add(dataStats.qtlu0).sum(),
                    dataStats.qtsr1.add(dataStats.qtlu1).sum(),
                    dataStats.qtsr2.add(dataStats.qtlu2).sum(),
                    dataStats.qtsr3.add(dataStats.qtlu3).sum(),
                    dataStats.qtsr4.add(dataStats.qtlu4).sum());
        }

        // predicate
        BooleanBuilder predicate = new BooleanBuilder();
        predicate.and(dataStats.t.goe(date.atStartOfDay())).and(dataStats.t.lt(date.plusDays(1).atStartOfDay()));

        if (filterValue != null) {
            if (FilterBy.CAMERA.equals(filterBy)) {
                predicate.and(dataStats.c.eq(filterValue));
            } else if (FilterBy.INTERSECTION.equals(filterBy)) {
                predicate.and(dataStats.i.eq(filterValue));
            } else if (FilterBy.REGION.equals(filterBy)) {
                predicate.and(dataStats.r.eq(filterValue));
            }
        }

        // group by
        Expression<?>[] groupBys = new Expression[] { dataStats.t.year(), dataStats.t.month(), dataStats.t.dayOfMonth() };

        if (DailyGroupBy.CAMERA.equals(groupBy)) {
            groupBys = ArrayUtils.addAll(groupBys, dataStats.c);
        }

        groupBys = ArrayUtils.addAll(groupBys, dataStats.t.hour());

        // order by
        OrderSpecifier<?>[] orderBys = new OrderSpecifier[] { dataStats.t.year().asc(), dataStats.t.month().asc(), dataStats.t.dayOfMonth().asc() };

        if (DailyGroupBy.CAMERA.equals(groupBy)) {
            orderBys = ArrayUtils.addAll(orderBys, dataStats.c.asc());
        }

        orderBys = ArrayUtils.addAll(orderBys, dataStats.t.hour().asc());

        List<Tuple> tuples = queryFactory
                .select(projections)
                .from(dataStats)
                .where(predicate)
                .groupBy(groupBys)
                .orderBy(orderBys)
                .fetch();

        ArrayList<StatsDailyDto> statsDailyDtos = new ArrayList<>();
        StatsDailyDto statsDailyDto = null;
        Integer lastYear = null, year;
        Integer lastMonth = null, month;
        Integer lastDay = null, day;
        String lastC = null, c;

        for (Tuple tuple : tuples) {
            year = tuple.get(dataStats.t.year());
            month = tuple.get(dataStats.t.month());
            day = tuple.get(dataStats.t.dayOfMonth());
            c = tuple.get(dataStats.c);

            if (!Objects.equals(lastYear, year) || !Objects.equals(lastMonth, month) || !Objects.equals(lastDay, day) ||
                    !Objects.equals(c, lastC)) {
                lastYear = year;
                lastMonth = month;
                lastDay = day;
                lastC = c;

                statsDailyDto = StatsDailyDto.builder()
                        .year(year)
                        .month(month)
                        .day(day)
                        .cameraNo(c)
                        .data(new ArrayList<>())
                        .build();

                statsDailyDtos.add(statsDailyDto);
            }

            StatsDailyDataDto statsDailyDataDto;
            int j = 0;
            if (DailyGroupBy.CAMERA.equals(groupBy)) {
                statsDailyDataDto = StatsDailyDataDto.builder()
                        .hour(tuple.get(dataStats.t.hour()))
                        .p(tuple.get(pIndex + j++, Integer.class))
                        .srluSum(tuple.get(pIndex + j++, Integer.class))
                        .qtsrluSum(tuple.get(pIndex + j, Integer.class))
                        .build();
            } else {
                statsDailyDataDto = StatsDailyDataDto.builder()
                        .hour(tuple.get(dataStats.t.hour()))
                        .p(tuple.get(pIndex + j++, Integer.class))
                        .srlu( new Integer[] {
                                tuple.get(pIndex + j++, Integer.class),
                                tuple.get(pIndex + j++, Integer.class),
                                tuple.get(pIndex + j++, Integer.class),
                                tuple.get(pIndex + j++, Integer.class),
                                tuple.get(pIndex + j++, Integer.class)
                        })
                        .qtsrlu(new Integer[] {
                                tuple.get(pIndex + j++, Integer.class),
                                tuple.get(pIndex + j++, Integer.class),
                                tuple.get(pIndex + j++, Integer.class),
                                tuple.get(pIndex + j++, Integer.class),
                                tuple.get(pIndex + j, Integer.class)
                        })
                        .build();
            }

            assert statsDailyDto != null;
            statsDailyDto.getData().add(statsDailyDataDto);
        }

        return statsDailyDtos;
    }

}
