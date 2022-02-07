package com.e4motion.challenge.api.config;

import static java.lang.String.format;

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

import com.e4motion.challenge.api.security.JwtAuthenticationEntryPoint;
import com.e4motion.challenge.api.security.JwtTokenFilter;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
    private final JwtTokenFilter jwtTokenFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Value("${springdoc.api-docs.path}")
    private String apiDocsPath;
    @Value("${springdoc.swagger-ui.path}")
    private String swaggerPath;

    public SecurityConfig(JwtTokenFilter jwtTokenFilter, 
    		JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint) {
        this.jwtTokenFilter = jwtTokenFilter;
		this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;

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
            .antMatchers(format("%s/**", apiDocsPath)).permitAll()
            .antMatchers(format("%s/**", swaggerPath)).permitAll()
            .antMatchers("/v1/login").permitAll()
        	.antMatchers("/hello").permitAll()
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
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/v1/**", config);
        return new CorsFilter(source);
    }
    
    // Expose authentication manager bean
    @Override @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

}
