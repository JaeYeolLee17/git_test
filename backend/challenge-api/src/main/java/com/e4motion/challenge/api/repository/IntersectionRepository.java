package com.e4motion.challenge.api.repository;

import com.e4motion.challenge.api.domain.Intersection;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface IntersectionRepository extends JpaRepository<Intersection, Long> {

    @Transactional(readOnly = true)
    Optional<Intersection> findByIntersectionNo(String intersectionNo);

    @Transactional(readOnly = true)
    List<Intersection> findAllByRegion_RegionNo(String regionNo, Sort sort);

    @Transactional
    void deleteByIntersectionNo(String intersectionNo);

}
