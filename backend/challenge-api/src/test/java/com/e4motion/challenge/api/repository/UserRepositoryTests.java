package com.e4motion.challenge.api.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.transaction.annotation.Propagation.NOT_SUPPORTED;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import com.e4motion.challenge.api.domain.entity.Authority;
import com.e4motion.challenge.api.domain.entity.User;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@Transactional(propagation = NOT_SUPPORTED)
public class UserRepositoryTests {

	@Autowired
    private UserRepository repository;
	
	private String savedUserId = "admin";
	private String savedUsername = "adminname";
	private String savedEmail = "admin@email...";
	private String savedPhone = "01022223333";
	private String savedAuthority = Authority.ROLE_ADMIN;
	
	private User savedUser;
	
	@BeforeEach
   	public void setup() {
   		repository.deleteAll();
   		
		Set<Authority> authorities = new HashSet<>();
		authorities.add(new Authority(savedAuthority));
		User user = User.builder()
				.userId(savedUserId)
				.username(savedUsername)
				.email(savedEmail)
				.phone(savedPhone)
				.authorities(authorities)
				.build();

		savedUser = repository.save(user);

		assertThat(repository.count()).isEqualTo(1);
		assertEqualsUser(user, savedUser);
    }

	@Test
   	public void findByUserId() {
        User foundUser = repository.findByUserId(savedUser.getUserId()).orElse(null);

        assertThat(foundUser).isNotNull();
        assertEqualsUser(foundUser, savedUser);
    }
	
	@Test
   	public void create() {

		String newUserId = "user1";
		String newUsername = "user1name";
		String newEmail = "user1@email...";
		String newPhone = "01044445555";
		String newAuthority = Authority.ROLE_USER;
		
		Set<Authority> authorities = new HashSet<>();
		authorities.add(new Authority(newAuthority));
		User newUser = User.builder()
				.userId(newUserId)
				.username(newUsername)
				.email(newEmail)
				.phone(newPhone)
				.authorities(authorities)
				.build();

		repository.save(newUser);
		
		User foundUser = repository.findByUserId(newUser.getUserId()).orElse(null);
		
		assertThat(repository.count()).isEqualTo(2);
        assertThat(foundUser).isNotNull();
        assertEqualsUser(newUser, foundUser);
    }

    @Test
   	public void update() {
    	
		String updatedUsername = "updated user name";
		String updatedEmail = "udpated@email...";
		String updatedPhone = "01066667777";
		String updatedAuthority = Authority.ROLE_USER;
		
    	savedUser.setUsername(updatedUsername);
    	savedUser.setEmail(updatedEmail);
    	savedUser.setPhone(updatedPhone);
    	
        Set<Authority> authorities = new HashSet<>();
		authorities.add(new Authority(updatedAuthority));
        savedUser.setAuthorities(authorities);
        
        repository.save(savedUser);

        User foundUser = repository.findByUserId(savedUser.getUserId()).orElse(null);
        
        assertThat(foundUser).isNotNull();
        assertEqualsUser(savedUser, foundUser);
    }

    @Test
   	public void delete() {
        repository.delete(savedUser);

        User foundUser = repository.findByUserId(savedUser.getUserId()).orElse(null);
        
        assertThat(repository.count()).isEqualTo(0);
        assertThat(foundUser).isNull();
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
