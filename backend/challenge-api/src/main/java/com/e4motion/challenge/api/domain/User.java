package com.e4motion.challenge.api.domain;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "nt_user")
public class User extends BaseTimeEntity {
	
	@Id
	@Column(name = "user_id", length = 20)
	private String userId;

	@Column(name = "password", length = 128, nullable = false)
	private String password;
	
	@Column(name = "username", length = 20, nullable = false)
	private String username;

	@Column(name = "email", length = 128)
	private String email;
	
	@Column(name = "phone", length = 20)
	private String phone;

	@Column(name = "enabled", nullable = false)
	private Boolean enabled;

    @ManyToMany
    @JoinTable(
    	name = "nt_user_authority",
    	joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "user_id")},
      	inverseJoinColumns = {@JoinColumn(name = "authority_name", referencedColumnName = "authority_name")})
    private Set<Authority> authorities;
   
}
