package com.e4motion.challenge.api.security;

import com.e4motion.challenge.api.TestDataHelper;
import com.e4motion.challenge.api.dto.UserDto;
import com.e4motion.challenge.api.mapper.UserMapper;
import com.e4motion.challenge.api.repository.UserRepository;
import com.e4motion.challenge.common.exception.customexception.InaccessibleException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;

@SpringBootTest
class SecurityHelperTest {

    @Mock
    UserRepository userRepository;

    @Autowired
    UserMapper userMapper;

    SecurityHelper securityHelper;

    @BeforeEach
    void setup() {
        securityHelper = new SecurityHelper();
    }

    @Test
    @WithMockUser(username = "user1", roles = "USER")
    void checkIfLoginUserForRoleUser_loggedInWithUser() {

        UserDto userDto = TestDataHelper.getUserDto1();

        doReturn(Optional.of(userMapper.toUser(userDto))).when(userRepository).findByUsername(userDto.getUsername());

        securityHelper.checkIfLoginUserForRoleUser(userDto.getUsername());    // Nothing should happen.

        // Exception should happen.
        Exception ex = assertThrows(InaccessibleException.class, () -> securityHelper.checkIfLoginUserForRoleUser("other_user"));

        assertThat(ex.getMessage()).isEqualTo(InaccessibleException.ACCESS_DENIED);
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void checkIfLoginUserForRoleUser_loggedInWithAdmin () {

        UserDto userDto = TestDataHelper.getUserDto1();
        UserDto adminUserDto = TestDataHelper.getAdminUserDto();

        doReturn(Optional.of(userMapper.toUser(adminUserDto))).when(userRepository).findByUsername(adminUserDto.getUsername());

        securityHelper.checkIfLoginUserForRoleUser(userDto.getUsername());    // Nothing should happen.

        securityHelper.checkIfLoginUserForRoleUser("other_user");    // Nothing should happen.
    }

    @Test
    @WithMockUser(username = "manager", roles = "MANAGER")
    void checkIfLoginUserForRoleUser_loggedInWithManager () {

        UserDto userDto = TestDataHelper.getUserDto1();
        UserDto managerUserDto = TestDataHelper.getManagerUserDto();

        doReturn(Optional.of(userMapper.toUser(managerUserDto))).when(userRepository).findByUsername(managerUserDto.getUsername());

        securityHelper.checkIfLoginUserForRoleUser(userDto.getUsername());    // Nothing should happen.

        securityHelper.checkIfLoginUserForRoleUser("other_user");    // Nothing should happen.
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void getLoginUser_loggedInWithAdmin() {

        UserDto adminUserDto = TestDataHelper.getAdminUserDto();

        doReturn(Optional.of(userMapper.toUser(adminUserDto))).when(userRepository).findByUsername(adminUserDto.getUsername());

        UserDto loginUserDto = securityHelper.getLoginUser();
        assertThat(loginUserDto.getUsername()).isEqualTo(adminUserDto.getUsername());
        assertThat(loginUserDto.getAuthority()).isEqualTo(adminUserDto.getAuthority());
    }

    @Test
    @WithMockUser(username = "manager", roles = "MANAGER")
    void getLoginUser_loggedInWithManager() {

        UserDto managerUserDto = TestDataHelper.getManagerUserDto();

        doReturn(Optional.of(userMapper.toUser(managerUserDto))).when(userRepository).findByUsername(managerUserDto.getUsername());

        UserDto loginUserDto = securityHelper.getLoginUser();
        assertThat(loginUserDto.getUsername()).isEqualTo(managerUserDto.getUsername());
        assertThat(loginUserDto.getAuthority()).isEqualTo(managerUserDto.getAuthority());
    }

    @Test
    @WithMockUser(username = "user1", roles = "USER")
    void getLoginUser_loggedInWithUser() {

        UserDto userDto = TestDataHelper.getUserDto1();

        doReturn(Optional.of(userMapper.toUser(userDto))).when(userRepository).findByUsername(userDto.getUsername());

        UserDto loginUserDto = securityHelper.getLoginUser();
        assertThat(loginUserDto.getUsername()).isEqualTo(userDto.getUsername());
        assertThat(loginUserDto.getAuthority()).isEqualTo(userDto.getAuthority());
    }
}