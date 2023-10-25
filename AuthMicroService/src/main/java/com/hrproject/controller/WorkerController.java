package com.hrproject.controller;


import com.hrproject.repository.entity.Auth;
import com.hrproject.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/worker")
@PreAuthorize("hasAuthority('Worker')")
@RequiredArgsConstructor
public class WorkerController {

    private final AuthService authService;

}
