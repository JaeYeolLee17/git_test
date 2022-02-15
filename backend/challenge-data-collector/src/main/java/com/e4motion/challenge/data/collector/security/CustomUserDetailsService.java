package com.e4motion.challenge.data.collector.security;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.e4motion.challenge.common.domain.AuthorityName;
import com.e4motion.challenge.data.collector.domain.entity.Camera;
import com.e4motion.challenge.data.collector.repository.CameraRepository;
import com.e4motion.common.exception.customexception.CameraNotFoundException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component("userDetailsService")
public class CustomUserDetailsService implements UserDetailsService {
	
	private final CameraRepository cameraRepository;

	@Override
	@Transactional
	public UserDetails loadUserByUsername(final String username) {

		return cameraRepository.findByCameraId(username)
				.map(camera -> createUser(camera))
				.orElseThrow(() -> new CameraNotFoundException("Invalid camera id"));
	}

	private UserDetails createUser(Camera camera) {
		
		Set<GrantedAuthority> grantedAuthorities = Collections.singleton(
				new SimpleGrantedAuthority(AuthorityName.ROLE_CAMERA.toString()));
		
		return new CustomUser(camera.getCameraId(), 
				camera.getPassword(),
				camera.isSettingsUpdated(),
				grantedAuthorities);
   }
	
}
