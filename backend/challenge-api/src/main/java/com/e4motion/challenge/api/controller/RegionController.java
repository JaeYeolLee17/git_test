package com.e4motion.challenge.api.controller;

import com.e4motion.challenge.api.dto.RegionDto;
import com.e4motion.challenge.api.service.RegionService;
import com.e4motion.challenge.common.response.Response;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Tag(name = "5. Region")
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "v1")
public class RegionController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final RegionService regionService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/region")
    public Response create(@Valid @RequestBody RegionDto regionDto) throws Exception {

        return new Response("region", regionService.create(regionDto));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/region/{regionId}")
    public Response update(@PathVariable String regionId, @RequestBody RegionDto regionDto) throws Exception {

        return new Response("region", regionService.update(regionId, regionDto));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/region/{regionId}")
    public Response delete(@PathVariable String regionId) throws Exception {

        regionService.delete(regionId);

        return new Response();
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_USER')")
    @GetMapping("/region/{regionId}")
    public Response get(@PathVariable String regionId) throws Exception {

        logger.debug(" regionId : " + regionId);
        return new Response("region", regionService.get(regionId));
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_USER')")
    @GetMapping("/regions")
    public Response getList() throws Exception {

        return new Response("regions", regionService.getList());
    }
}
