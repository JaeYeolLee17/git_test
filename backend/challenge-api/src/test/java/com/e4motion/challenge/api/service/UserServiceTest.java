package com.e4motion.challenge.api.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.e4motion.challenge.api.TestHelper;
import com.e4motion.challenge.api.dto.UserUpdateDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.e4motion.challenge.api.domain.Authority;
import com.e4motion.challenge.api.domain.User;
import com.e4motion.challenge.api.dto.UserDto;
import com.e4motion.challenge.api.mapper.UserMapper;
import com.e4motion.challenge.api.repository.UserRepository;
import com.e4motion.challenge.api.service.impl.UserServiceImpl;
import com.e4motion.challenge.common.exception.customexception.UserDuplicateException;
import com.e4motion.challenge.common.exception.customexception.UserNotFoundException;

@SpringBootTest
public class UserServiceTest {
	
	@Autowired 
	PasswordEncoder passwordEncoder;
	
	@Autowired 
	UserMapper userMapper;
	
    @Mock
    private UserRepository userRepository;
    
    private UserService userService;
	
	@BeforeEach 
	void setup() { 
		userService = new UserServiceImpl(userRepository, passwordEncoder, userMapper); 
	}
    
	@Test
	public void create() throws Exception {
		
		// given
		UserDto userDto = TestHelper.getUserDto1();

		// TODO: user UserMapper for UserDto -> User
		User newUser = User.builder()
				.userId(userDto.getUserId())
				.password(passwordEncoder.encode(userDto.getPassword()))
				.username(userDto.getUsername())
				.email(userDto.getEmail())
				.phone(userDto.getPhone())
				.authorities(Collections.singleton(new Authority(userDto.getAuthority())))
				.build();
		
		doReturn(Optional.ofNullable(null)).when(userRepository).findByUserId(userDto.getUserId());
		doReturn(newUser).when(userRepository).save(any());
		
		// when
		UserDto createdUserDto = userService.create(userDto);
		
		// then
		assertThat(createdUserDto).isNotNull();
		assertEqualsUserDtos(createdUserDto, userDto);
    }
	
	@Test
	public void createDuplicateUser() throws Exception {
		
		// given
		UserDto userDto = TestHelper.getUserDto1();

		// TODO: user UserMapper for UserDto -> User
		User newUser = User.builder()
				.userId(userDto.getUserId())
				.password(passwordEncoder.encode(userDto.getPassword()))
				.username(userDto.getUsername())
				.email(userDto.getEmail())
				.phone(userDto.getPhone())
				.authorities(Collections.singleton(new Authority(userDto.getAuthority())))
				.build();
		
		doReturn(Optional.of(newUser)).when(userRepository).findByUserId(userDto.getUserId());

		// when
		Exception ex = assertThrows(UserDuplicateException.class, () -> userService.create(userDto));

		// then
		assertThat(ex.getMessage()).isEqualTo(UserDuplicateException.USER_ID_ALREADY_EXISTS);
    }

	@Test
   	public void update() throws Exception {
		
		// given
		UserDto userDto = TestHelper.getUserDto2();
		UserUpdateDto userUpdateDto = TestHelper.getUserUpdateDto();

		// TODO: user UserMapper for UserDto -> User
		User user = User.builder()
				.userId(userDto.getUserId())
				.password(passwordEncoder.encode(userDto.getPassword()))
				.username(userDto.getUsername())
				.email(userDto.getEmail())
				.phone(userDto.getPhone())
				.authorities(Collections.singleton(new Authority(userDto.getAuthority())))
				.build();

		User updatedUser = User.builder()
				.userId(userDto.getUserId())
				.username(userUpdateDto.getUsername())
				.email(userUpdateDto.getEmail())
				.phone(userUpdateDto.getPhone())
				.authorities(Collections.singleton(new Authority(userUpdateDto.getAuthority())))
				.build();
		
		doReturn(Optional.of(user)).when(userRepository).findByUserId(userDto.getUserId());
		doReturn(updatedUser).when(userRepository).save(any());		// use any() due to parameter mismatch?
		
		// when
		UserDto updatedUserDto = userService.update(userDto.getUserId(), userUpdateDto);
 
		// then
		userDto.setUsername(userUpdateDto.getUsername());
		userDto.setEmail(userUpdateDto.getEmail());
		userDto.setPhone(userUpdateDto.getPhone());
		userDto.setAuthority(userUpdateDto.getAuthority());

		assertThat(updatedUserDto).isNotNull();
		assertEqualsUserDtos(updatedUserDto, userDto);
    }
	
