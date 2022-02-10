package com.e4motion.challenge.api.service;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.e4motion.challenge.api.domain.dto.UserDto;
import com.e4motion.challenge.api.domain.entity.Authority;
import com.e4motion.challenge.api.domain.entity.User;
import com.e4motion.challenge.api.domain.mapper.UserMapper;
import com.e4motion.challenge.api.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService {
	
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    
    @Transactional
    public UserDto create(UserDto userDto) {

    	// TODO: duplicate
        User user = User.builder()
                .userId(userDto.getUserId())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .username(userDto.getUsername())
                .email(userDto.getEmail())
                .phone(userDto.getPhone())
                .authorities(Collections.singleton(new Authority(userDto.getAuthority())))
                .activated(true)
                .build();

        return userMapper.toUserDto(userRepository.save(user));
    }
    
    @Transactional
    public UserDto update(String userId, UserDto userDto) {
    	
    	User user = userRepository.findByUserId(userId).orElse(null);
    	if (user != null) {
    		if (userDto.getPassword() != null) {
        		user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        	}
    		if (userDto.getUsername() != null) {
        		user.setUsername(userDto.getUsername());
        	}
    		if (userDto.getEmail() != null) {
        		user.setEmail(userDto.getEmail());
        	}
    		if (userDto.getPhone() != null) {
        		user.setPhone(userDto.getPhone());
        	}
    		if (userDto.getAuthority() != null) {
    			Set<Authority> authorities = new HashSet<>();
    			authorities.add(new Authority(userDto.getAuthority()));
        		user.setAuthorities(authorities);
        	}
    		return userMapper.toUserDto(userRepository.save(user));
    	}
    	
        return null;
    }

    @Transactional
    public void delete(String userId) {
    	
    	User user = userRepository.findByUserId(userId).orElse(null);
    	if (user != null) {
    		userRepository.delete(user);
    	}
    }
    
    @Transactional(readOnly = true)
    public List<UserDto> getList() {
    	
        return userMapper.toUserDto(userRepository.findAll());
    }
    
    @Transactional(readOnly = true)
    public UserDto get(String userId) {
    	
        return userMapper.toUserDto(userRepository.findByUserId(userId).orElse(null));
    }
}
