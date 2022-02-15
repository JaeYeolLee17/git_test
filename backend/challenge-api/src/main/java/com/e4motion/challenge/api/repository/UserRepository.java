package com.e4motion.challenge.api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.e4motion.challenge.api.domain.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
	
	@Transactional(readOnly=true)
	@EntityGraph(attributePaths = "authorities")
	public Optional<User> findByUserId(String userId);
	
}
