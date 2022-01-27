package com.e4motion.challenge.data.collector;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.e4motion")
public class ChallengeDataCollectorApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChallengeDataCollectorApplication.class, args);
	}

}
