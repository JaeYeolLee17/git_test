package com.e4motion.challenge.api.repository;

import com.e4motion.challenge.api.TestDataHelper;
import com.e4motion.challenge.api.domain.Authority;
import com.e4motion.challenge.api.domain.User;
import com.e4motion.challenge.api.dto.UserUpdateDto;
import com.e4motion.challenge.common.constant.AuthorityName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class UserRepositoryTest {

	@Autowired
	UserRepository userRepository;

	@Autowired
	EntityManager entityManager;

	@Test
	void save() {
		
		User user = saveUser1();
		
		Optional<User> found = userRepository.findByUsername(user.getUsername());
		assertThat(found.isPresent()).isTrue();

        assertEquals(found.get(), user);
	}

	@Test
	void update() {

		User user = saveUser1();

		UserUpdateDto userUpdateDto = TestDataHelper.getUserUpdateDto();
		user.setUsername(userUpdateDto.getUsername());
		user.setPassword(userUpdateDto.getNewPassword());
		user.setEmail(userUpdateDto.getEmail());
		user.setPhone(userUpdateDto.getPhone());
    	
		Set<Authority> authorities = new HashSet<>();		// Do not use Collections.singleton when save for update.
		authorities.add(new Authority(AuthorityName.ROLE_USER));
		user.setAuthorities(authorities);
        
        userRepository.saveAndFlush(user);
		entityManager.clear();

        Optional<User> found = userRepository.findByUsername(user.getUsername());
        
        assertThat(found.isPresent()).isTrue();
        assertEquals(found.get(), user);
	}

	@Test
	void findByUsername() {

		User user = saveUser1();

		Optional<User> found = userRepository.findByUsername(user.getUsername());

		assertThat(found.isPresent()).isTrue();
		assertEquals(found.get(), user);
	}

	@Test
	void deleteByUsername() {

		User user = saveUser1();

		Optional<User> found = userRepository.findByUsername(user.getUsername());
    	assertThat(found.isPresent()).isTrue();
    	
        userRepository.deleteByUsername(found.get().getUsername());
		entityManager.flush();
		entityManager.clear();

        found = userRepository.findByUsername(user.getUsername());
        assertThat(found.isPresent()).isFalse();
        assertThat(userRepository.count()).isEqualTo(0);
	}

	private User saveUser1() {

		User user = TestDataHelper.getUser1();

		User saved = userRepository.saveAndFlush(user);
		entityManager.clear();

		assertThat(userRepository.count()).isEqualTo(1);

		return saved;
	}
	
	private void assertEquals(User user1, User user2) {

		assertThat(user1.getUsername()).isEqualTo(user2.getUsername());
		assertThat(user1.getPassword()).isEqualTo(user2.getPassword());
		assertThat(user1.getNickname()).isEqualTo(user2.getNickname());
		assertThat(user1.getEmail()).isEqualTo(user2.getEmail());
		assertThat(user1.getPhone()).isEqualTo(user2.getPhone());
		assertThat(user1.getDisabled()).isEqualTo(user2.getDisabled());
		assertThat(user1.getAuthorities().size()).isEqualTo(user2.getAuthorities().size());
		if (user1.getAuthorities().size() > 0) {
			assertThat(user1.getAuthorities().iterator().next().getAuthorityName())
				.isEqualTo(user2.getAuthorities().iterator().next().getAuthorityName());
		}
	}
}
