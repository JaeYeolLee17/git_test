package com.e4motion.challenge.data.collector.security;

import com.e4motion.challenge.common.domain.AuthorityName;
import com.e4motion.challenge.common.exception.customexception.CameraNotFoundException;
import com.e4motion.challenge.data.collector.domain.Camera;
import com.e4motion.challenge.data.collector.repository.CameraRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Set;

@RequiredArgsConstructor
@Component("userDetailsService")
public class CustomUserDetailsService implements UserDetailsService {
	
	private final CameraRepository cameraRepository;

	@Override
	@Transactional
	public UserDetails loadUserByUsername(final String username) {

		return cameraRepository.findByCameraNo(username)
				.map(this::createUser)
				.orElseThrow(() -> new CameraNotFoundException(CameraNotFoundException.INVALID_CAMERA_NO));
	}

	private UserDetails createUser(Camera camera) {

		Set<GrantedAuthority> grantedAuthorities = Collections.singleton(
				new SimpleGrantedAuthority(AuthorityName.ROLE_CAMERA.toString()));

		return new CustomUser(camera.getCameraNo(),
				camera.getPassword(),
				camera.getSettingsUpdated(),
				grantedAuthorities);
   }
	
}
