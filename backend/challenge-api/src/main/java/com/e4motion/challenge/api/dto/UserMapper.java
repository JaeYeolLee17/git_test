package com.e4motion.challenge.api.dto;

import java.util.List;

import org.mapstruct.Mapper;

import com.e4motion.challenge.api.entity.User;

@Mapper(componentModel = "spring")
public abstract class UserMapper {

    public abstract UserDto toUserDto(User user);

    public abstract List<UserDto> toUserDto(List<User> users);

}
