package com.e4motion.challenge.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.e4motion.common.MyService;

@RestController
public class ProfileController {

	@GetMapping("/profile")
	public String profile(@Value("${profile.message}") String message) {
		return message;
	}
	
}
