package com.e4motion.challenge.api.controller;

import com.e4motion.challenge.api.dto.CameraDto;
import com.e4motion.challenge.api.dto.IntersectionDto;
import com.e4motion.challenge.api.service.IntersectionService;
import com.e4motion.challenge.common.response.Response;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Tag(name = "4. Intersection")
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "v1")
public class IntersectionController {

    private final IntersectionService intersectionService;


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/intersection")
    public Response create(@Valid @RequestBody IntersectionDto intersectionDto) throws Exception {

        return new Response("intersection", intersectionService.create(intersectionDto));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/intersection/{intersectionId}")
    public Response update(@PathVariable String intersectionId, @RequestBody IntersectionDto intersectionDto) throws Exception {

        return new Response("intersection", intersectionService.update(intersectionId, intersectionDto));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/intersection/{intersectionId}")
    public Response delete(@PathVariable String intersectionId) throws Exception {

        intersectionService.delete(intersectionId);

        return new Response();
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_USER')")
    @GetMapping("/intersection/{intersectionId}")
    public Response get(@PathVariable String intersectionId) throws Exception {

        return new Response("intersection", intersectionService.get(intersectionId));
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_USER')")
    @GetMapping("/intersections")
    public Response getList() throws Exception {

        return new Response("intersections", intersectionService.getList());
    }
}
