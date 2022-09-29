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
		assertMapTo(user, userDto);
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
		assertMapTo(users.get(0), userDtos.get(0));
		assertMapTo(users.get(1), userDtos.get(1));
	}

	@Test
	public void toUser() {

		UserDto userDto = TestDataHelper.getUserDto1();

		User user = mapper.toUser(userDto);
		assertMapTo(userDto, user);
	}

	private void assertMapTo(User user, UserDto userDto) {

		assertThat(userDto.getUsername()).isEqualTo(user.getUsername());
		assertThat(userDto.getPassword()).isNull();
		assertThat(userDto.getName()).isEqualTo(user.getName());
		assertThat(userDto.getEmail()).isEqualTo(user.getEmail());
		assertThat(userDto.getPhone()).isEqualTo(user.getPhone());
		assertThat(userDto.getDisabled()).isEqualTo(user.getDisabled());
		assertThat(userDto.getAuthority()).isEqualTo(user.getAuthorities().isEmpty() ? null : user.getAuthorities().iterator().next().getAuthorityName());
	}

	private void assertMapTo(UserDto userDto, User user) {

		assertThat(user.getUserId()).isNull();
		assertThat(userDto.getUsername()).isEqualTo(user.getUsername());
		assertThat(userDto.getPassword()).isEqualTo(user.getPassword());
		assertThat(userDto.getName()).isEqualTo(user.getName());
		assertThat(userDto.getEmail()).isEqualTo(user.getEmail());
		assertThat(userDto.getPhone()).isEqualTo(user.getPhone());
		assertThat(userDto.getDisabled()).isEqualTo(user.getDisabled());
		assertThat(userDto.getAuthority()).isEqualTo(user.getAuthorities().isEmpty() ? null : user.getAuthorities().iterator().next().getAuthorityName());
	}
}
