package com.e4motion.challenge.data.collector.domain;

import org.springframework.security.core.GrantedAuthority;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class Role implements GrantedAuthority {

    public static final String USER_ADMIN = "ROLE_USER_ADMIN";
    public static final String AUTHOR_ADMIN = "ROLE_AUTHOR_ADMIN";
    public static final String BOOK_ADMIN = "ROLE_BOOK_ADMIN";

    private String authority;

}
