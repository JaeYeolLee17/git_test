package com.e4motion.challenge.api.domain;

import com.e4motion.challenge.api.constant.TsiSignalInfo;
import com.e4motion.challenge.api.constant.TsiSignalStatus;
import com.e4motion.challenge.api.constant.TsiTimeReliability;
import com.e4motion.challenge.common.constant.FieldLengths;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
//@ToString                             // remove circular reference.
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "nt_tsi_signal", uniqueConstraints = @UniqueConstraint(columnNames = {"tsi_id", "info", "direction"}))
public class TsiSignal extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tsi_signal_id")
    private Long tsiSignalId;

    @ManyToOne
    @JoinColumn(name = "tsi_id")
    private Tsi tsi;

    @Column(name = "info", length = FieldLengths.TSI_SIGNAL_INFO)
    @Enumerated(EnumType.STRING)
    private TsiSignalInfo info;

    @Column(name = "time_reliability", length = FieldLengths.TSI_TIME_RELIABILITY)
    @Enumerated(EnumType.STRING)
    private TsiTimeReliability timeReliability;

    @Column(name = "person")
    private Boolean person;

    @Column(name = "status", length = FieldLengths.TSI_SIGNAL_STATUS)
    @Enumerated(EnumType.STRING)
    private TsiSignalStatus status;

    @Column(name = "display_time")
    private Integer displayTime;

    @Column(name = "remain_time")
    private Integer remainTime;

    @Column(name = "direction")
    private Integer direction;

    @Override
    public String toString() {
        return "TsiSignal{" +
                "tsiSignalId=" + tsiSignalId +
                ", tsi=" + tsi.getNodeId() +
                ", info=" + info +
                ", timeReliability=" + timeReliability +
                ", person=" + person +
                ", status=" + status +
                ", displayTime=" + displayTime +
                ", remainTime=" + remainTime +
                ", direction=" + direction +
                '}';
    }
}
