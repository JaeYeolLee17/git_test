package com.e4motion.challenge.api.controller;

import com.e4motion.challenge.api.dto.LinkDto;
import com.e4motion.challenge.api.service.LinkService;
import com.e4motion.challenge.common.response.Response;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Tag(name = "6. Link")
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "v1")
public class LinkController {

    private final LinkService linkService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/link")
    public Response create(@Valid @RequestBody LinkDto linkDto) throws Exception {

        return new Response("link", linkService.create(linkDto));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/link/{linkId}")
    public Response update(@PathVariable String linkId, @RequestBody LinkDto linkDto) throws Exception {

        return new Response("link", linkService.update(linkId, linkDto));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/link/{linkId}")
    public Response delete(@PathVariable String linkId) throws Exception {

        linkService.delete(linkId);

        return new Response();
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_USER')")
    @GetMapping("/link/{linkId}")
    public Response get(@PathVariable String linkId) throws Exception {

        return new Response("link", linkService.get(linkId));
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_USER')")
    @GetMapping("/links")
    public Response getList() throws Exception {

        return new Response("links", linkService.getList());
    }
}
