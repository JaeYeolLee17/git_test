package com.e4motion.challenge.api.service.impl;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.e4motion.challenge.api.dao.UserDao;
import com.e4motion.challenge.api.domain.Authority;
import com.e4motion.challenge.api.domain.User;
import com.e4motion.challenge.api.dto.UserDto;
import com.e4motion.challenge.api.mapper.UserMapper;
import com.e4motion.challenge.api.service.UserService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
	
	private final UserDao userDao;
	private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    
    @Transactional(readOnly = true)
    public List<UserDto> getList() throws Exception {
    	
        return userMapper.toUserDto(userDao.getList());
    }
    
    @Transactional(readOnly = true)
    public UserDto get(String userId) throws Exception {
    	
        return userMapper.toUserDto(userDao.get(userId));
    }
    
    @Transactional
    public UserDto create(UserDto userDto) throws Exception {

        User user = User.builder()
                .userId(userDto.getUserId())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .username(userDto.getUsername())
                .email(userDto.getEmail())
                .phone(userDto.getPhone())
                .authorities(Collections.singleton(new Authority(userDto.getAuthority())))
                .activated(true)
                .build();

        return userMapper.toUserDto(userDao.create(user));
    }
    
    @Transactional
    public UserDto update(String userId, UserDto userDto) throws Exception {
		
    	User user = User.builder()
                .userId(userId)
                .password(userDto.getPassword() != null ? passwordEncoder.encode(userDto.getPassword()) : null)
                .username(userDto.getUsername())
                .email(userDto.getEmail())
                .phone(userDto.getPhone())
                .activated(true)
                .build();

    	Set<Authority> authorities = new HashSet<>();
		if (userDto.getAuthority() != null) {
			authorities.add(new Authority(userDto.getAuthority()));
    	}
		user.setAuthorities(authorities);
		
		return userMapper.toUserDto(userDao.update(user));
    }

    @Transactional
    public void delete(String userId) throws Exception {
    	
    	userDao.delete(userId);
    }
}
