package com.e4motion.challenge.data.provider.domain;

import com.e4motion.challenge.common.constant.AuthorityName;
import com.e4motion.challenge.common.constant.FieldLengths;
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
	@Column(name = "authority_name", length = FieldLengths.AUTHORITY_NAME)
	@Enumerated(EnumType.STRING)
	private AuthorityName authorityName;

}
