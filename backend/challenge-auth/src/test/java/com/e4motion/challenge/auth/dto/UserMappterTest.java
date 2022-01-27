package com.e4motion.challenge.auth.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import com.e4motion.challenge.auth.domain.user.User;

class UserMappterTest {

	@Test
	void testToDto() {
		User user = new User(1, 1, "userId111", "name111");
		
		UserMapper mapper = Mappers.getMapper(UserMapper.class);
		UserDto userDto = mapper.toDto(user);
		
		assertThat(userDto).isNotNull();
		assertThat(userDto.getUserId()).isEqualTo("userId111");
		assertThat(userDto.getName()).isEqualTo("name111");
	}
	
	@Test
	void testToEntity() {
		UserDto userDto = new UserDto("userId222", "name222");
		
		UserMapper mapper = Mappers.getMapper(UserMapper.class);
		User user = mapper.toEntity(userDto);
		
		assertThat(user).isNotNull();
		assertThat(user.getId()).isEqualTo(0);
		assertThat(user.getVersion()).isEqualTo(0);
		assertThat(user.getUserId()).isEqualTo("userId222");
		assertThat(user.getName()).isEqualTo("name222");
	}

}
