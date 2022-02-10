package com.e4motion.challenge.api.service;

import java.util.Collections;
import java.util.List;

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

        User user = User.builder()
                .userId(userDto.getUserId())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .username(userDto.getUsername())
                .email(userDto.getEmail())
                .authorities(Collections.singleton(new Authority(userDto.getAuthority())))
                .activated(true)
                .build();

        return userMapper.toUserDto(userRepository.save(user));
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
