package com.e4motion.challenge.api.service.impl;

import com.e4motion.challenge.api.domain.*;
import com.e4motion.challenge.api.dto.LinkDto;
import com.e4motion.challenge.api.mapper.LinkMapper;
import com.e4motion.challenge.api.repository.IntersectionRepository;
import com.e4motion.challenge.api.repository.LinkRepository;
import com.e4motion.challenge.api.service.LinkService;
import com.e4motion.challenge.common.exception.customexception.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class LinkServiceImpl implements LinkService {

    private final LinkRepository linkRepository;
    private final IntersectionRepository intersectionRepository;
    private final LinkMapper linkMapper;

    @Transactional
    public LinkDto create(LinkDto linkDto) {

        linkRepository.findByStart_IntersectionNoAndEnd_IntersectionNo(linkDto.getStart().getIntersectionNo(), linkDto.getEnd().getIntersectionNo())
                .ifPresent(link -> {
                    throw new LinkDuplicateException(LinkDuplicateException.LINK_START_END_ALREADY_EXISTS);
                });

        Link link = linkMapper.toLink(linkDto);
        if (link.getStart() != null) {
            link.setStart(getIntersection(link.getStart().getIntersectionNo()));
        }
        if (link.getEnd() != null) {
            link.setEnd(getIntersection(link.getEnd().getIntersectionNo()));
        }
        link.setGps(getLinkGps(linkDto, link));

        return linkMapper.toLinkDto(linkRepository.save(link));
    }

    @Transactional
    public LinkDto update(Long linkId, LinkDto linkDto) {

        return linkRepository.findByLinkId(linkId)
                .map(link -> {

                    if (linkDto.getStart() != null) {
                        link.setStart(getIntersection(linkDto.getStart().getIntersectionNo()));
                    }

                    if (linkDto.getEnd() != null) {
                        link.setEnd(getIntersection(linkDto.getEnd().getIntersectionNo()));
                    }

                    if (linkDto.getGps() != null) {
                        link.getGps().clear();
                        linkRepository.saveAndFlush(link);

                        List<LinkGps> linkGps = getLinkGps(linkDto, link);
                        link.getGps().addAll(linkGps);
                    }

                    return linkMapper.toLinkDto(linkRepository.saveAndFlush(link));
                })
                .orElseThrow(() -> new LinkNotFoundException(LinkNotFoundException.INVALID_LINK_ID));
    }

    @Transactional
    public void delete(Long linkId) {

        linkRepository.deleteByLinkId(linkId);
    }

    @Transactional(readOnly = true)
    public LinkDto get(Long linkId) {

        return linkMapper.toLinkDto(linkRepository.findByLinkId(linkId).orElse(null));
    }

    @Transactional(readOnly = true)
    public List<LinkDto> getList() {

        Sort sort = Sort.by("linkId").ascending();
        return linkMapper.toLinkDto(linkRepository.findAll(sort));
    }

    private Intersection getIntersection(String intersectionNo) {

        return intersectionRepository.findByIntersectionNo(intersectionNo)
                .orElseThrow(() -> new IntersectionNotFoundException(IntersectionNotFoundException.INVALID_INTERSECTION_NO));
    }

    private List<LinkGps> getLinkGps(LinkDto linkDto, Link link) {
        if (linkDto.getGps() != null) {
            AtomicInteger order = new AtomicInteger();
            return linkDto.getGps().stream()
                    .filter(gps -> gps.getLat() != null && gps.getLng() != null)
                    .map(gps ->
                            LinkGps.builder()
                                    .link(link)
                                    .lat(gps.getLat())
                                    .lng(gps.getLng())
                                    .gpsOrder(order.incrementAndGet())
                            .build())
                    .collect(Collectors.toList());
        }
        return null;
    }
}
