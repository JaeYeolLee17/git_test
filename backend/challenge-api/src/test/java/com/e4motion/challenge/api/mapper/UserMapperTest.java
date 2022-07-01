package com.e4motion.challenge.api.mapper;

import com.e4motion.challenge.api.TestDataHelper;
import com.e4motion.challenge.api.domain.User;
import com.e4motion.challenge.api.dto.UserDto;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class UserMapperTest {

	UserMapper mapper = Mappers.getMapper(UserMapper.class);
	
	@Test
    public void toUserDto() {
		
		User user = TestDataHelper.getUser1();
		
		UserDto userDto = mapper.toUserDto(user);
		
		assertEquals(user, userDto);
	}
	
	@Test
    public void toUserDtoList() {
		
		User user1 = TestDataHelper.getUser1();
		User user2 = TestDataHelper.getUser2();
		
		List<User> users = new ArrayList<>();
		users.add(user1);
		users.add(user2);
		
		List<UserDto> userDtos = mapper.toUserDto(users);
		
		assertThat(userDtos.size()).isEqualTo(2);
		
		assertThat(userDtos.get(0).getPassword()).isNull();
		assertThat(userDtos.get(1).getPassword()).isNull();

		assertEquals(users.get(0), userDtos.get(0));
		assertEquals(users.get(1),userDtos.get(1));
	}

	@Test
	public void toUser() {

		UserDto userDto = TestDataHelper.getUserDto1();

		User user = mapper.toUser(userDto);

		assertEquals(userDto, user);
	}

	private void assertEquals(User user, UserDto userDto) {

		assertThat(userDto.getUserId()).isEqualTo(user.getUserId());
		assertThat(userDto.getUsername()).isEqualTo(user.getUsername());
		assertThat(userDto.getPassword()).isNull();
		assertThat(userDto.getNickname()).isEqualTo(user.getNickname());
		assertThat(userDto.getEmail()).isEqualTo(user.getEmail());
		assertThat(userDto.getPhone()).isEqualTo(user.getPhone());
		assertThat(userDto.getEnabled()).isEqualTo(user.getEnabled());
		assertThat(userDto.getAuthority()).isEqualTo(user.getAuthorities().isEmpty() ? null : user.getAuthorities().iterator().next().getAuthorityName());
	}

	private void assertEquals(UserDto userDto, User user) {

		assertThat(userDto.getUserId()).isEqualTo(user.getUserId());
		assertThat(userDto.getUsername()).isEqualTo(user.getUsername());
		assertThat(userDto.getPassword()).isEqualTo(user.getPassword());
		assertThat(userDto.getNickname()).isEqualTo(user.getNickname());
		assertThat(userDto.getEmail()).isEqualTo(user.getEmail());
		assertThat(userDto.getPhone()).isEqualTo(user.getPhone());
		assertThat(userDto.getEnabled()).isEqualTo(user.getEnabled());
		assertThat(userDto.getAuthority()).isEqualTo(user.getAuthorities().isEmpty() ? null : user.getAuthorities().iterator().next().getAuthorityName());
	}
}
