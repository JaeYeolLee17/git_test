package com.e4motion.challenge.api.repository;

import java.util.List;
import java.util.Optional;

import com.e4motion.challenge.api.domain.User;

public interface UserRepository {
	
	User save(User user);
	
	List<User> findAll();
	
	void deleteAll();
	
	long count();
	
	Optional<User> findByUserId(String userId);

	void deleteByUserId(String userId);
	
}
