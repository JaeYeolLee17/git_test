package com.e4motion.challenge.api.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.e4motion.challenge.api.domain.Authority;
import com.e4motion.challenge.api.domain.User;
import com.e4motion.challenge.common.domain.AuthorityName;

@DataJpaTest
@ActiveProfiles("test")
@Transactional
class UserRepositoryTest {

	@Autowired
	private UserRepository userRepository;
	
	private User savedUser;
	
	@BeforeEach
	void setUp() throws Exception {
		userRepository.deleteAll();
		
		User user = User.builder()
				.userId("admin")
				.password("password")
				.username("adminname")
				.email("admin@email...")
				.phone("01022223333")
				.authorities(Collections.singleton(new Authority(AuthorityName.ROLE_ADMIN)))
				.build();

		savedUser = userRepository.save(user);

		assertThat(userRepository.count()).isEqualTo(1);
		assertEqualsUser(user, savedUser);
	}

	@Test
	void saveNew() {
		
		User newUser = User.builder()
				.userId("user1")
				.password("password")
				.username("user1name")
				.email("user1@email...")
				.phone("01044445555")
				.authorities(Collections.singleton(new Authority(AuthorityName.ROLE_USER)))
				.build();

		userRepository.save(newUser);
		
		Optional<User> foundUser = userRepository.findByUserId(newUser.getUserId());
		
		assertThat(userRepository.count()).isEqualTo(2);
		assertThat(foundUser.isPresent()).isTrue();
        assertEqualsUser(foundUser.get(), newUser);
	}

	@Test
	void saveUpdate() {

        String updatedUsername = "updated user name";
		String updatedEmail = "udpated@email...";
		String updatedPhone = "01066667777";
		AuthorityName updatedAuthority = AuthorityName.ROLE_USER;
		
    	savedUser.setUsername(updatedUsername);
    	savedUser.setEmail(updatedEmail);
    	savedUser.setPhone(updatedPhone);
    	
		Set<Authority> authorities = new HashSet<>();	// Do not use Collections.singleton when save for update.
		authorities.add(new Authority(updatedAuthority));
        savedUser.setAuthorities(authorities);
        
        userRepository.save(savedUser);

        Optional<User> foundUser = userRepository.findByUserId(savedUser.getUserId());
        
        assertThat(foundUser.isPresent()).isTrue();
        assertEqualsUser(foundUser.get(), savedUser);
	}
	
	@Test
	void findAll() {

		User user = User.builder()
				.userId("user2")
				.password("password")
				.username("user2name")
				.email("user2@email...")
				.phone("01066667777")
				.authorities(Collections.singleton(new Authority(AuthorityName.ROLE_USER)))
				.build();

		userRepository.save(user);
		
		List<User> users = userRepository.findAll();
		
		assertThat(users.size()).isEqualTo(2);
        assertEqualsUser(users.get(0), savedUser);
        assertEqualsUser(users.get(1), user);
	}

	@Test
	void deleteAll() {
		
		User user = User.builder()
				.userId("user2")
				.password("password")
				.username("user2name")
				.email("user2@email...")
				.phone("01066667777")
				.authorities(Collections.singleton(new Authority(AuthorityName.ROLE_USER)))
				.build();

		userRepository.save(user);
		
		assertThat(userRepository.count()).isEqualTo(2);
		
		userRepository.deleteAll();
		
		assertThat(userRepository.count()).isEqualTo(0);
	}

	@Test
	void findByUserId() {
		
		User user = User.builder()
				.userId("user1")
				.password("password")
				.username("user1name")
				.email("user1@email...")
				.phone("01044445555")
				.authorities(Collections.singleton(new Authority(AuthorityName.ROLE_USER)))
				.build();

		userRepository.save(user);
		
        Optional<User> foundUser = userRepository.findByUserId(savedUser.getUserId());

        assertThat(foundUser.isPresent()).isTrue();
        assertEqualsUser(foundUser.get(), savedUser);
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
	
	private void assertEqualsUser(User user1, User user2) {
		
		assertThat(user1.getUserId()).isEqualTo(user2.getUserId());
		assertThat(user1.getUsername()).isEqualTo(user2.getUsername());
		assertThat(user1.getEmail()).isEqualTo(user2.getEmail());
		assertThat(user1.getPhone()).isEqualTo(user2.getPhone());
		assertThat(user1.getAuthorities().size()).isEqualTo(user2.getAuthorities().size());
		if (user1.getAuthorities().size() > 0) {
			assertThat(user1.getAuthorities().iterator().next().getAuthorityName())
				.isEqualTo(user2.getAuthorities().iterator().next().getAuthorityName());
		}
	}

}
