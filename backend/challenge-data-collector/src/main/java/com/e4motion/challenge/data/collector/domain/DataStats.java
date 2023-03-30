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
	@Column(name = "s0")
	private Integer s0;

	@Column(name = "s1")
	private Integer s1;

	@Column(name = "s2")
	private Integer s2;

	@Column(name = "s3")
	private Integer s3;

	@Column(name = "s4")
	private Integer s4;

	@Column(name = "s5")
	private Integer s5;

	@Column(name = "r0")
	private Integer r0;

	@Column(name = "r1")
	private Integer r1;

	@Column(name = "r2")
	private Integer r2;

	@Column(name = "r3")
	private Integer r3;

	@Column(name = "r4")
	private Integer r4;

	@Column(name = "r5")
	private Integer r5;

	@Column(name = "l0")
	private Integer l0;

	@Column(name = "l1")
	private Integer l1;

	@Column(name = "l2")
	private Integer l2;

	@Column(name = "l3")
	private Integer l3;

	@Column(name = "l4")
	private Integer l4;

	@Column(name = "l5")
	private Integer l5;

	@Column(name = "u0")
	private Integer u0;

	@Column(name = "u1")
	private Integer u1;

	@Column(name = "u2")
	private Integer u2;

	@Column(name = "u3")
	private Integer u3;

	@Column(name = "u4")
	private Integer u4;

	@Column(name = "u5")
	private Integer u5;

	// TODO: queue total s, r, l, u 합산, 차종별 합산.
	@Column(name = "qt0")
	private Integer qt0;

	@Column(name = "qt1")
	private Integer qt1;

	@Column(name = "qt2")
	private Integer qt2;

	@Column(name = "qt3")
	private Integer qt3;

	@Column(name = "qt4")
	private Integer qt4;

	@Column(name = "qt5")
	private Integer qt5;

	@Column(name = "qt_t")
	private Integer qtT;

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

}
