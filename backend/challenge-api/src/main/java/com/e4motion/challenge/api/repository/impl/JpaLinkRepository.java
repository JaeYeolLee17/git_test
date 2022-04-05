package com.e4motion.challenge.api.repository.impl;

import com.e4motion.challenge.api.domain.Link;
import com.e4motion.challenge.api.repository.LinkRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface JpaLinkRepository extends JpaRepository<Link, String>, LinkRepository {

    @Transactional(readOnly = true)
    Optional<Link> findByLinkId(String linkId);

    @Transactional
    void deleteByLinkId(String linkId);
}
