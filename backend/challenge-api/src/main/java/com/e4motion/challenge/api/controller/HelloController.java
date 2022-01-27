package com.e4motion.challenge.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.e4motion.common.MyService;

@RestController
public class HelloController {

	private final MyService myService;

	public HelloController(MyService myService) {
		this.myService = myService;
	}

	@GetMapping("/hello")
	public String hello() {
		return myService.message();
	}
}
