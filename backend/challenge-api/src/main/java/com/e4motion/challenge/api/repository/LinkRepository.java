package com.e4motion.challenge.api.repository;

import com.e4motion.challenge.api.domain.Link;

import java.util.List;
import java.util.Optional;

public interface LinkRepository {

    Link save(Link link);

    List<Link> findAll();

    void deleteAll();

    long count();

    Optional<Link> findByLinkId(String linkId);

    void deleteByLinkId(String linkId);
}
