package com.e4motion.challenge.api.domain;

import java.util.Set;

import javax.persistence.*;

import com.e4motion.challenge.api.constant.Lengths;
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

	@Column(name = "username", length = Lengths.USERNAME, unique = true, nullable = false)
	private String username;

	@Column(name = "password", length = Lengths.PASSWORD, nullable = false)
	private String password;

	@Column(name = "nickname", length = Lengths.NICKNAME)
	private String nickname;

	@Column(name = "email", length = Lengths.EMAIL)
	private String email;
	
	@Column(name = "phone", length = Lengths.PHONE)
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
