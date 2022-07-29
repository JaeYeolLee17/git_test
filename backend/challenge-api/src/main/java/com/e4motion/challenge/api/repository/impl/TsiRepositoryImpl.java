package com.e4motion.challenge.api.repository.impl;

import com.e4motion.challenge.api.domain.*;
import com.e4motion.challenge.api.dto.*;
import com.e4motion.challenge.api.repository.TsiRepositoryCustom;
import com.e4motion.challenge.common.domain.TsiFilterBy;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Repository
public class TsiRepositoryImpl implements TsiRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    private final QTsi tsi = QTsi.tsi;
    private final QTsiSignal tsiSignal = QTsiSignal.tsiSignal;
    private final QTsiNode tsiNode = QTsiNode.tsiNode;
    private final QIntersection intersection = QIntersection.intersection;
    private final QRegion region = QRegion.region;

    private final static Integer[] directionDegrees = new Integer[] {0, 90, 180, 270, 45, 135, 225, 315};

    /*
    SELECT 	T.node_id, TN.lat, TN.lng,
        I.intersection_no, I.intersection_name, R.region_no, R.region_name,
		T.error_scu, T.error_center, T.error_contradiction,
		T.time,
		TS.direction, TS.info, TS.status
	FROM public.nt_tsi T
	LEFT JOIN nt_tsi_node TN ON t.node_id = TN.node_id
	LEFT JOIN nt_intersection I ON t.node_id = I.national_id
	LEFT JOIN nt_region R ON I.region_id = R.region_id
	LEFT JOIN nt_tsi_signal TS ON T.tsi_id = TS.tsi_id
	WHERE I.region_id is not NULL
		AND R.region_no = 'R01'
		AND I.intersection_no = 'I0012'
	ORDER BY T.node_id, TS.direction, TS.info;
     */
    @Transactional(readOnly = true)
    public List<TsiDto> getTsiList(TsiFilterBy filterBy, String filterValue) {

        // projections
        Expression<?>[] projections = new Expression[] {
                tsi.nodeId, tsiNode.lat, tsiNode.lng,
                intersection.intersectionNo, intersection.intersectionName, region.regionNo, region.regionName,
                tsi.errorScu, tsi.errorCenter, tsi.errorContradiction, tsi.time,
                tsiSignal.direction, tsiSignal.info, tsiSignal.status
        };

        // predicate
        BooleanBuilder predicate = new BooleanBuilder();
        if (TsiFilterBy.ALL_REGIONS.equals(filterBy)) {
            predicate.and(region.isNotNull());
        } else if (TsiFilterBy.REGION.equals(filterBy)) {
            predicate.and(region.regionNo.eq(filterValue));
        } else if (TsiFilterBy.INTERSECTION.equals(filterBy)) {
            predicate.and(intersection.intersectionNo.eq(filterValue));
        }

        // order by
        OrderSpecifier<?>[] orderBys = new OrderSpecifier[] {
                tsi.nodeId.asc(), tsiSignal.direction.asc(), tsiSignal.info.asc()
        };

        List<Tuple> tuples = queryFactory
                .select(projections)
                .from(tsi)
                .leftJoin(tsiNode).on(tsiNode.nodeId.eq(tsi.nodeId))
                .leftJoin(intersection).on(intersection.nationalId.eq(tsi.nodeId))
                .leftJoin(region).on(intersection.region.eq(region))
                .leftJoin(tsiSignal).on(tsiSignal.tsi.eq(tsi))
                .where(predicate)
                .orderBy(orderBys)
                .fetch();

        ArrayList<TsiDto> tsiDtos = new ArrayList<>();
        TsiDto tsiDto = null;
        TsiSignalDto tsiSignalDto = null;
        Long lastNodeId = null, nodeId;
        Integer lastDirection = null, direction;

        for (Tuple tuple : tuples) {
            nodeId = tuple.get(tsi.nodeId);

            if (!Objects.equals(lastNodeId, nodeId)) {
                lastNodeId =  nodeId;

                tsiDto = TsiDto.builder()
                        .nodeId(nodeId)
                        .gps(GpsDto.builder()
                                .lat(tuple.get(tsiNode.lat))
                                .lng(tuple.get(tsiNode.lng))
                                .build())
                        .error(Boolean.TRUE.equals(tuple.get(tsi.errorScu)) ||
                                Boolean.TRUE.equals(tuple.get(tsi.errorCenter)) ||
                                Boolean.TRUE.equals(tuple.get(tsi.errorContradiction)))
                        .time(tuple.get(tsi.time))
                        .intersection(IntersectionDto.builder()
                                .intersectionNo(tuple.get(intersection.intersectionNo))
                                .intersectionName(tuple.get(intersection.intersectionName))
                                .region(RegionDto.builder()
                                        .regionNo(tuple.get(region.regionNo))
                                        .regionName(tuple.get(region.regionName))
                                        .build())
                                .build())
                        .tsiSignals(new ArrayList<>())
                        .build();

                tsiDtos.add(tsiDto);
                lastDirection = null;
            }

            direction = tuple.get(tsiSignal.direction);
            if (!Objects.equals(lastDirection, direction)) {
                lastDirection = direction;

                tsiSignalDto = TsiSignalDto.builder()
                        .direction(getDirectionDegree(direction))
                        .tsiSignalInfos(new ArrayList<>())
                        .build();

                assert tsiDto != null;
                tsiDto.getTsiSignals().add(tsiSignalDto);
            }

            assert tsiSignalDto != null;
            tsiSignalDto.getTsiSignalInfos().add(TsiSignalInfoDto.builder()
                    .info(tuple.get(tsiSignal.info))
                    .status(tuple.get(tsiSignal.status))
                    .build());
        }

        return tsiDtos;
    }

    private Integer getDirectionDegree(Integer direction) {

        return directionDegrees[direction/10 - 1];
    }
}
