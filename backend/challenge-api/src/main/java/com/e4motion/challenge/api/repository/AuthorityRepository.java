package com.e4motion.challenge.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.e4motion.challenge.api.domain.entity.Authority;

public interface AuthorityRepository  extends JpaRepository<Authority, String> {

}
