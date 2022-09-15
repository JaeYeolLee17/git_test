package com.e4motion.challenge.api.service;

import com.e4motion.challenge.api.TestDataHelper;
import com.e4motion.challenge.api.domain.Authority;
import com.e4motion.challenge.api.domain.User;
import com.e4motion.challenge.api.dto.UserCreateDto;
import com.e4motion.challenge.api.dto.UserDto;
import com.e4motion.challenge.api.dto.UserUpdateDto;
import com.e4motion.challenge.api.mapper.UserMapper;
import com.e4motion.challenge.api.repository.UserRepository;
import com.e4motion.challenge.api.service.impl.UserServiceImpl;
import com.e4motion.challenge.common.exception.customexception.UnauthorizedException;
import com.e4motion.challenge.common.exception.customexception.UserDuplicateException;
import com.e4motion.challenge.common.exception.customexception.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;

@SpringBootTest
@ActiveProfiles("test")
public class UserServiceTest {
	
	@Autowired 
	PasswordEncoder passwordEncoder;
	
	@Autowired 
	UserMapper userMapper;
	
    @Mock
    UserRepository userRepository;

    UserService userService;
	
	@BeforeEach 
	void setup() { 
		userService = new UserServiceImpl(userRepository, passwordEncoder, userMapper);
	}
    
	@Test
	public void create() throws Exception {
		
		// given
		UserCreateDto userCreateDto = TestDataHelper.getUserCreateDto1();

		userCreateDto.setPassword(passwordEncoder.encode(userCreateDto.getPassword()));
		User user = userMapper.toUser(userCreateDto);
		
		doReturn(Optional.empty()).when(userRepository).findByUsername(userCreateDto.getUsername());
		doReturn(user).when(userRepository).save(any());
		
		// when
		UserDto createdUserDto = userService.create(userCreateDto);
		
		// then
		assertThat(createdUserDto).isNotNull();
		assertEquals(createdUserDto, userCreateDto);
    }
	
	@Test
	public void createDuplicateUser() throws Exception {
		
		// given
		UserCreateDto userCreateDto = TestDataHelper.getUserCreateDto1();

		userCreateDto.setPassword(passwordEncoder.encode(userCreateDto.getPassword()));
		User user = userMapper.toUser(userCreateDto);

		doReturn(Optional.of(user)).when(userRepository).findByUsername(userCreateDto.getUsername());

		// when
		Exception ex = assertThrows(UserDuplicateException.class, () -> userService.create(userCreateDto));

		// then
		assertThat(ex.getMessage()).isEqualTo(UserDuplicateException.USERNAME_ALREADY_EXISTS);
    }

	@Test
   	public void update() throws Exception {
		
		// given
		UserDto userDto = TestDataHelper.getUserDto2();
		User user = User.builder()
				.userId(2L)
				.username(userDto.getUsername())
				.password(passwordEncoder.encode(userDto.getPassword()))
				.nickname(userDto.getNickname())
				.email(userDto.getEmail())
				.phone(userDto.getPhone())
				.authorities(Collections.singleton(new Authority(userDto.getAuthority())))
				.build();

		UserUpdateDto userUpdateDto = TestDataHelper.getUserUpdateDto();
		User updatedUser = User.builder()
				.userId(2L)
				.username(userUpdateDto.getUsername())
				.password(passwordEncoder.encode(userUpdateDto.getNewPassword()))
				.nickname(userUpdateDto.getNickname())
				.email(userUpdateDto.getEmail())
				.phone(userUpdateDto.getPhone())
				.authorities(Collections.singleton(new Authority(userUpdateDto.getAuthority())))
				.build();
		
		doReturn(Optional.of(user)).when(userRepository).findByUsername(userDto.getUsername());
		doReturn(updatedUser).when(userRepository).saveAndFlush(any());
		
		// when
		UserDto updatedUserDto = userService.update(userDto.getUsername(), userUpdateDto);
 
		// then
		assertThat(updatedUserDto).isNotNull();
		assertEquals(updatedUserDto, userUpdateDto);
    }

	@Test
	public void updateWithInvalidOldPassword() throws Exception {

		// given
		UserDto userDto = TestDataHelper.getUserDto2();
		User user = User.builder()
				.userId(2L)
				.username(userDto.getUsername())
				.password(passwordEncoder.encode(userDto.getPassword()))
				.nickname(userDto.getNickname())
				.email(userDto.getEmail())
				.phone(userDto.getPhone())
				.authorities(Collections.singleton(new Authority(userDto.getAuthority())))
				.build();

		UserUpdateDto userUpdateDto = TestDataHelper.getUserUpdateDto();
		userUpdateDto.setOldPassword("wrong old password...");

		doReturn(Optional.of(user)).when(userRepository).findByUsername(userDto.getUsername());

		// when
		Exception ex = assertThrows(UnauthorizedException.class, () -> userService.update(userDto.getUsername(), userUpdateDto));

		// then
		assertThat(ex.getMessage()).isEqualTo(UnauthorizedException.INVALID_PASSWORD);
	}
	
