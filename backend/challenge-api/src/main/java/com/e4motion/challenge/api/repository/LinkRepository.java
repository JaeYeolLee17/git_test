package com.e4motion.challenge.api.repository;

import com.e4motion.challenge.api.domain.Link;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface LinkRepository extends JpaRepository<Link, Long> {

    @Transactional(readOnly = true)
    Optional<Link> findByLinkId(Long linkId);

    @Transactional(readOnly = true)
    Optional<Link> findByStart_IntersectionNoAndEnd_IntersectionNo(String startNo, String endNo);

    @Transactional
    void deleteByLinkId(Long linkId);

    @Transactional
    void deleteByStart_IntersectionNoAndEnd_IntersectionNo(String startNo, String endNo);
}
