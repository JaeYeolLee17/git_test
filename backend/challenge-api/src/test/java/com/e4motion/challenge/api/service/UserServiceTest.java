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
import com.e4motion.challenge.common.domain.AuthorityName;
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
		UserDto newUserDto = UserDto.builder()
				.userId("admin")
				.password("password")
				.username("adminname")    
				.email("admin@email...")
				.phone("01022223333")
				.authority(AuthorityName.ROLE_ADMIN)
				.build();
		
		User newUser = User.builder()
				.userId(newUserDto.getUserId())
				.password(passwordEncoder.encode(newUserDto.getPassword()))
				.username(newUserDto.getUsername())
				.email(newUserDto.getEmail())
				.phone(newUserDto.getPhone())
				.authorities(Collections.singleton(new Authority(newUserDto.getAuthority())))
				.build();
		
		doReturn(Optional.ofNullable(null)).when(userRepository).findByUserId(newUserDto.getUserId());
		doReturn(newUser).when(userRepository).save(any());		// use any() due to parameter mismatch?
		
		// when
		UserDto userDto = userService.create(newUserDto);
		
		// then
		assertThat(userDto).isNotNull();
		assertEqualsUserDto(userDto, newUserDto);
    }
	
	@Test
	public void createDuplicateUser() throws Exception {
		
		// given
		UserDto newUserDto = UserDto.builder()
				.userId("admin")
				.password("password")
				.username("adminname")    
				.email("admin@email...")
				.phone("01022223333")
				.authority(AuthorityName.ROLE_ADMIN)
				.build();
		
		User newUser = User.builder()
				.userId(newUserDto.getUserId())
				.password(passwordEncoder.encode(newUserDto.getPassword()))
				.username(newUserDto.getUsername())
				.email(newUserDto.getEmail())
				.phone(newUserDto.getPhone())
				.authorities(Collections.singleton(new Authority(newUserDto.getAuthority())))
				.build();
		
		doReturn(Optional.of(newUser)).when(userRepository).findByUserId(newUserDto.getUserId());

		// when
		Exception ex = assertThrows(UserDuplicateException.class, () -> userService.create(newUserDto));

		// then
		assertThat(ex.getMessage()).isEqualTo(UserDuplicateException.USER_ID_ALREADY_EXISTS);
    }

	@Test
   	public void update() throws Exception {
		
		// given		
		UserDto updateUserDto = UserDto.builder()
				.userId("admin")
				.password("updated_password")
				.username("updated_adminname")    
				.email("updated_admin@email...")
				.phone("01022223333")
				.authority(AuthorityName.ROLE_ADMIN)
				.build();
		
		User updateUser = User.builder()
				.userId(updateUserDto.getUserId())
				.username(updateUserDto.getUsername())
				.email(updateUserDto.getEmail())
				.phone(updateUserDto.getPhone())
				.authorities(Collections.singleton(new Authority(updateUserDto.getAuthority())))
				.build();
		
		doReturn(Optional.of(updateUser)).when(userRepository).findByUserId(updateUserDto.getUserId());
		doReturn(updateUser).when(userRepository).save(any());		// use any() due to parameter mismatch?
		
		// when
		UserDto userDto = userService.update(updateUserDto.getUserId(), updateUserDto);
 
		// then
		assertThat(userDto).isNotNull();
		assertEqualsUserDto(userDto, updateUserDto);
    }
	
	@Test
   	public void updateNonexistentUser() throws Exception {
		
		// given		
		UserDto updateUserDto = UserDto.builder()
				.userId("admin")
				.password("updated_password")
				.username("updated_adminname")    
				.email("updated_admin@email...")
				.phone("01022223333")
				.authority(AuthorityName.ROLE_ADMIN)
				.build();
		
		User updateUser = User.builder()
				.userId(updateUserDto.getUserId())
				.username(updateUserDto.getUsername())
				.email(updateUserDto.getEmail())
				.phone(updateUserDto.getPhone())
				.authorities(Collections.singleton(new Authority(updateUserDto.getAuthority())))
				.build();
		
		doReturn(Optional.ofNullable(null)).when(userRepository).findByUserId(updateUserDto.getUserId());
		doReturn(updateUser).when(userRepository).save(any());		// use any() due to parameter mismatch?

		// when
		Exception ex = assertThrows(UserNotFoundException.class, () -> userService.update(updateUserDto.getUserId(), updateUserDto));

		// then
		assertThat(ex.getMessage()).isEqualTo(UserNotFoundException.INVALID_USER_ID);
    }
	
	@Test
   	public void delete() throws Exception {
		
		String userId = "user1";
		
		// given
		doNothing().when(userRepository).deleteByUserId(userId);
		
		// when
		userService.delete(userId);
		
		// then
    }
	
	@Test
   	public void get() throws Exception {
		
		// given
		UserDto userDto = UserDto.builder()
				.userId("user1")
				.password("password1")
				.username("username1")    
				.email("user1@email...")
				.phone("01022223333")
				.authority(AuthorityName.ROLE_USER)
				.build();
		
		User user = User.builder()
				.userId(userDto.getUserId())
				.username(userDto.getUsername())
				.email(userDto.getEmail())
				.phone(userDto.getPhone())
				.authorities(Collections.singleton(new Authority(userDto.getAuthority())))
				.build();
		
		doReturn(Optional.of(user)).when(userRepository).findByUserId(userDto.getUserId());
		
		// when
		UserDto found = userService.get(userDto.getUserId());
		
		// then
		assertEqualsUserDto(found, userDto);
    }
	
	@Test
   	public void getList() throws Exception {
		
		// given
		UserDto userDto1 = UserDto.builder()
				.userId("user1")
				.password("password1")
				.username("username1")    
				.email("use1r@email...")
				.phone("01022223333")
				.authority(AuthorityName.ROLE_USER)
				.build();
		
		UserDto userDto2 = UserDto.builder()
				.userId("user2")
				.password("password2")
				.username("username2")    
				.email("user2@email...")
				.phone("01044445555")
				.authority(AuthorityName.ROLE_USER)
				.build();
		
		List<UserDto> userDtos = new ArrayList<>();
		userDtos.add(userDto1);
		userDtos.add(userDto2);
		
		User user1 = User.builder()
				.userId(userDto1.getUserId())
				.username(userDto1.getUsername())
				.email(userDto1.getEmail())
				.phone(userDto1.getPhone())
				.authorities(Collections.singleton(new Authority(userDto1.getAuthority())))
				.build();
		
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
		List<UserDto> found = userService.getList();
		
		// then
		assertThat(found.size()).isEqualTo(userDtos.size());
		assertEqualsUserDto(found.get(0), userDtos.get(0));
		assertEqualsUserDto(found.get(1), userDtos.get(1));
    }

	private void assertEqualsUserDto(UserDto userDto1, UserDto userDto2) {
		
		assertThat(userDto1.getUserId()).isEqualTo(userDto2.getUserId());
		assertThat(userDto1.getUsername()).isEqualTo(userDto2.getUsername());
		assertThat(userDto1.getEmail()).isEqualTo(userDto2.getEmail());
		assertThat(userDto1.getPhone()).isEqualTo(userDto2.getPhone());
		assertThat(userDto1.getAuthority()).isEqualTo(userDto2.getAuthority());
	}
	
}
