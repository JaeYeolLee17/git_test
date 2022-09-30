package com.e4motion.challenge.data.provider.domain;

import com.e4motion.challenge.common.constant.FieldLengths;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

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

	@Column(name = "username", length = FieldLengths.USERNAME, unique = true, nullable = false)
	private String username;

	@Column(name = "password", length = FieldLengths.PASSWORD, nullable = false)
	private String password;

	@Column(name = "nickname", length = FieldLengths.NICKNAME)
	private String nickname;

	@Column(name = "email", length = FieldLengths.EMAIL)
	private String email;

	@Column(name = "phone", length = FieldLengths.PHONE)
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
