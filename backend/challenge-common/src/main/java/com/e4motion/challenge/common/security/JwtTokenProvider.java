package com.e4motion.challenge.common.security;

import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtTokenProvider {
	
	private static final String AUTHORITIES_KEY = "auth";
    
    private String secret;
    private long tokenValidityInMilliseconds;

    public JwtTokenProvider(
    	      @Value("${jwt.secret}") String secret,
    	      @Value("${jwt.token-validity-in-seconds}") long tokenValidityInSeconds) {
    	      this.secret = Base64.getEncoder().encodeToString(secret.getBytes());
    	      this.tokenValidityInMilliseconds = tokenValidityInSeconds * 1000;
    }
    
    public String createToken(Authentication authentication) {
    	String authorities = authentication.getAuthorities().stream()
    	         .map(GrantedAuthority::getAuthority)
    	         .collect(Collectors.joining(","));
    	
    	long now = (new Date()).getTime();
    	Date validity = new Date(now + tokenValidityInMilliseconds);
    	      
        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim(AUTHORITIES_KEY, authorities)
                .setIssuedAt(new Date())
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public Authentication getAuthentication(String token) {
    	Claims claims = Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();

        Collection<? extends GrantedAuthority> authorities =
           Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
              .map(SimpleGrantedAuthority::new)
              .collect(Collectors.toList());

        User principal = new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
     }

     public boolean validateToken(String token) {
    	 try {
             Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
             return true;
         } catch (SignatureException ex) {
             log.error("Invalid JWT signature - {}", ex.getMessage());
         } catch (MalformedJwtException ex) {
        	 log.error("Invalid JWT token - {}", ex.getMessage());
         } catch (ExpiredJwtException ex) {
        	 log.error("Expired JWT token - {}", ex.getMessage());
         } catch (UnsupportedJwtException ex) {
        	 log.error("Unsupported JWT token - {}", ex.getMessage());
         } catch (IllegalArgumentException ex) {
        	 log.error("JWT claims string is empty - {}", ex.getMessage());
         }
         return false;
     }

}
