package com.e4motion.challenge.api.service.impl;

import com.e4motion.challenge.api.domain.Authority;
import com.e4motion.challenge.api.domain.User;
import com.e4motion.challenge.api.dto.UserDto;
import com.e4motion.challenge.api.dto.UserUpdateDto;
import com.e4motion.challenge.api.mapper.UserMapper;
import com.e4motion.challenge.api.repository.UserRepository;
import com.e4motion.challenge.api.service.UserService;
import com.e4motion.challenge.common.exception.customexception.UnauthorizedException;
import com.e4motion.challenge.common.exception.customexception.UserDuplicateException;
import com.e4motion.challenge.common.exception.customexception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
	
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
	private final EntityManager entityManager;
    
    @Transactional
    public UserDto create(UserDto userDto) {

    	userRepository.findByUserId(userDto.getUserId())
				.ifPresent(user -> {
					throw new UserDuplicateException(UserDuplicateException.USER_ID_ALREADY_EXISTS);
				});

		userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
		userDto.setEnabled(true);
		User user = userMapper.toUser(userDto);

        return userMapper.toUserDto(userRepository.save(user));
    }
    
    @Transactional
    public UserDto update(String userId, UserUpdateDto userUpdateDto) {

    	return userRepository.findByUserId(userId)
				.map(user -> {
					if (userUpdateDto.getNewPassword() != null) {
						if (userUpdateDto.getOldPassword() == null || !passwordEncoder.matches(userUpdateDto.getOldPassword(), user.getPassword())) {
							throw new UnauthorizedException(UnauthorizedException.INVALID_PASSWORD);
						}
						user.setPassword(passwordEncoder.encode(userUpdateDto.getNewPassword()));
					}

					if (userUpdateDto.getUsername() != null) {
						user.setUsername(userUpdateDto.getUsername());
					}

					if (userUpdateDto.getEmail() != null) {
						user.setEmail(userUpdateDto.getEmail());
					}

					if (userUpdateDto.getPhone() != null) {
						user.setPhone(userUpdateDto.getPhone());
					}

					if (userUpdateDto.getAuthority() != null) {
						Set<Authority> authorities = new HashSet<>();	// Do not use Collections.singleton when save for update.
						authorities.add(new Authority(userUpdateDto.getAuthority()));
						user.setAuthorities(authorities);
					}

					User saved = userRepository.save(user);
					entityManager.flush();

					return userMapper.toUserDto(saved);
				})
				.orElseThrow(() -> new UserNotFoundException(UserNotFoundException.INVALID_USER_ID));
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
