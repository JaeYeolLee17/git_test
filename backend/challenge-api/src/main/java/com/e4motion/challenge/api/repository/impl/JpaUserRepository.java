package com.e4motion.challenge.api.repository.impl;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.e4motion.challenge.api.domain.User;
import com.e4motion.challenge.api.repository.UserRepository;

public interface JpaUserRepository extends JpaRepository<User, Long>, UserRepository {
	
	@Transactional(readOnly = true)
	@EntityGraph(attributePaths = "authorities")
	Optional<User> findByUserId(String userId);
	
	@Transactional
	@EntityGraph(attributePaths = "authorities")
	void deleteByUserId(String userId);
	
}
