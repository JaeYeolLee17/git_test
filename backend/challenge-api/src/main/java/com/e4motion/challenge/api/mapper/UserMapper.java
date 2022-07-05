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

    @Mapping(target = "authority", expression = MappingExpression.TO_USER_DTO_AUTHORITY)
	@Mapping(target = "password", ignore = true)
    UserDto toUserDto(User user);

    List<UserDto> toUserDto(List<User> users);

    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "authorities", expression = MappingExpression.TO_USER_AUTHORITIES)
    User toUser(UserDto userDto);

}
