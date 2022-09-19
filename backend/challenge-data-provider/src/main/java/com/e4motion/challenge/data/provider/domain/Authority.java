package com.e4motion.challenge.data.provider.domain;

import com.e4motion.challenge.common.constant.AuthorityName;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "nt_authority")
public class Authority {

	@Id
	@Column(name = "authority_name", length = 20)
	@Enumerated(EnumType.STRING)
	private AuthorityName authorityName;

}
