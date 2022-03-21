package com.e4motion.challenge.api.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.geo.Point;

import javax.persistence.*;
import java.util.List;

@Data
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

    @Column(name = "gps")
    @ElementCollection(targetClass=Point.class)
    private List<Point> gps;
}
