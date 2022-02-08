package com.e4motion.challenge.api.security;

import static java.lang.String.format;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.e4motion.challenge.api.entity.User;
import com.e4motion.challenge.api.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component("userDetailsService")
public class CustomUserDetailsService implements UserDetailsService {
	
	private final UserRepository userRepository;

	@Override
	@Transactional
	public UserDetails loadUserByUsername(final String username) {
		
		return userRepository.findByUserId(username)
				.map(user -> createUser(user))
				.orElseThrow(() -> new UsernameNotFoundException(
						format("User: %s, not found", username)
						));
	}

	private org.springframework.security.core.userdetails.User createUser(User user) {
		
		List<GrantedAuthority> grantedAuthorities = user.getAuthorities().stream()
				.map(authority -> new SimpleGrantedAuthority(authority.getAuthorityName()))
				.collect(Collectors.toList());
		
		return new org.springframework.security.core.userdetails.User(user.getUserId(),
				user.getPassword(),
				grantedAuthorities);
   }
	
}