	@Test
   	public void updateNonexistentUser() throws Exception {
		
		// given		
		UserDto userDto = TestDataHelper.getUserDto2();
		UserUpdateDto userUpdateDto = TestDataHelper.getUserUpdateDto();

		doReturn(Optional.empty()).when(userRepository).findByUsername(userDto.getUsername());

		// when
		Exception ex = assertThrows(UserNotFoundException.class, () -> userService.update(userDto.getUsername(), userUpdateDto));

		// then
		assertThat(ex.getMessage()).isEqualTo(UserNotFoundException.INVALID_USERNAME);
    }
	
	@Test
   	public void delete() throws Exception {

		UserDto userDto = TestDataHelper.getUserDto1();

		// given
		doNothing().when(userRepository).deleteByUsername(userDto.getUsername());

		// when
		userService.delete(userDto.getUsername());
		
		// then
    }
	
	@Test
   	public void get() throws Exception {
		
		// given
		UserDto userDto = TestDataHelper.getUserDto1();
		User user = User.builder()
				.userId(1L)
				.username(userDto.getUsername())
				.password(passwordEncoder.encode(userDto.getPassword()))
				.nickname(userDto.getNickname())
				.email(userDto.getEmail())
				.phone(userDto.getPhone())
				.disabled(userDto.getDisabled())
				.authorities(Collections.singleton(new Authority(userDto.getAuthority())))
				.build();
		
		doReturn(Optional.of(user)).when(userRepository).findByUsername(userDto.getUsername());
		
		// when
		UserDto foundUserDto = userService.get(userDto.getUsername());
		
		// then
		assertEquals(foundUserDto, userDto);
    }
	
	@Test
   	public void getList() throws Exception {
		
		// given
		UserDto userDto1 = TestDataHelper.getUserDto1();
		User user1 = User.builder()
				.userId(1L)
				.username(userDto1.getUsername())
				.password(passwordEncoder.encode(userDto1.getPassword()))
				.nickname(userDto1.getNickname())
				.email(userDto1.getEmail())
				.phone(userDto1.getPhone())
				.disabled(userDto1.getDisabled())
				.authorities(Collections.singleton(new Authority(userDto1.getAuthority())))
				.build();

		UserDto userDto2 = TestDataHelper.getUserDto2();
		User user2 = User.builder()
				.userId(1L)
				.username(userDto2.getUsername())
				.password(passwordEncoder.encode(userDto2.getPassword()))
				.nickname(userDto2.getNickname())
				.email(userDto2.getEmail())
				.phone(userDto2.getPhone())
				.disabled(userDto2.getDisabled())
				.authorities(Collections.singleton(new Authority(userDto2.getAuthority())))
				.build();

		List<UserDto> userDtos = new ArrayList<>();
		userDtos.add(userDto1);
		userDtos.add(userDto2);
		
		List<User> users = new ArrayList<>();
		users.add(user1);
		users.add(user2);

		Sort sort = Sort.by("userId").ascending();
		doReturn(users).when(userRepository).findAll(sort);
		
		// when
		List<UserDto> foundUserDtos = userService.getList();
		
		// then
		assertThat(foundUserDtos.size()).isEqualTo(userDtos.size());
		assertEquals(foundUserDtos.get(0), userDtos.get(0));
		assertEquals(foundUserDtos.get(1), userDtos.get(1));
    }

	private void assertEquals(UserDto userDto, UserCreateDto userCreateDto) {

		assertThat(userDto.getUsername()).isEqualTo(userCreateDto.getUsername());
		assertThat(userDto.getNickname()).isEqualTo(userCreateDto.getNickname());
		assertThat(userDto.getEmail()).isEqualTo(userCreateDto.getEmail());
		assertThat(userDto.getPhone()).isEqualTo(userCreateDto.getPhone());
		assertThat(userDto.getAuthority()).isEqualTo(userCreateDto.getAuthority());
	}

	private void assertEquals(UserDto userDto, UserUpdateDto userUpdateDto) {

		assertThat(userDto.getUsername()).isEqualTo(userUpdateDto.getUsername());
		assertThat(userDto.getNickname()).isEqualTo(userUpdateDto.getNickname());
		assertThat(userDto.getEmail()).isEqualTo(userUpdateDto.getEmail());
		assertThat(userDto.getPhone()).isEqualTo(userUpdateDto.getPhone());
		assertThat(userDto.getAuthority()).isEqualTo(userUpdateDto.getAuthority());
	}

	private void assertEquals(UserDto userDto1, UserDto userDto2) {

		assertThat(userDto1.getUsername()).isEqualTo(userDto2.getUsername());
		assertThat(userDto1.getNickname()).isEqualTo(userDto2.getNickname());
		assertThat(userDto1.getEmail()).isEqualTo(userDto2.getEmail());
		assertThat(userDto1.getPhone()).isEqualTo(userDto2.getPhone());
		assertThat(userDto1.getDisabled()).isEqualTo(userDto2.getDisabled());
		assertThat(userDto1.getAuthority()).isEqualTo(userDto2.getAuthority());
	}
}
