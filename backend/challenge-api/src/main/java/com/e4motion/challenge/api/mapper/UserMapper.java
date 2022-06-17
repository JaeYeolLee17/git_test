package com.e4motion.challenge.api.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.e4motion.challenge.api.domain.User;
import com.e4motion.challenge.api.dto.UserDto;                              

@Mapper(componentModel = "spring")
public interface UserMapper {
    
	@Mapping(target = "authority", expression = "java(user.getAuthorities().isEmpty() ? null : user.getAuthorities().iterator().next().getAuthorityName())")
	@Mapping(target = "password", ignore = true)
    UserDto toUserDto(User user);

    List<UserDto> toUserDto(List<User> users);

    // TODO: UserDto -> User mapper 추가. (authority 변환, password 인코딩)

}
