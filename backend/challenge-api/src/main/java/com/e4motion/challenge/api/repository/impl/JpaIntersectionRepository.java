package com.e4motion.challenge.api.repository.impl;

import com.e4motion.challenge.api.domain.Intersection;
import com.e4motion.challenge.api.repository.IntersectionRepository;
import org.mapstruct.Mapping;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface JpaIntersectionRepository extends JpaRepository<Intersection, String>, IntersectionRepository {

    @Transactional(readOnly = true)
    Optional<Intersection> findByIntersectionId(String intersectionId);

    @Transactional
    void deleteByIntersectionId(String intersectionId);
}
