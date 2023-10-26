package com.hrproject.controller;


import com.hrproject.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/worker")
@PreAuthorize("hasAuthority('Worker')")
@RequiredArgsConstructor
public class WorkerController {

    private final AuthService authService;
}