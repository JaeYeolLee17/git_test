package com.e4motion.challenge.api.domain.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import com.e4motion.challenge.api.domain.Authority;
import com.e4motion.challenge.api.domain.User;
import com.e4motion.challenge.api.dto.UserDto;
import com.e4motion.challenge.api.mapper.UserMapper;
import com.e4motion.challenge.common.domain.AuthorityName;

public class UserMapperTests {

	private UserMapper mapper = Mappers.getMapper(UserMapper.class);
	
	@Test
    public void mapperTests() {
		
		String testUserId = "admin";
		String testUsername = "adminname";
		String testEmail = "admin@email...";
		String testPhone = "01022223333";
		AuthorityName testAuthority = AuthorityName.ROLE_ADMIN;
		
		Set<Authority> authorities = new HashSet<>();
		authorities.add(new Authority(testAuthority));
		User user = User.builder()
				.userId(testUserId)
				.username(testUsername)
				.email(testEmail)
				.phone(testPhone)
				.authorities(authorities)
				.build();
		
		UserDto userDto = mapper.toUserDto(user);
		
		assertThat(userDto.getUserId()).isEqualTo(testUserId);
		assertThat(userDto.getUsername()).isEqualTo(testUsername);
		assertThat(userDto.getEmail()).isEqualTo(testEmail);
		assertThat(userDto.getPhone()).isEqualTo(testPhone);
		assertThat(userDto.getAuthority()).isEqualTo(testAuthority);
		
	}
	
	@Test
    public void mapperListTests() {
		
		String testUserId = "admin";
		String testUsername = "adminname";
		String testEmail = "admin@eamil...";
		String testPhone = "01022223333";
		AuthorityName testAuthority = AuthorityName.ROLE_ADMIN;
		
		String testUserId2 = "user1";
		String testUsername2 = "user1name";
		String testEmail2 = "user1@email...";
		String testPhone2 = "01044445555";
		AuthorityName testAuthority2 = AuthorityName.ROLE_USER;
		
		Set<Authority> authorities = new HashSet<>();
		authorities.add(new Authority(testAuthority));
		User user = User.builder()
				.userId(testUserId)
				.username(testUsername)
				.email(testEmail)
				.phone(testPhone)
				.authorities(authorities)
				.build();
		
		Set<Authority> authorities2 = new HashSet<>();
		authorities2.add(new Authority(testAuthority2));
		User user2 = User.builder()
				.userId(testUserId2)
				.username(testUsername2)
				.email(testEmail2)
				.phone(testPhone2)
				.authorities(authorities2)
				.build();
		
		List<User> users = new ArrayList<>();
		users.add(user);
		users.add(user2);
		
		List<UserDto> userDtos = mapper.toUserDto(users);
		
		assertThat(userDtos.size()).isEqualTo(2);
		
		assertThat(userDtos.get(0).getUserId()).isEqualTo(testUserId);
		assertThat(userDtos.get(0).getUsername()).isEqualTo(testUsername);
		assertThat(userDtos.get(0).getEmail()).isEqualTo(testEmail);
		assertThat(userDtos.get(0).getPhone()).isEqualTo(testPhone);
		assertThat(userDtos.get(0).getAuthority()).isEqualTo(testAuthority);
		
		assertThat(userDtos.get(1).getUserId()).isEqualTo(testUserId2);
		assertThat(userDtos.get(1).getUsername()).isEqualTo(testUsername2);
		assertThat(userDtos.get(1).getEmail()).isEqualTo(testEmail2);
		assertThat(userDtos.get(1).getPhone()).isEqualTo(testPhone2);
		assertThat(userDtos.get(1).getAuthority()).isEqualTo(testAuthority2);
		
	}
}
