package com.e4motion.challenge.data.collector.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.e4motion.challenge.data.collector.domain.entity.Authority;

public interface AuthorityRepository  extends JpaRepository<Authority, String> {

}
