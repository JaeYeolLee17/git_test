package com.e4motion.challenge.auth.dto;

import org.mapstruct.Mapper;

import com.e4motion.challenge.auth.domain.user.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
	
	UserDto toDto(User entity);
	
//    @Mappings({
//    	@Mapping(target = "id", ignore = true),
//        @Mapping(target = "version", ignore = true)
//    })
    User toEntity(UserDto dto);

}
