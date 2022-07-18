package com.e4motion.challenge.api.dto;

import com.e4motion.challenge.common.utils.DateTimeHelper;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DataStatsDto {

	@NotNull
	@JsonFormat(pattern = DateTimeHelper.dateTimeFormat, shape = JsonFormat.Shape.STRING)
	private LocalDateTime t;

	@NotNull
	private String c;

	private String i;

	private String r;

	private Integer p;

	private Integer[] sr;

	private Integer qmsrLen;

	private Integer[] qmsr;

	private Integer[] qtsr;

	private Integer[] lu;

	private Integer qmluLen;

	private Integer[] qmlu;

	private Integer[] qtlu;

}
