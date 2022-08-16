package com.e4motion.challenge.api.repository;

import com.e4motion.challenge.api.dto.TsiDto;
import com.e4motion.challenge.common.domain.TsiFilterBy;

import java.util.List;

public interface TsiRepositoryCustom {

    List<TsiDto> getList(TsiFilterBy filterBy, String filterValue);

}
