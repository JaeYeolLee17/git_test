package com.e4motion.challenge.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.e4motion")
public class ChallengeApiApplication {

	private static final Logger LOG = LoggerFactory.getLogger(ChallengeApiApplication.class);
	
	public static void main(String[] args) {
		SpringApplication.run(ChallengeApiApplication.class, args);
		
		LombokTest lombokTest = new LombokTest(1, "b");
		LOG.info(lombokTest.getA() + ", " + lombokTest.getB());
		lombokTest.setA(1000);
		lombokTest.setB("bbb"); 
		LOG.info(lombokTest.toString());
	}

}
