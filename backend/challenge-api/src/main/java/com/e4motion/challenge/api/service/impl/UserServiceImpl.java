package com.e4motion.challenge.api.service.impl;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.e4motion.challenge.api.domain.Authority;
import com.e4motion.challenge.api.domain.User;
import com.e4motion.challenge.api.dto.UserDto;
import com.e4motion.challenge.api.mapper.UserMapper;
import com.e4motion.challenge.api.repository.UserRepository;
import com.e4motion.challenge.api.service.UserService;
import com.e4motion.common.exception.customexception.UserDuplicateException;
import com.e4motion.common.exception.customexception.UserNotFoundException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
	
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    
    @Transactional
    public UserDto create(UserDto userDto) {

    	userRepository.findByUserId(userDto.getUserId())
				.ifPresent(user -> {
					throw new UserDuplicateException("User id already exists");
				});

        User user = User.builder()
                .userId(userDto.getUserId())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .username(userDto.getUsername())
                .email(userDto.getEmail())
                .phone(userDto.getPhone())
                .authorities(Collections.singleton(new Authority(userDto.getAuthority())))
                .build();

        return userMapper.toUserDto(userRepository.save(user));
    }
    
    @Transactional
    public UserDto update(String userId, UserDto userDto) {

    	return userRepository.findByUserId(userId)
				.map(user -> {
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
						Set<Authority> authorities = new HashSet<>();	// Do not use Collections.singleton when save for update.
						authorities.add(new Authority(userDto.getAuthority()));
						user.setAuthorities(authorities);
					}

					return userMapper.toUserDto(userRepository.save(user));
				})
				.orElseThrow(() -> new UserNotFoundException("Invalid user id"));
    }

    @Transactional
    public void delete(String userId) {
    	
    	userRepository.deleteByUserId(userId);
    }
    
    @Transactional(readOnly = true)
    public UserDto get(String userId) {
    	
        return userMapper.toUserDto(userRepository.findByUserId(userId).orElse(null));
    }
    
    @Transactional(readOnly = true)
    public List<UserDto> getList() {
    	
        return userMapper.toUserDto(userRepository.findAll());
    }
}
