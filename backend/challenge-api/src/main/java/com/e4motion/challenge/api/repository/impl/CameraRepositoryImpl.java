package com.e4motion.challenge.api.repository.impl;

import com.e4motion.challenge.api.domain.Camera;
import com.e4motion.challenge.api.domain.QCamera;
import com.e4motion.challenge.api.repository.CameraRepositoryCustom;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class CameraRepositoryImpl implements CameraRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    QCamera camera = QCamera.camera;

    @Transactional(readOnly = true)
    public List<Camera> findAllByRegionNoIntersectionNo(String regionNo, String intersectionNo) {

        BooleanBuilder builder = new BooleanBuilder();
        if (regionNo != null) {
            builder.and(camera.intersection.region.regionNo.eq(regionNo));
        }
        if (intersectionNo != null) {
            builder.and(camera.intersection.intersectionNo.eq(intersectionNo));
        }

        return queryFactory
                .selectFrom(camera)
                .where(builder)
                .orderBy(camera.cameraNo.asc())
                .fetch();
    }
}
