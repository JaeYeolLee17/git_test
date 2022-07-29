package com.e4motion.challenge.api.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "nt_tsi")
public class Tsi extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tsi_id")
    private Long tsiId;

    @Column(name = "node_id", unique = true, nullable = false)
    private Long nodeId;

    @Column(name = "transition")
    private Boolean transition;

    @Column(name = "response")
    private Boolean response;

    @Column(name = "lights_out")
    private Boolean lightsOut;

    @Column(name = "flashing")
    private Boolean flashing;

    @Column(name = "manual")
    private Boolean manual;

    @Column(name = "error_scu")
    private Boolean errorScu;

    @Column(name = "error_center")
    private Boolean errorCenter;

    @Column(name = "error_contradiction")
    private Boolean errorContradiction;

    @Column(name = "cycle_counter")
    private Integer cycleCounter;

    @Column(name = "signal_count")
    private Integer signalCount;

    @Column(name = "time")
    private LocalDateTime time;

    @OneToMany(mappedBy = "tsi")
    private List<TsiSignal> tsiSignals;
}
