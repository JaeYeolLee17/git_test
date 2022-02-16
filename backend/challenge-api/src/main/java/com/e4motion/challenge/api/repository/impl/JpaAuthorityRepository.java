package com.e4motion.challenge.api.repository.impl;

import org.springframework.data.jpa.repository.JpaRepository;

import com.e4motion.challenge.api.domain.Authority;

public interface JpaAuthorityRepository  extends JpaRepository<Authority, String> {

}
