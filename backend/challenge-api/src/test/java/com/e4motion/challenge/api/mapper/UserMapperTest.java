package com.e4motion.challenge.api.mapper;

import com.e4motion.challenge.api.TestHelper;
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
		
		User user = TestHelper.getUser1();
		
		UserDto userDto = mapper.toUserDto(user);
		
		assertEqualsUserDtoWithUser(userDto, user, true);
	}
	
	@Test
    public void toUserDtoList() {
		
		User user1 = TestHelper.getUser1();
		User user2 = TestHelper.getUser2();
		
		List<User> users = new ArrayList<>();
		users.add(user1);
		users.add(user2);
		
		List<UserDto> userDtos = mapper.toUserDto(users);
		
		assertThat(userDtos.size()).isEqualTo(2);
		
		assertThat(userDtos.get(0).getPassword()).isNull();
		assertThat(userDtos.get(1).getPassword()).isNull();
		
		assertEqualsUserDtoWithUser(userDtos.get(0), user1, true);
		assertEqualsUserDtoWithUser(userDtos.get(1), user2, true);
	}

	@Test
	public void toUser() {

		UserDto userDto = TestHelper.getUserDto1();

		User user = mapper.toUser(userDto);

		assertEqualsUserDtoWithUser(userDto, user, false);
	}
	
	private void assertEqualsUserDtoWithUser(UserDto userDto, User user, Boolean ignorePassword) {

		assertThat(userDto.getUserId()).isEqualTo(user.getUserId());
		assertThat(userDto.getUsername()).isEqualTo(user.getUsername());
		if (ignorePassword) {
			assertThat(userDto.getPassword()).isNull();
		} else {
			assertThat(userDto.getPassword()).isEqualTo(user.getPassword());
		}
		assertThat(userDto.getNickname()).isEqualTo(user.getNickname());
		assertThat(userDto.getEmail()).isEqualTo(user.getEmail());
		assertThat(userDto.getPhone()).isEqualTo(user.getPhone());
		assertThat(userDto.getEnabled()).isEqualTo(user.getEnabled());
		assertThat(userDto.getAuthority()).isEqualTo(user.getAuthorities().isEmpty() ? null : user.getAuthorities().iterator().next().getAuthorityName());
	}
}
