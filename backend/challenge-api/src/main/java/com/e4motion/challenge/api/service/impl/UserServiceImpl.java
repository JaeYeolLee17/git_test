package com.e4motion.challenge.api.service.impl;

import com.e4motion.challenge.api.domain.Authority;
import com.e4motion.challenge.api.dto.UserDto;
import com.e4motion.challenge.api.dto.UserUpdateDto;
import com.e4motion.challenge.api.mapper.UserMapper;
import com.e4motion.challenge.api.repository.UserRepository;
import com.e4motion.challenge.api.service.UserService;
import com.e4motion.challenge.common.exception.customexception.UnauthorizedException;
import com.e4motion.challenge.common.exception.customexception.UserDuplicateException;
import com.e4motion.challenge.common.exception.customexception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
	
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    
    @Transactional
    public UserDto create(UserDto userDto) {

		checkIfUsernameExists(userDto.getUsername());

		userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));

        return userMapper.toUserDto(userRepository.save(userMapper.toUser(userDto)));
    }
    
    @Transactional
    public UserDto update(String username, UserUpdateDto userUpdateDto) {

    	return userRepository.findByUsername(username)
				.map(user -> {
					if (userUpdateDto.getUsername() != null && !userUpdateDto.getUsername().equals(user.getUsername())) {
						checkIfUsernameExists(userUpdateDto.getUsername());
						user.setUsername(userUpdateDto.getUsername());
					}

					if (userUpdateDto.getNewPassword() != null) {
						if (userUpdateDto.getOldPassword() == null || !passwordEncoder.matches(userUpdateDto.getOldPassword(), user.getPassword())) {
							throw new UnauthorizedException(UnauthorizedException.INVALID_PASSWORD);
						}
						user.setPassword(passwordEncoder.encode(userUpdateDto.getNewPassword()));
					}

					if (userUpdateDto.getName() != null) {
						user.setName(userUpdateDto.getName());
					}

					if (userUpdateDto.getEmail() != null) {
						user.setEmail(userUpdateDto.getEmail());
					}

					if (userUpdateDto.getPhone() != null) {
						user.setPhone(userUpdateDto.getPhone());
					}

					if (userUpdateDto.getDisabled() != null) {
						user.setDisabled(userUpdateDto.getDisabled());
					}

					if (userUpdateDto.getAuthority() != null) {
						Set<Authority> authorities = new HashSet<>();	// Do not use Collections.singleton when save for update.
						authorities.add(new Authority(userUpdateDto.getAuthority()));
						user.setAuthorities(authorities);
					}

					return userMapper.toUserDto(userRepository.saveAndFlush(user));
				})
				.orElseThrow(() -> new UserNotFoundException(UserNotFoundException.INVALID_USERNAME));
    }

    @Transactional
    public void delete(String username) {
    	
    	userRepository.deleteByUsername(username);
    }
    
    @Transactional(readOnly = true)
    public UserDto get(String username) {
    	
        return userMapper.toUserDto(userRepository.findByUsername(username).orElse(null));
    }
    
    @Transactional(readOnly = true)
    public List<UserDto> getList() {

		Sort sort = Sort.by("userId").ascending();
        return userMapper.toUserDto(userRepository.findAll(sort));
    }

	private void checkIfUsernameExists(String username) {

		userRepository.findByUsername(username)
				.ifPresent(user -> {
					throw new UserDuplicateException(UserDuplicateException.USERNAME_ALREADY_EXISTS);
				});
	}
}
