package com.e4motion.challenge.data.provider.domain;

import com.e4motion.challenge.common.domain.AuthorityName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "nt_authority")
public class Authority {
	
	@Id
	@Column(name = "authority_name")
	@Enumerated(EnumType.STRING)
	private AuthorityName authorityName;
   
}
