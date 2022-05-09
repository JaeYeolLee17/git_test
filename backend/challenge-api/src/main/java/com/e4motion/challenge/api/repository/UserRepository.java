package com.e4motion.challenge.api.repository;

import com.e4motion.challenge.api.domain.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
	
	@Transactional(readOnly = true)
	@EntityGraph(attributePaths = "authorities")
	Optional<User> findByUserId(String userId);
	
	@Transactional
	void deleteByUserId(String userId);
	
}
