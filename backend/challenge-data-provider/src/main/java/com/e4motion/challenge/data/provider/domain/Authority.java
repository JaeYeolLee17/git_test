package com.e4motion.challenge.data.provider.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

import com.e4motion.challenge.common.domain.AuthorityName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
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
