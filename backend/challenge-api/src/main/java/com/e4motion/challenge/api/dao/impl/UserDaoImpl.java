package com.e4motion.challenge.api.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.e4motion.challenge.api.dao.UserDao;
import com.e4motion.challenge.api.domain.User;
import com.e4motion.challenge.api.repository.UserRepository;
import com.e4motion.common.exception.customexception.UserNotFoundException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class UserDaoImpl implements UserDao {
	
    private final UserRepository userRepository;
    
    public User get(String userId) throws Exception {
    	
        return userRepository.findByUserId(userId).orElse(null);
    }
    
    public List<User> getList() throws Exception {
    	
        return userRepository.findAll();
    }
    
    public User create(User user) throws Exception {

        return userRepository.save(user);
    }

    public User update(User user) throws Exception {
    	
    	User foundUser = userRepository.findByUserId(user.getUserId()).orElse(null);
    	if (foundUser == null) {
    		throw new UserNotFoundException("Invalid user id");
    	}
    	
    	if (user.getPassword() != null) {
    		foundUser.setPassword(user.getPassword());
    	}
    	
		if (user.getUsername() != null) {
			foundUser.setUsername(user.getUsername());
    	}
		
		if (user.getEmail() != null) {
			foundUser.setEmail(user.getEmail());
    	}

		if (user.getPhone() != null) {
			foundUser.setPhone(user.getPhone());
    	}

		if (user.getAuthorities().size() > 0) {
			foundUser.setAuthorities(user.getAuthorities());
    	}
		
		return userRepository.save(foundUser);
    }

    public void delete(String userId) throws Exception {
    	
    	userRepository.deleteByUserId(userId);
    	
    	return;
    }
}
