package com.e4motion.challenge.common.config;

import com.e4motion.challenge.common.security.JwtAccessDeniedHandler;
import com.e4motion.challenge.common.security.JwtAuthenticationEntryPoint;
import com.e4motion.challenge.common.security.JwtTokenFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

import static java.lang.String.format;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
    private final JwtTokenFilter jwtTokenFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Value("${management.endpoints.web.base-path}")
    private String actuatorPath;
    @Value("${springdoc.api-docs.path}")
    private String apiDocsPath;
    @Value("${springdoc.swagger-ui.path}")
    private String swaggerPath;

    public SecurityConfig(JwtTokenFilter jwtTokenFilter, 
    		JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint,
    		JwtAccessDeniedHandler jwtAccessDeniedHandler) {

        this.jwtTokenFilter = jwtTokenFilter;
		this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
		this.jwtAccessDeniedHandler = jwtAccessDeniedHandler;

        // Inherit security context in async function calls
        SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring()
                .antMatchers(
                        "/h2-console/**"
                        ,"/favicon.ico"
                        ,"/error"
                );
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors().and().csrf().disable()

        	    .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)

        	    // enable h2-console
        	    .and()
        	    .headers()
        	    .frameOptions()
        	    .sameOrigin()

        	    // Set session management to stateless
                .and()
        	    .sessionManagement()
        	    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

        	    .and()
        	    .authorizeRequests()
        	    .antMatchers(format("%s/**", actuatorPath)).permitAll()	// TODO: apply secure.
                .antMatchers(format("%s/**", apiDocsPath)).permitAll()	// TODO: apply secure.
                .antMatchers(format("%s/**", swaggerPath)).permitAll()	// TODO: apply secure.
                .antMatchers("/v1/login").permitAll()
                .antMatchers("/v1/camera/login").permitAll()
        	    .anyRequest().authenticated()

        	    .and()
        	    .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    // Used by spring security if CORS is enabled.
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOrigins(Arrays.asList("http://localhost:3000", "http://localhost:8090",
                                                "http://192.168.0.231:3000", "http://192.168.0.231:8090", // dev challenge 01
                                                "http://192.168.0.17:3000", "http://192.168.0.17:8090",	// inho's
                                                "http://192.168.0.154:3000", "http://192.168.0.154:8090")); // sujin's
        config.setAllowedMethods(Arrays.asList("HEAD", "GET", "POST", "PUT", "DELETE", "PATCH"));
        config.setAllowedHeaders(Arrays.asList(
                "Authorization",
                "Accept",
                "Cache-Control",
                "Content-Type",
                "Origin",
                "ajax", // <-- This is needed for jQuery's ajax request.
                "x-csrf-token",
                "x-requested-with"
        ));

        config.setAllowCredentials(true);
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
    
    // Expose authentication manager bean
    @Override @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

}
