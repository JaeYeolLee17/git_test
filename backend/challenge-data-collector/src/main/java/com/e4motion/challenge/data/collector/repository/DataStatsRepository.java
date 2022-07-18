package com.e4motion.challenge.data.collector.repository;

import com.e4motion.challenge.data.collector.domain.DataStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

public interface DataStatsRepository extends JpaRepository<DataStats, Long> {

    @Transactional(readOnly = true)
    Optional<DataStats> findByTAndC(LocalDateTime t, String c);

}
