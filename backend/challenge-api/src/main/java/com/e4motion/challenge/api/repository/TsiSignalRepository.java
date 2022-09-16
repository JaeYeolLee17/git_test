package com.e4motion.challenge.api.repository;

import com.e4motion.challenge.api.domain.TsiSignal;
import com.e4motion.challenge.api.constant.TsiSignalInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface TsiSignalRepository extends JpaRepository<TsiSignal, Long> {

    @Transactional(readOnly = true)
    Optional<TsiSignal> findByTsi_NodeIdAndInfoAndDirection(Long nodeId, TsiSignalInfo info, Integer direction);

    @Transactional(readOnly = true)
    List<TsiSignal> findAllByTsi_NodeId(Long nodeId);

}
