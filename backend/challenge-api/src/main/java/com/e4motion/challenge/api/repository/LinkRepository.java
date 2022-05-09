package com.e4motion.challenge.api.repository;

import com.e4motion.challenge.api.domain.Link;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface LinkRepository extends JpaRepository<Link, String> {

    @Transactional(readOnly = true)
    Optional<Link> findByLinkId(String linkId);

    @Transactional
    void deleteByLinkId(String linkId);

}
