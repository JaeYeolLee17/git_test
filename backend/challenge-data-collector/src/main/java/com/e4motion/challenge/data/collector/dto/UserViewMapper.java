package com.e4motion.challenge.data.collector.dto;

import java.util.List;

import org.mapstruct.Mapper;

import com.e4motion.challenge.data.collector.domain.User;

@Mapper(componentModel = "spring")
public abstract class UserViewMapper {

    public abstract UserView toUserView(User user);

    public abstract List<UserView> toUserView(List<User> users);

}
