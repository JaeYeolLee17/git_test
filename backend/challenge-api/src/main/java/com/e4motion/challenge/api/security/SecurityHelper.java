package com.e4motion.challenge.api.security;

import com.e4motion.challenge.api.dto.UserDto;
import com.e4motion.challenge.api.mapper.UserMapper;
import com.e4motion.challenge.api.repository.UserRepository;
import com.e4motion.challenge.common.domain.AuthorityName;
import com.e4motion.challenge.common.exception.customexception.InaccessibleException;
import com.e4motion.challenge.common.exception.customexception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class SecurityHelper {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public void checkIfLoginUserForRoleUser(Long userId) {

        UserDto loginUser = getLoginUser();
        if (loginUser.getAuthority().equals(AuthorityName.ROLE_USER)
                && !loginUser.getUserId().equals(userId)) {
            throw new InaccessibleException(InaccessibleException.ACCESS_DENIED);
        }
    }

    public UserDto getLoginUser() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails)authentication.getPrincipal();

        return userRepository.findByUsername(userDetails.getUsername())
                .map(userMapper::toUserDto)
                .orElseThrow(() -> new UserNotFoundException(UserNotFoundException.INVALID_USERNAME));
    }
}
