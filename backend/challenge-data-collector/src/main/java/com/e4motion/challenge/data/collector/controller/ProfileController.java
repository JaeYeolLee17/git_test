package com.e4motion.challenge.data.collector.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Profile")
@RestController
public class ProfileController {

	@GetMapping("/profile")
	public String profile(@Value("${profile.message}") String message) {
		return message;
	}
	
}
