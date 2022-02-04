package com.e4motion.challenge.data.collector.domain;

import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.userdetails.UserDetails;

import lombok.Data;

//@Entity
@Data
public class User implements UserDetails {

    //@Id
    private Integer id;
    
    private boolean enabled = true;

    //@Indexed(unique=true)
    private String username;
    private String password;
    //@Indexed
    private String fullName;
    private Set<Role> authorities = new HashSet<>();

    public User() {
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.enabled = true;
    }

    @Override
    public boolean isAccountNonExpired() {
        return enabled;
    }

    @Override
    public boolean isAccountNonLocked() {
        return enabled;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return enabled;
    }

}
