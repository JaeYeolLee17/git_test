package com.e4motion.challenge.api.security;

import com.e4motion.challenge.api.TestDataHelper;
import com.e4motion.challenge.api.dto.UserDto;
import com.e4motion.challenge.common.exception.customexception.InaccessibleException;
import com.e4motion.challenge.common.security.SecurityHelper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
class SecurityHelperTest {

    @Autowired
    SecurityHelper securityHelper;

    @Test
    @WithMockUser(username = "user1", roles = "USER")
    void checkIfLoginUserForRoleUser_loggedInWithUser() {

        UserDto userDto = TestDataHelper.getUserDto1();

        securityHelper.checkIfLoginUserForRoleUser(userDto.getUsername());    // Nothing should happen.

        // Exception should happen.
        Exception ex = assertThrows(InaccessibleException.class, () -> securityHelper.checkIfLoginUserForRoleUser("other_user"));

        assertThat(ex.getMessage()).isEqualTo(InaccessibleException.ACCESS_DENIED);
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void checkIfLoginUserForRoleUser_loggedInWithAdmin () {

        UserDto userDto = TestDataHelper.getUserDto1();

        securityHelper.checkIfLoginUserForRoleUser(userDto.getUsername());    // Nothing should happen.

        securityHelper.checkIfLoginUserForRoleUser("other_user");    // Nothing should happen.
    }

    @Test
    @WithMockUser(username = "manager", roles = "MANAGER")
    void checkIfLoginUserForRoleUser_loggedInWithManager () {

        UserDto userDto = TestDataHelper.getUserDto1();

        securityHelper.checkIfLoginUserForRoleUser(userDto.getUsername());    // Nothing should happen.

        securityHelper.checkIfLoginUserForRoleUser("other_user");    // Nothing should happen.
    }
}