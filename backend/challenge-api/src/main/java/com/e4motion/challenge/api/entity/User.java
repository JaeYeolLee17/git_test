package com.e4motion.challenge.api.entity;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
@Table(name = "user")
public class User implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "user_id", length = 50, unique = true)
	private String userId;
   
	@Column(name = "username", length = 50)
	private String username;

	@Column(name = "password", length = 100)
	private String password;

	@Column(name = "email", length = 100)
	private String email;
   
   	@Column(name = "activated")
   	private boolean activated;

    @ManyToMany
    @JoinTable(
    	name = "user_authority",
    	joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "user_id")},
      	inverseJoinColumns = {@JoinColumn(name = "authority_name", referencedColumnName = "authority_name")})
    private Set<Authority> authorities;
   
    @Builder
    public User(String userId, String password, String username, String email, Set<Authority> authorities, boolean activated) {
    	this.userId = userId;
    	this.password = password;
    	this.username = username;
    	this.email = email;
    	this.authorities = authorities;
    	this.activated = activated;
    }
   
}
