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

import com.e4motion.challenge.api.dto.UserDto;
import com.e4motion.challenge.api.repository.UserRepository;
import com.e4motion.challenge.common.domain.AuthorityName;

@SpringBootTest
@ActiveProfiles("unittest")
@Transactional
public class UserServiceTest {
	
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
	public UserDto create() throws Exception {
		
		String userId = "admin";
		String password = "password";
		String username = "adminname";
		String email = "admin@email...";
		String phone = "01022223333";
		AuthorityName authority = AuthorityName.ROLE_ADMIN;
		
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
   	public void update() throws Exception {
		
		UserDto createdUserDto = create();
		
		String userId = createdUserDto.getUserId();
		String username = "updated_adminname";
		String email = "updated_admin@email...";
		String phone = "01044445555";
		AuthorityName authority = AuthorityName.ROLE_USER;
		
		UserDto userDto = UserDto.builder()
				.userId(userId)
				.username(username)    
				.email(email)
				.build();
		
		UserDto updaedUserDto = service.update(userId, userDto);
		assertThat(updaedUserDto).isNotNull();
 
		assertThat(updaedUserDto.getUsername()).isEqualTo(username);
		assertThat(updaedUserDto.getEmail()).isEqualTo(email);
		assertThat(updaedUserDto.getPhone()).isEqualTo(createdUserDto.getPhone());
		assertThat(updaedUserDto.getAuthority()).isEqualTo(createdUserDto.getAuthority());
		
		userDto = UserDto.builder()
				.userId(userId)
				.phone(phone)    
				.authority(authority)
				.build();
		
		updaedUserDto = service.update(userId, userDto);
		assertThat(updaedUserDto).isNotNull();
 
		assertThat(updaedUserDto.getUsername()).isEqualTo(username);
		assertThat(updaedUserDto.getEmail()).isEqualTo(email);
		assertThat(updaedUserDto.getPhone()).isEqualTo(phone);
		assertThat(updaedUserDto.getAuthority()).isEqualTo(authority);
    }
	
	@Test
   	public void delete() throws Exception {
		
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
