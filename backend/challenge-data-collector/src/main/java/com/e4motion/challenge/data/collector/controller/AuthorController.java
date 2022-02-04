package com.e4motion.challenge.data.collector.controller;

import javax.annotation.security.RolesAllowed;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.e4motion.challenge.data.collector.domain.Role;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Author")
@RestController 
@RequestMapping(path = "api/author")
public class AuthorController {

    //@RolesAllowed(Role.AUTHOR_ADMIN)
	@PreAuthorize("hasRole('ROLE_AUTHOR_ADMIN')")
    @PostMapping
    public String create() {
        return "author created";
    }

    @RolesAllowed(Role.AUTHOR_ADMIN)
    @PutMapping("{id}")
    public String edit(@PathVariable String id) {
        return "author " + id + " updated";
    }

    @RolesAllowed(Role.AUTHOR_ADMIN)
    @DeleteMapping("{id}")
    public String delete(@PathVariable String id) {
        return "author " + id + " deleted";
    }

    @GetMapping("{id}")
    public String get(@PathVariable String id) {
        return "get author " + id;
    }
    
}
