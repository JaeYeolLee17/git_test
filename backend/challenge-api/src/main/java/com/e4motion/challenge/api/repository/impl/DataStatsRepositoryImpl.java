package com.e4motion.challenge.api.repository.impl;

import com.e4motion.challenge.api.domain.QDataStats;
import com.e4motion.challenge.api.dto.StatsMfdDataDto;
import com.e4motion.challenge.api.dto.StatsMfdDto;
import com.e4motion.challenge.api.repository.DataStatsRepositoryCustom;
import com.e4motion.challenge.common.domain.FilterBy;
import com.e4motion.challenge.common.domain.GroupBy;
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
    public List<StatsMfdDto> getMdfStats(LocalDateTime startTime, LocalDateTime endTime, GroupBy groupBy, FilterBy filterBy, String filterId, Integer dayOfWeek) {

        // projections
        int index_srlu;
        int index_qtsrlu;
        Expression<?>[] projections;
        if (dayOfWeek != null) {
            projections = new Expression[] { dataStats.t.dayOfWeek() };
            index_srlu = 3;
            index_qtsrlu = 4;
        } else {
            projections = new Expression[] { dataStats.t.year(), dataStats.t.month(), dataStats.t.dayOfMonth() };
            index_srlu = 5;
            index_qtsrlu = 6;
        }

        if (GroupBy.CAMERA.equals(groupBy)) {
            projections = ArrayUtils.addAll(projections, dataStats.c);
            index_srlu++;
            index_qtsrlu++;
        } else if (GroupBy.INTERSECTION.equals(groupBy)) {
            projections = ArrayUtils.addAll(projections, dataStats.i);
            index_srlu++;
            index_qtsrlu++;
        } else if (GroupBy.REGION.equals(groupBy)) {
            projections = ArrayUtils.addAll(projections, dataStats.r);
            index_srlu++;
            index_qtsrlu++;
        }

        projections = ArrayUtils.addAll(projections,
                dataStats.t.hour(), dataStats.t.minute(),
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
        predicate.and(dataStats.t.between(startTime, endTime));

        if (filterId != null) {
            if (FilterBy.CAMERA.equals(filterBy)) {
                predicate.and(dataStats.c.eq(filterId));
            } else if (FilterBy.INTERSECTION.equals(filterBy)) {
                predicate.and(dataStats.i.eq(filterId));
            } else if (FilterBy.REGION.equals(filterBy)) {
                predicate.and(dataStats.r.eq(filterId));
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
        Integer lastYear = 0;
        Integer lastMonth = 0;
        Integer lastDay = 0;
        Integer lastDow = 0;
        String lastC = null;
        String lastI = null;
        String lastR = null;

        for (Tuple tuple : tuples) {

            Integer year = tuple.get(dataStats.t.year());
            Integer month = tuple.get(dataStats.t.month());
            Integer day = tuple.get(dataStats.t.dayOfMonth());
            Integer dow = tuple.get(dataStats.t.dayOfWeek());
            String c = tuple.get(dataStats.c);
            String i = tuple.get(dataStats.i);
            String r = tuple.get(dataStats.r);

            if (!Objects.equals(lastYear, year) || !Objects.equals(lastMonth, month) || !Objects.equals(lastDay, day) ||
                    !Objects.equals(lastDow, dow) ||
                    !Objects.equals(c, lastC) || !Objects.equals(i, lastI) || !Objects.equals(r, lastR)) {

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

            StatsMfdDataDto statsMfdDataDto = StatsMfdDataDto.builder()
                    .hour(tuple.get(dataStats.t.hour()))
                    .min(tuple.get(dataStats.t.minute()))
                    .srlu(tuple.get(index_srlu, Integer.class))
                    .qtsrlu(tuple.get(index_qtsrlu, Integer.class))
                    .build();

            assert statsMfdDto != null;
            statsMfdDto.getData().add(statsMfdDataDto);

            lastYear =  year;
            lastMonth = month;
            lastDay = day;
            lastDow = dow;
            lastC = c;
            lastI = i;
            lastR = r;
        }

        return statsMfdDtos;
    }
}
