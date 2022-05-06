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
@Table(name = "nt_link")
public class Link {

    @Id
    @Column(name = "link_id", length = 10)
    private String linkId;

    @Column(name = "start_id", length = 10)
    private String startId;

    @Column(name = "end_id", length = 10)
    private String endId;

    @Column(name = "start_name", length = 30)
    private String startName;

    @Column(name = "end_name", length = 30)
    private String endName;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "link_id")
    private List<LinkGps> gps;
}
