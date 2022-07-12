package com.e4motion.challenge.api.service;

import com.e4motion.challenge.api.dto.LinkDto;

import java.util.List;

public interface LinkService {

    LinkDto create(LinkDto linkDto);

    LinkDto update(Long linkId, LinkDto linkDto);

    void delete(Long linkId);

    LinkDto get(Long linkId);

    List<LinkDto> getList();
}
