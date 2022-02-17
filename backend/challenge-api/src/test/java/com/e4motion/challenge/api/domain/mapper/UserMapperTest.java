package com.e4motion.challenge.api.domain.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import com.e4motion.challenge.api.domain.Authority;
import com.e4motion.challenge.api.domain.User;
import com.e4motion.challenge.api.dto.UserDto;
import com.e4motion.challenge.api.mapper.UserMapper;
import com.e4motion.challenge.common.domain.AuthorityName;

public class UserMapperTest {

	private UserMapper mapper = Mappers.getMapper(UserMapper.class);
	
	@Test
    public void toUserDto() {
		
		User user = User.builder()
				.userId("admin")
				.password("de27ad6167310d667c33d6e6f3fd2050eaa4941bc5cf5a2c820c5a35f3a292a0")
				.username("adminname")
				.email("admin@email...")
				.phone("01022223333")
				.authorities(Collections.singleton(new Authority(AuthorityName.ROLE_ADMIN)))
				.build();
		
		UserDto userDto = mapper.toUserDto(user);
		
		assertEqualsUserDtoInfoWithUser(userDto, user);
		
	}
	
	@Test
    public void toUserDtoList() {
		
		User user1 = User.builder()
				.userId("admin")
				.password("de27ad6167310d667c33d6e6f3fd2050eaa4941bc5cf5a2c820c5a35f3a292a0")
				.username("adminname")
				.email("admin@email...")
				.phone("01022223333")
				.authorities(Collections.singleton(new Authority(AuthorityName.ROLE_ADMIN)))
				.build();
		
		User user2 = User.builder()
				.userId("user1")
				.password("de27ad6167310d667c33d6e6f3fd2050eaa4941bc5cf5a2c820c5a35f3a292a0")
				.username("user1name")
				.email("user1@email...")
				.phone("01044445555")
				.authorities(Collections.singleton(new Authority(AuthorityName.ROLE_USER)))
				.build();
		
		List<User> users = new ArrayList<>();
		users.add(user1);
		users.add(user2);
		
		List<UserDto> userDtos = mapper.toUserDto(users);
		
		assertThat(userDtos.size()).isEqualTo(2);
		
		assertThat(userDtos.get(0).getPassword()).isNull();
		assertThat(userDtos.get(1).getPassword()).isNull();
		
		assertEqualsUserDtoInfoWithUser(userDtos.get(0), user1);
		assertEqualsUserDtoInfoWithUser(userDtos.get(1), user2);
		
	}
	
	private void assertEqualsUserDtoInfoWithUser(UserDto userDto, User user) {

		assertThat(userDto.getUserId()).isEqualTo(user.getUserId());
		assertThat(userDto.getPassword()).isNotEqualTo(user.getPassword());	
		assertThat(userDto.getUsername()).isEqualTo(user.getUsername());
		assertThat(userDto.getEmail()).isEqualTo(user.getEmail());
		assertThat(userDto.getPhone()).isEqualTo(user.getPhone());
		assertThat(userDto.getAuthority()).isEqualTo(user.getAuthorities().isEmpty() ? null : user.getAuthorities().iterator().next().getAuthorityName());
	}
}
