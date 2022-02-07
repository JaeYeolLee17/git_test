package com.e4motion.challenge.api.domain;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Component;

@Component
public class UserRepository {
	
	private String userId;
	private String password;
	private String username;
	private String email;
	private Set<Authority> authorities;
	
	public UserRepository() {
		userId = "user1";
		password = "$2a$10$aQbZAm7JtqSdnFe61HDyz.HOJllg5OgRUA7D.d9KA/6hGnLuXzEI.";
		authorities = new HashSet<>();
		authorities.add(new Authority(Authority.ROLE_USER));
		authorities.add(new Authority(Authority.ROLE_ADMIN));
	}
	
	public void save(User user) {
		userId = user.getUserId();
		password = user.getPassword();
		username = user.getUsername();
		email = user.getEmail();
	}
	
	public Optional<User> findByUserId(String userId) {
		User user = new User(this.userId, password, username, email, authorities);
		user.setAuthorities(authorities);
		return Optional.ofNullable(user);
	}
	
}
