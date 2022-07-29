package com.e4motion.challenge.api.repository;

import com.e4motion.challenge.api.domain.TsiNode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface TsiNodeRepository extends JpaRepository<TsiNode, Long> {

    @Transactional(readOnly = true)
    Optional<TsiNode> findByNodeId(Long nodeId);

}
