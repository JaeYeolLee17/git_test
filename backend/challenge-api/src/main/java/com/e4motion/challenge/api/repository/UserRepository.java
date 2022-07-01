package com.e4motion.challenge.api.repository;

import com.e4motion.challenge.api.domain.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
	
	@Transactional(readOnly = true)
	@EntityGraph(attributePaths = "authorities")
	Optional<User> findByUserId(Long userId);

	@Transactional(readOnly = true)
	@EntityGraph(attributePaths = "authorities")	// TODO: FetchType.EAGER 와 @EntityGraph 차이점 파악.
	Optional<User> findByUsername(String username);
	
	@Transactional
	void deleteByUserId(Long userId);
	
}
