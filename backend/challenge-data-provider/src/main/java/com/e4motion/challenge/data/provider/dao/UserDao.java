package com.e4motion.challenge.data.provider.dao;

import java.util.List;

import com.e4motion.challenge.data.provider.domain.User;

public interface UserDao {
	
    User get(String userId) throws Exception;
    
    List<User> getList() throws Exception;
    
    User create(User user) throws Exception;
    
    User update(User userDto) throws Exception;

    void delete(String userId) throws Exception;
    
}
