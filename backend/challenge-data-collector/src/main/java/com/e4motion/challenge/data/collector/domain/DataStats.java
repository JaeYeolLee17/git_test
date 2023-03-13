package com.e4motion.challenge.data.collector.domain;

import com.e4motion.challenge.common.constant.FieldLengths;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "lt_traffic_data_m15", uniqueConstraints = @UniqueConstraint(columnNames = {"t", "c"}))
public class DataStats {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "t", nullable = false)
	private LocalDateTime t;

	@Column(name = "c", length = FieldLengths.CAMERA_NO, nullable = false)
	private String c;

	@Column(name = "i", length = FieldLengths.INTERSECTION_NO)
	private String i;

	@Column(name = "r", length = FieldLengths.REGION_NO)
	private String r;
	
	@Column(name = "p")
	private Integer p;

	// TODO: s, r, l, u 분리, 차종별 합산.
	@Column(name = "sr0")
	private Integer sr0;

	@Column(name = "sr1")
	private Integer sr1;

	@Column(name = "sr2")
	private Integer sr2;

	@Column(name = "sr3")
	private Integer sr3;

	@Column(name = "sr4")
	private Integer sr4;

	// TODO: queue max 관련 삭제
	@Column(name = "qmsr_len")
	private Integer qmsrLen;

	@Column(name = "qmsr0")
	private Integer qmsr0;

	@Column(name = "qmsr1")
	private Integer qmsr1;

	@Column(name = "qmsr2")
	private Integer qmsr2;

	@Column(name = "qmsr3")
	private Integer qmsr3;

	@Column(name = "qmsr4")
	private Integer qmsr4;

	// TODO: queue total s, r, l, u 합산, 차종별 합산.
	@Column(name = "qtsr0")
	private Integer qtsr0;

	@Column(name = "qtsr1")
	private Integer qtsr1;

	@Column(name = "qtsr2")
	private Integer qtsr2;

	@Column(name = "qtsr3")
	private Integer qtsr3;

	@Column(name = "qtsr4")
	private Integer qtsr4;

	@Column(name = "lu0")
	private Integer lu0;

	@Column(name = "lu1")
	private Integer lu1;

	@Column(name = "lu2")
	private Integer lu2;

	@Column(name = "lu3")
	private Integer lu3;

	@Column(name = "lu4")
	private Integer lu4;

	@Column(name = "qmlu_len")
	private Integer qmluLen;

	@Column(name = "qmlu0")
	private Integer qmlu0;

	@Column(name = "qmlu1")
	private Integer qmlu1;

	@Column(name = "qmlu2")
	private Integer qmlu2;

	@Column(name = "qmlu3")
	private Integer qmlu3;

	@Column(name = "qmlu4")
	private Integer qmlu4;

	@Column(name = "qtlu0")
	private Integer qtlu0;

	@Column(name = "qtlu1")
	private Integer qtlu1;

	@Column(name = "qtlu2")
	private Integer qtlu2;

	@Column(name = "qtlu3")
	private Integer qtlu3;

	@Column(name = "qtlu4")
	private Integer qtlu4;

	@Column(name = "qt_t")
	private Integer qtT;

}
