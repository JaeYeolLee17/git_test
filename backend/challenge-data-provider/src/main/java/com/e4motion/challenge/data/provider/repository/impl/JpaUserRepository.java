package com.e4motion.challenge.data.provider.repository.impl;

import com.e4motion.challenge.data.provider.domain.User;
import com.e4motion.challenge.data.provider.repository.UserRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface JpaUserRepository extends JpaRepository<User, Long>, UserRepository {
	
	@Transactional(readOnly = true)
	@EntityGraph(attributePaths = "authorities")
	Optional<User> findByUserId(String userId);
	
}
