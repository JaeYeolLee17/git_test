package com.e4motion.challenge.api.security;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.e4motion.challenge.api.dao.UserDao;
import com.e4motion.challenge.api.domain.User;
import com.e4motion.common.exception.customexception.UserNotFoundException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component("userDetailsService")
public class CustomUserDetailsService implements UserDetailsService {
	
	private final UserDao userDao;

	@Override
	@Transactional
	public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
		
		try {
			User user = userDao.get(username);
			return createUser(user);
		} catch (Exception e) {
			throw new UserNotFoundException("Invalid user id");
		}
	}

	private UserDetails createUser(User user) {
		
		List<GrantedAuthority> grantedAuthorities = user.getAuthorities().stream()
				.map(authority -> new SimpleGrantedAuthority(authority.getAuthorityName().toString()))
				.collect(Collectors.toList());
		
		return new CustomUser(user.getUserId(), 
				user.getPassword(),
				user.getUsername(),
				user.getEmail(),
				user.getPhone(),
				grantedAuthorities);
   }
	
}
