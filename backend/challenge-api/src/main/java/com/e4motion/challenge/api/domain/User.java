package com.e4motion.challenge.api.domain;

import java.util.Set;

import javax.persistence.*;

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
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private Long userId;

	@Column(name = "username", length = 32, unique = true, nullable = false)
	private String username;

	@Column(name = "password", length = 128, nullable = false)
	private String password;

	@Column(name = "nickname", length = 32)
	private String nickname;

	@Column(name = "email", length = 128)
	private String email;
	
	@Column(name = "phone", length = 20)
	private String phone;

	@Column(name = "disabled")
	private Boolean disabled;

    @ManyToMany
    @JoinTable(
    	name = "nt_user_authority",
    	joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "user_id")},
      	inverseJoinColumns = {@JoinColumn(name = "authority_name", referencedColumnName = "authority_name")})
    private Set<Authority> authorities;
   
}
