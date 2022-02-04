package com.e4motion.challenge.data.collector.dto;

import lombok.Data;

@Data
public class UserView {

    private final String id;
    private final String username;
    private final String password;
    private final String fullName;

}