	@Test
   	public void updateNonexistentUser() throws Exception {
		
		// given		
		UserDto userDto = TestHelper.getUserDto2();
		UserUpdateDto userUpdateDto = TestHelper.getUserUpdateDto();

		doReturn(Optional.ofNullable(null)).when(userRepository).findByUserId(userDto.getUserId());

		// when
		Exception ex = assertThrows(UserNotFoundException.class, () -> userService.update(userDto.getUserId(), userUpdateDto));

		// then
		assertThat(ex.getMessage()).isEqualTo(UserNotFoundException.INVALID_USER_ID);
    }
	
	@Test
   	public void delete() throws Exception {

		UserDto userDto = TestHelper.getUserDto1();

		// given
		doNothing().when(userRepository).deleteByUserId(userDto.getUserId());

		// when
		userService.delete(userDto.getUserId());
		
		// then
    }
	
	@Test
   	public void get() throws Exception {
		
		// given
		UserDto userDto = TestHelper.getUserDto1();

		// TODO: user UserMapper for UserDto -> User
		// TODO: DB 에서 조회 시 password 가 있나?
		User user = User.builder()
				.userId(userDto.getUserId())
				.username(userDto.getUsername())
				.email(userDto.getEmail())
				.phone(userDto.getPhone())
				.authorities(Collections.singleton(new Authority(userDto.getAuthority())))
				.build();
		
		doReturn(Optional.of(user)).when(userRepository).findByUserId(userDto.getUserId());
		
		// when
		UserDto foundUserDto = userService.get(userDto.getUserId());
		
		// then
		assertEqualsUserDtos(foundUserDto, userDto);
    }
	
	@Test
   	public void getList() throws Exception {
		
		// given
		UserDto userDto1 = TestHelper.getUserDto1();
		
		UserDto userDto2 = TestHelper.getUserDto2();
		
		List<UserDto> userDtos = new ArrayList<>();
		userDtos.add(userDto1);
		userDtos.add(userDto2);

		// TODO: user UserMapper for UserDto -> User
		// TODO: DB 에서 조회 시 password 가 있나?
		User user1 = User.builder()
				.userId(userDto1.getUserId())
				.username(userDto1.getUsername())
				.email(userDto1.getEmail())
				.phone(userDto1.getPhone())
				.authorities(Collections.singleton(new Authority(userDto1.getAuthority())))
				.build();

		// TODO: user UserMapper for UserDto -> User
		// TODO: DB 에서 조회 시 password 가 있나?
		User user2 = User.builder()
				.userId(userDto2.getUserId())
				.username(userDto2.getUsername())
				.email(userDto2.getEmail())
				.phone(userDto2.getPhone())
				.authorities(Collections.singleton(new Authority(userDto2.getAuthority())))
				.build();
		
		List<User> users = new ArrayList<>();
		users.add(user1);
		users.add(user2);
		
		doReturn(users).when(userRepository).findAll();
		
		// when
		List<UserDto> foundUserDtos = userService.getList();
		
		// then
		assertThat(foundUserDtos.size()).isEqualTo(userDtos.size());
		assertEqualsUserDtos(foundUserDtos.get(0), userDtos.get(0));
		assertEqualsUserDtos(foundUserDtos.get(1), userDtos.get(1));
    }

	private void assertEqualsUserDtos(UserDto userDto1, UserDto userDto2) {
		
		assertThat(userDto1.getUserId()).isEqualTo(userDto2.getUserId());
		assertThat(userDto1.getUsername()).isEqualTo(userDto2.getUsername());
		assertThat(userDto1.getEmail()).isEqualTo(userDto2.getEmail());
		assertThat(userDto1.getPhone()).isEqualTo(userDto2.getPhone());
		assertThat(userDto1.getAuthority()).isEqualTo(userDto2.getAuthority());
	}
}
