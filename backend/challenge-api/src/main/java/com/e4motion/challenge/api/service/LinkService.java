package com.e4motion.challenge.api.service;

import com.e4motion.challenge.api.dto.LinkDto;

import java.util.List;

public interface LinkService {

    LinkDto create(LinkDto linkDto);

    LinkDto update(String linkId, LinkDto linkDto);

    void delete(String linkId);

    LinkDto get(String linkId);

    List<LinkDto> getList();
}
