package com.e4motion.challenge.data.provider.security;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.e4motion.challenge.data.provider.domain.User;
import com.e4motion.challenge.data.provider.repository.UserRepository;
import com.e4motion.challenge.common.exception.customexception.UserNotFoundException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component("userDetailsService")
public class CustomUserDetailsService implements UserDetailsService {

	private final UserRepository userRepository;

	@Override
	@Transactional
	public UserDetails loadUserByUsername(final String username) {

		return userRepository.findByUsername(username)
				.map(this::createUser)
				.orElseThrow(() -> new UserNotFoundException(UserNotFoundException.INVALID_USERNAME));
	}

	private UserDetails createUser(User user) {

		List<GrantedAuthority> grantedAuthorities = user.getAuthorities().stream()
				.map(authority -> new SimpleGrantedAuthority(authority.getAuthorityName().toString()))
				.collect(Collectors.toList());

		return new CustomUser(user.getUserId(),
				user.getUsername(),
				user.getPassword(),
				user.getNickname(),
				user.getEmail(),
				user.getPhone(),
				user.getDisabled(),
				grantedAuthorities);
	}

}
