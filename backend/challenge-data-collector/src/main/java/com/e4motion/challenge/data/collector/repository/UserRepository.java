package com.e4motion.challenge.data.collector.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.e4motion.challenge.data.collector.domain.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
	
	@EntityGraph(attributePaths = "authorities")
	public Optional<User> findByUserId(String userId);
	
}
