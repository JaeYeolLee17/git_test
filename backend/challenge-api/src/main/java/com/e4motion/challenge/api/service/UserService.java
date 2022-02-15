package com.e4motion.challenge.api.service;

import java.util.List;

import com.e4motion.challenge.api.dto.UserDto;

public interface UserService {
    
    UserDto get(String userId) throws Exception;
    
    List<UserDto> getList() throws Exception;
    
    UserDto create(UserDto userDto) throws Exception;
    
    UserDto update(String userId, UserDto userDto) throws Exception;

    void delete(String userId) throws Exception;

}
