package com.e4motion.challenge.api.dto;

import com.e4motion.challenge.api.constant.TsiSignalInfo;
import com.e4motion.challenge.api.constant.TsiSignalStatus;
import lombok.*;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TsiSignalInfoDto {

    private TsiSignalInfo info;

    private TsiSignalStatus status;

}
