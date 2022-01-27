package com.e4motion.challenge.auth.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import com.e4motion.challenge.auth.domain.user.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

	UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);
	
	UserDto toDto(User entity);
	
    @Mappings({
    	@Mapping(target = "id", ignore = true),
        @Mapping(target = "version", ignore = true)
    })
    User toEntity(UserDto dto);

}
