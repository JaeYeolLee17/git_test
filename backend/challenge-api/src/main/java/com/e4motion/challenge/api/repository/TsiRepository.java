package com.e4motion.challenge.api.repository;

import com.e4motion.challenge.api.domain.Tsi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface TsiRepository extends JpaRepository<Tsi, Long>, TsiRepositoryCustom {

    @Transactional(readOnly = true)
    Optional<Tsi> findByNodeId(Long nodeId);

}
