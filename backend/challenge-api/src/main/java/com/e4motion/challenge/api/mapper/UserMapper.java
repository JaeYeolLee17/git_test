package com.e4motion.challenge.api.mapper;

import java.util.Collections;
import java.util.List;

import com.e4motion.challenge.api.domain.Authority;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.e4motion.challenge.api.domain.User;
import com.e4motion.challenge.api.dto.UserDto;                              

@Mapper(componentModel = "spring", imports = {Authority.class, Collections.class})
public interface UserMapper {
    
	@Mapping(target = "authority", expression = "java(user.getAuthorities().isEmpty() ? null : user.getAuthorities().iterator().next().getAuthorityName())")
	@Mapping(target = "password", ignore = true)
    UserDto toUserDto(User user);

    List<UserDto> toUserDto(List<User> users);

    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "authorities", expression = "java(Collections.singleton(new Authority(userDto.getAuthority())))")
    User toUser(UserDto userDto);

}
