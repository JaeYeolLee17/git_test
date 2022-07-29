package com.e4motion.challenge.api.domain;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "nt_tsi_node")
public class TsiNode extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tsi_node_id")
    private Long tsiNodeId;

    @Column(name = "node_id", unique = true, nullable = false)
    private Long nodeId;

    @Column(name = "node_name", length = 32)
    private String nodeName;

    @Column(name = "lat")
    private Double lat;

    @Column(name = "lng")
    private Double lng;

}