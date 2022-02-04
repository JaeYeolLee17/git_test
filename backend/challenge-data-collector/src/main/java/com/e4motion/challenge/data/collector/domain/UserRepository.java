package com.e4motion.challenge.data.collector.domain;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Component;

@Component
public class UserRepository {
	
	private String username;
	private String password;
	private Set<Role> authorities;
	
	public UserRepository() {
		username = "user1";
		password = "$2a$10$aQbZAm7JtqSdnFe61HDyz.HOJllg5OgRUA7D.d9KA/6hGnLuXzEI.";
		authorities = new HashSet<>();
		authorities.add(new Role(Role.USER_ADMIN));
		authorities.add(new Role(Role.AUTHOR_ADMIN));
	}
	
	public void save(User user) {
		username = user.getUsername();
		password = user.getPassword();
	}
	
	public Optional<User> findByUsername(String username) {
		User user = new User(this.username, password);
		user.setAuthorities(authorities);
		return Optional.ofNullable(user);
	}
}
