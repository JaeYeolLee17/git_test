package com.e4motion.challenge.api.domain.entity;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "nt_user")
public class User {
	
	@Id
	@Column(name = "user_id", length = 20)
	private String userId;

	@Column(name = "password", length = 128)
	private String password;
	
	@Column(name = "username", length = 20)
	private String username;

	@Column(name = "email", length = 128)
	private String email;
	
	@Column(name = "phone", length = 20)
	private String phone;
   
   	@Column(name = "activated")
   	private boolean activated;

    @ManyToMany
    @JoinTable(
    	name = "nt_user_authority",
    	joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "user_id")},
      	inverseJoinColumns = {@JoinColumn(name = "authority_name", referencedColumnName = "authority_name")})
    private Set<Authority> authorities;
   
    @Builder
    public User(String userId, String password, String username, String email, String phone, boolean activated, Set<Authority> authorities) {
    	this.userId = userId;
    	this.password = password;
    	this.username = username;
    	this.email = email;
    	this.phone = phone;
    	this.activated = activated;
    	this.authorities = authorities;
    }
   
}
