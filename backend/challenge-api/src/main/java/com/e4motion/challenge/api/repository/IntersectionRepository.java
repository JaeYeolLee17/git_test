package com.e4motion.challenge.api.repository;

import com.e4motion.challenge.api.domain.Intersection;

import java.util.List;
import java.util.Optional;

public interface IntersectionRepository {

    Intersection save(Intersection intersection);

    List<Intersection> findAll();

    void deleteAll();

    long count();

    Optional<Intersection> findByIntersectionId(String intersectionId);

    void deleteByIntersectionId(String intersectionId);
}
