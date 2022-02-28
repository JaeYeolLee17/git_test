package com.e4motion.challenge.data.provider.repository;

import com.e4motion.challenge.data.provider.domain.User;

import java.util.Optional;

public interface UserRepository {
	
	Optional<User> findByUserId(String userId);
	
}
