package com.e4motion.challenge.data.collector.controller;

import javax.annotation.security.RolesAllowed;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.e4motion.challenge.data.collector.domain.Role;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "User Admin")
@RestController 
@RequestMapping(path = "api/admin/user")
@RolesAllowed(Role.USER_ADMIN)
public class UserAdminController {

    @PostMapping
    public String create() {
    	return "user created";
    }

    @PutMapping("{id}")
    public String update(@PathVariable String id) {
    	return "user " + id + " updated";
    }

    @DeleteMapping("{id}")
    public String delete(@PathVariable String id) {
    	return "user " + id + " updated";
    }

    @GetMapping("{id}")
    public String get(@PathVariable String id) {
    	return "get user " + id;
    }
}
