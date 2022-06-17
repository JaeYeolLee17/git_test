package com.e4motion.challenge.api;

import com.e4motion.challenge.api.domain.Authority;
import com.e4motion.challenge.api.domain.User;
import com.e4motion.challenge.api.dto.UserDto;
import com.e4motion.challenge.api.dto.UserUpdateDto;
import com.e4motion.challenge.common.domain.AuthorityName;

import java.util.Collections;

public class TestHelper {

    public static UserDto getUserDto1() {
        return UserDto.builder()
                .userId("user1")
                .password("challenge12!@")
                .username("username1")
                .email("user1@email.com")
                .phone("01022223333")
                .enabled(true)
                .authority(AuthorityName.ROLE_USER)
                .build();
    }

    public static UserDto getUserDto2() {
        return UserDto.builder()
                .userId("user2")
                .password("challenge12!@")
                .username("username2")
                .email("user2@email.com")
                .phone("01044445555")
                .enabled(true)
                .authority(AuthorityName.ROLE_USER)
                .build();
    }

    public static UserUpdateDto getUserUpdateDto() {
        return UserUpdateDto.builder()
                .oldPassword("challenge12!@")
                .newPassword("challenge12!@updated")
                .username("usernameupdated")
                .email("emailupdated@email.com")
                .phone("01088889999")
                .enabled(true)
                .authority(AuthorityName.ROLE_ADMIN)
                .build();
    }

    public static User getUser1() {
        return User.builder()
                .userId("user1")
                .password("challenge1123!")
                .username("username1")
                .email("user1@email.com")
                .phone("01022223333")
                .enabled(true)
                .authorities(Collections.singleton(new Authority(AuthorityName.ROLE_ADMIN)))
                .build();
    }

    public static User getUser2() {
        return User.builder()
                .userId("user2")
                .password("challenge12!@")
                .username("username2")
                .email("user2@email.com")
                .phone("01044445555")
                .enabled(true)
                .authorities(Collections.singleton(new Authority(AuthorityName.ROLE_USER)))
                .build();
    }
}
