package com.e4motion.challenge.api.service.impl;

import com.e4motion.challenge.api.domain.Link;
import com.e4motion.challenge.api.dto.LinkDto;
import com.e4motion.challenge.api.mapper.LinkMapper;
import com.e4motion.challenge.api.repository.LinkRepository;
import com.e4motion.challenge.api.service.LinkService;
import com.e4motion.challenge.common.exception.customexception.LinkDuplicationException;
import com.e4motion.challenge.common.exception.customexception.LinkNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class LinkServiceImpl implements LinkService {

    private final LinkRepository linkRepository;
    private final LinkMapper linkMapper;

    @Transactional
    public LinkDto create(LinkDto linkDto) {

        linkRepository.findByLinkId(linkDto.getLinkId())
                .ifPresent(intersection -> {
                    throw new LinkDuplicationException(LinkDuplicationException.LINK_ID_ALREADY_EXISTS);
                });

        Link link = Link.builder()
                .startId(linkDto.getStartId())
                .endId(linkDto.getEndId())
                .startName(linkDto.getStartName())
                .endName(linkDto.getEndName())
                .gps(linkMapper.toGps(linkDto.getGps()))
                .linkId(linkDto.getLinkId())
                .build();

        return linkMapper.toLinkDto(linkRepository.save(link));
    }

    @Transactional
    public LinkDto update(String linkId, LinkDto linkDto) {

        return linkRepository.findByLinkId(linkId)
                .map(link -> {
                    if (linkDto.getStartId() != null) {
                        link.setStartId(linkDto.getStartId());
                    }

                    if (linkDto.getEndId() != null) {
                        link.setEndId(linkDto.getEndId());
                    }

                    if (linkDto.getStartName() != null) {
                        link.setStartName(linkDto.getStartName());
                    }

                    if (linkDto.getEndName() != null) {
                        link.setEndName(linkDto.getEndName());
                    }

                    //TODO::how change type ??
//                    if (linkDto.getGps() != null) {
//                        link.setGps(linkDto.getGps());
//                    }

                    return linkMapper.toLinkDto(linkRepository.save(link));
                }).orElseThrow(() -> new LinkNotFoundException(LinkNotFoundException.INVALID_LINK_ID));
    }

    @Transactional
    public void delete(String linkId) {

        linkRepository.deleteByLinkId(linkId);
    }

    @Transactional
    public LinkDto get(String linkId) {

        return linkMapper.toLinkDto(linkRepository.findByLinkId(linkId).orElse(null));
    }

    @Transactional
    public List<LinkDto> getList() {

        return linkMapper.toLinkDto(linkRepository.findAll());
    }
}
