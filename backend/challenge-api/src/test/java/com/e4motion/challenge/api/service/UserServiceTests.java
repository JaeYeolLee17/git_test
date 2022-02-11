package com.e4motion.challenge.api.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.transaction.annotation.Propagation.NOT_SUPPORTED;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.e4motion.challenge.api.domain.dto.UserDto;
import com.e4motion.challenge.api.domain.entity.Authority;
import com.e4motion.challenge.api.repository.UserRepository;

@SpringBootTest
@ActiveProfiles("unittest")
@Transactional(propagation = NOT_SUPPORTED)
public class UserServiceTests {
	
	@Autowired
    private UserRepository repository;
	
	@Autowired
    private UserService service;
	
	@BeforeEach
   	public void setup() {
   		repository.deleteAll();
   		
   		assertThat(repository.count()).isEqualTo(0);
    }
	
	//@Test
	public UserDto create() {
		
		String userId = "admin";
		String password = "password";
		String username = "adminname";
		String email = "admin@email...";
		String phone = "01022223333";
		String authority = Authority.ROLE_ADMIN;
		
		UserDto newUserDto = UserDto.builder()
				.userId(userId)
				.password(password)
				.username(username)    
				.email(email)
				.phone(phone)
				.authority(authority)
				.build();
		
		UserDto userDto = service.create(newUserDto);
		assertThat(userDto).isNotNull();
		
		List<UserDto> userDtos = service.getList();
		assertThat(userDtos.size()).isEqualTo(1);
		
		userDto = service.get(userId);
		assertThat(userDto).isNotNull();

		assertEqualsUserDto(userDto, newUserDto);
		
		return newUserDto;
    }

	@Test
   	public void update() {
		
		UserDto createdUserDto = create();
		
		String userId = createdUserDto.getUserId();
		String username = "updated_adminname";
		String email = "updated_admin@email...";
		String phone = "01044445555";
		String authority = Authority.ROLE_USER;
		
		UserDto updatedUserDto = UserDto.builder()
				.userId(userId)
				.username(username)    
				.email(email)
				.build();
		
		UserDto userDto = service.update(userId, updatedUserDto);
		assertThat(userDto).isNotNull();
 
		assertThat(userDto.getUsername()).isEqualTo(username);
		assertThat(userDto.getEmail()).isEqualTo(email);
		assertThat(userDto.getPhone()).isEqualTo(createdUserDto.getPhone());
		assertThat(userDto.getAuthority()).isEqualTo(createdUserDto.getAuthority());
		
		updatedUserDto = UserDto.builder()
				.userId(userId)
				.phone(phone)    
				.authority(authority)
				.build();
		
		userDto = service.update(userId, updatedUserDto);
		assertThat(userDto).isNotNull();
 
		assertThat(userDto.getUsername()).isEqualTo(username);
		assertThat(userDto.getEmail()).isEqualTo(email);
		assertThat(userDto.getPhone()).isEqualTo(phone);
		assertThat(userDto.getAuthority()).isEqualTo(authority);
    }
	
	@Test
   	public void delete() {
		
		UserDto createdUserDto = create();
		
		String userId = createdUserDto.getUserId();
		
		service.delete(userId);

		UserDto userDto = service.get(userId);
		assertThat(userDto).isNull();
    }

	private void assertEqualsUserDto(UserDto userDto1, UserDto userDto2) {
		assertThat(userDto1.getUserId()).isEqualTo(userDto2.getUserId());
		assertThat(userDto1.getUsername()).isEqualTo(userDto2.getUsername());
		assertThat(userDto1.getEmail()).isEqualTo(userDto2.getEmail());
		assertThat(userDto1.getPhone()).isEqualTo(userDto2.getPhone());
		assertThat(userDto1.getAuthority()).isEqualTo(userDto2.getAuthority());
	}
	
}
