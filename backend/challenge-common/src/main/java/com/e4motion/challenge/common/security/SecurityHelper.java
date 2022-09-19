package com.e4motion.challenge.common.security;

import com.e4motion.challenge.common.constant.AuthorityName;
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

    public AuthorityName getLoginRole() {
        return getAuthority(getLoginUser());
    }

    public void checkIfLoginUserForRoleUser(String username) {

        checkIfLoginUserForRole(username, AuthorityName.ROLE_USER);
    }

    public void checkIfLoginCameraForRoleCamera(String cameraNo) {

        checkIfLoginUserForRole(cameraNo, AuthorityName.ROLE_CAMERA);
    }

    private void checkIfLoginUserForRole(String username, AuthorityName authority) {

        UserDetails loginUser = getLoginUser();
        AuthorityName loginAuthority = getAuthority(loginUser);

        if (authority.equals(loginAuthority) &&
                !loginUser.getUsername().equals(username)) {
            throw new InaccessibleException(InaccessibleException.ACCESS_DENIED);
        }
    }

    private AuthorityName getAuthority(UserDetails userDetails) {

        Set<AuthorityName> authorities = userDetails.getAuthorities().stream()
                .map(authority -> AuthorityName.valueOf(authority.getAuthority()))
                .collect(Collectors.toSet());

        return authorities.isEmpty() ? null : authorities.iterator().next();
    }

    private UserDetails getLoginUser() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (UserDetails)authentication.getPrincipal();
    }
}
