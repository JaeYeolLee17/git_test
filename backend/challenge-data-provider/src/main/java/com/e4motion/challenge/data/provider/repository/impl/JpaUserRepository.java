package com.e4motion.challenge.data.provider.repository.impl;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.e4motion.challenge.data.provider.domain.User;
import com.e4motion.challenge.data.provider.repository.UserRepository;

public interface JpaUserRepository extends JpaRepository<User, Long>, UserRepository {
	
	@Transactional(readOnly=true)
	@EntityGraph(attributePaths = "authorities")	// TODO: check actions.
	public Optional<User> findByUserId(String userId);
	
	@Transactional
	@EntityGraph(attributePaths = "authorities")	// TODO: check actions.
	public void deleteByUserId(String userId);
	
}
