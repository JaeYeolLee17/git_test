package com.e4motion.challenge.api.mapper;

import java.util.Collections;
import java.util.List;

import com.e4motion.challenge.api.domain.Authority;
import com.e4motion.challenge.api.dto.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.e4motion.challenge.api.domain.User;

@Mapper(componentModel = "spring", imports = {Authority.class, Collections.class})
public interface UserMapper {

    @Mapping(target = "password", ignore = true)
    @Mapping(target = "authority", expression = MappingExpressions.TO_USER_DTO_AUTHORITY)
    UserDto toUserDto(User user);

    List<UserDto> toUserDto(List<User> users);

    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "authorities", expression = MappingExpressions.TO_USER_AUTHORITIES)
    User toUser(UserDto userDto);

}
