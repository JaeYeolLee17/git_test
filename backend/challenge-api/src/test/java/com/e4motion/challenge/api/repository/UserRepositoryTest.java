package com.e4motion.challenge.api.repository;

import com.e4motion.challenge.api.TestDataHelper;
import com.e4motion.challenge.api.domain.Authority;
import com.e4motion.challenge.api.domain.User;
import com.e4motion.challenge.api.dto.UserUpdateDto;
import com.e4motion.challenge.common.domain.AuthorityName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@Transactional
class UserRepositoryTest {

	@Autowired
	UserRepository userRepository;
	
	User savedUser;
	
	@BeforeEach
	void setUp() throws Exception {

		userRepository.deleteAll();

		User user = TestDataHelper.getUser1();

		savedUser = userRepository.save(user);

		assertThat(userRepository.count()).isEqualTo(1);
		assertEqualsUsers(user, savedUser);
	}

	@Test
	void save() {
		
		User newUser = TestDataHelper.getUser2();

		userRepository.save(newUser);
		
		Optional<User> foundUser = userRepository.findByUsername(newUser.getUsername());
		
		assertThat(userRepository.count()).isEqualTo(2);
		assertThat(foundUser.isPresent()).isTrue();
        assertEqualsUsers(foundUser.get(), newUser);
	}

	@Test
	void update() {

		UserUpdateDto userUpdateDto = TestDataHelper.getUserUpdateDto();
    	savedUser.setUsername(userUpdateDto.getUsername());
    	savedUser.setEmail(userUpdateDto.getEmail());
    	savedUser.setPhone(userUpdateDto.getPhone());
    	
		Set<Authority> authorities = new HashSet<>();		// Do not use Collections.singleton when save for update.
		authorities.add(new Authority(AuthorityName.ROLE_USER));
        savedUser.setAuthorities(authorities);
        
        userRepository.save(savedUser);

        Optional<User> foundUser = userRepository.findByUserId(savedUser.getUserId());
        
        assertThat(foundUser.isPresent()).isTrue();
        assertEqualsUsers(foundUser.get(), savedUser);
	}
	
	@Test
	void findAll() {

		User user = TestDataHelper.getUser2();

		userRepository.save(user);
		
		List<User> users = userRepository.findAll();
		
		assertThat(users.size()).isEqualTo(2);
        assertEqualsUsers(users.get(0), savedUser);
        assertEqualsUsers(users.get(1), user);
	}

	@Test
	void deleteAll() {

		User user = TestDataHelper.getUser2();

		userRepository.save(user);
		
		assertThat(userRepository.count()).isEqualTo(2);
		
		userRepository.deleteAll();
		
		assertThat(userRepository.count()).isEqualTo(0);
	}

	@Test
	void findByUserId() {

        Optional<User> foundUser = userRepository.findByUserId(savedUser.getUserId());

        assertThat(foundUser.isPresent()).isTrue();
        assertEqualsUsers(foundUser.get(), savedUser);
	}

	@Test
	void findByUsername() {

		User user = TestDataHelper.getUser2();

		userRepository.save(user);

		Optional<User> foundUser = userRepository.findByUsername(savedUser.getUsername());

		assertThat(foundUser.isPresent()).isTrue();
		assertEqualsUsers(foundUser.get(), savedUser);
	}

	@Test
	void deleteByUserId() {
		
		Optional<User> foundUser = userRepository.findByUserId(savedUser.getUserId());
    	
    	assertThat(foundUser.isPresent()).isTrue();
    	
        userRepository.deleteByUserId(foundUser.get().getUserId());

        foundUser = userRepository.findByUserId(savedUser.getUserId());
        
        assertThat(foundUser.isPresent()).isFalse();
        assertThat(userRepository.count()).isEqualTo(0);
	}
	
	private void assertEqualsUsers(User user1, User user2) {

		assertThat(user1.getUsername()).isEqualTo(user2.getUsername());
		assertThat(user1.getNickname()).isEqualTo(user2.getNickname());
		assertThat(user1.getEmail()).isEqualTo(user2.getEmail());
		assertThat(user1.getPhone()).isEqualTo(user2.getPhone());
		assertThat(user1.getEnabled()).isEqualTo(user2.getEnabled());
		assertThat(user1.getAuthorities().size()).isEqualTo(user2.getAuthorities().size());
		if (user1.getAuthorities().size() > 0) {
			assertThat(user1.getAuthorities().iterator().next().getAuthorityName())
				.isEqualTo(user2.getAuthorities().iterator().next().getAuthorityName());
		}
	}
}
