package com.e4motion.challenge.data.collector.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.e4motion.common.MyService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Hello")
@RequiredArgsConstructor
@RestController
public class HelloController {

	private final MyService myService;

	@GetMapping("/hello")
	public String hello() {
		return myService.message();
	}
	
}
