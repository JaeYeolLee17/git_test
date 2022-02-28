package com.e4motion.challenge.data.provider.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "nt_user")
public class User {
	
	@Id
	@Column(name = "user_id")
	private String userId;

	@Column(name = "password")
	private String password;
	
	@Column(name = "username")
	private String username;

	@Column(name = "email")
	private String email;
	
	@Column(name = "phone")
	private String phone;

    @ManyToMany
    @JoinTable(
    	name = "nt_user_authority",
    	joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "user_id")},
      	inverseJoinColumns = {@JoinColumn(name = "authority_name", referencedColumnName = "authority_name")})
    private Set<Authority> authorities;
   
}
