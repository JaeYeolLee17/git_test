package com.e4motion.challenge.api.security;

import com.e4motion.challenge.api.dto.UserDto;
import com.e4motion.challenge.common.domain.AuthorityName;
import com.e4motion.challenge.common.exception.customexception.InaccessibleException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class SecurityHelper {

    public void checkIfLoginUserForRoleUser(String username) {

        UserDto loginUser = getLoginUser();
        if (loginUser.getAuthority().equals(AuthorityName.ROLE_USER)
                && !loginUser.getUsername().equals(username)) {
            throw new InaccessibleException(InaccessibleException.ACCESS_DENIED);
        }
    }

    public UserDto getLoginUser() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails)authentication.getPrincipal();

        Set<AuthorityName> authorities = userDetails.getAuthorities().stream()
                .map(authority -> AuthorityName.valueOf(authority.getAuthority()))
                .collect(Collectors.toSet());

        return UserDto.builder()
                .username(userDetails.getUsername())
                .authority(authorities.isEmpty() ? null : authorities.iterator().next())
                .build();
    }
}
