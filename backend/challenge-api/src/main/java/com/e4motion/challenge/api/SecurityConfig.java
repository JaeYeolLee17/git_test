package com.e4motion.challenge.api;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
public class SecurityConfig {

	// @formatter:off
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
			.mvcMatcher("/**")
				.authorizeRequests()
					.mvcMatchers("/profile/**").access("hasAuthority('SCOPE_product:read')")
					.and()
			.oauth2ResourceServer()
				.jwt();
		return http.build();
	}
	// @formatter:on
	
//	@Bean
//    SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
//		http
//			.authorizeExchange()
//				.pathMatchers("/actuator/**").permitAll()
//				.pathMatchers(POST, "/product-composite/**").hasAuthority("SCOPE_product:write")
//				.pathMatchers(DELETE, "/product-composite/**").hasAuthority("SCOPE_product:write")
//				.pathMatchers(GET, "/product-composite/**").hasAuthority("SCOPE_product:read")
//				.anyExchange().authenticated()
//				.and()
//			.oauth2ResourceServer()
//				.jwt();
//		return http.build();
//	}
}
