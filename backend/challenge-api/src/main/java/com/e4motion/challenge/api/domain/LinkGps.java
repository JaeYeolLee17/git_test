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
@Table(name = "nt_link_gps")
public class LinkGps {

    @Id
    @Column(name = "latitude")
    private double latitude;

    @Column(name = "longitude")
    private double longitude;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "link_id", insertable = false, updatable = false)
    private Link link;
}
