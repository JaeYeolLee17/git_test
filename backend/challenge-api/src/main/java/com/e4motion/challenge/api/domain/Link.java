package com.e4motion.challenge.api.domain;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "nt_link", uniqueConstraints = {@UniqueConstraint(columnNames = {"start_id", "end_id"})})
public class Link extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "link_id")
    private Long linkId;

    @ManyToOne
    @JoinColumn(name = "start_id")
    private Intersection start;

    @ManyToOne
    @JoinColumn(name = "end_id")
    private Intersection end;

    @OneToMany(mappedBy = "link", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("gps_order ASC")
    private List<LinkGps> gps;

}