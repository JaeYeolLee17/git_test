package com.e4motion.challenge.api.repository;

import com.e4motion.challenge.api.domain.LinkGps;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface LinkGpsRepository extends JpaRepository<LinkGps, Long> {

    @Transactional(readOnly = true)
    List<LinkGps> findAllByLink_LinkIdOrderByGpsOrder(Long linkId);     // for unit tests.

}
