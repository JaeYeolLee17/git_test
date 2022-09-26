package com.e4motion.challenge.api.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

import com.e4motion.challenge.common.constant.AuthorityName;

import com.e4motion.challenge.common.constant.FieldLengths;
import lombok.*;

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
