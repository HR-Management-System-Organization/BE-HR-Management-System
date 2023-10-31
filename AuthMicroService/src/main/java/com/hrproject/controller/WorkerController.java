package com.hrproject.controller;


import com.hrproject.repository.entity.Auth;
import com.hrproject.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/employee")
@PreAuthorize("hasAuthority('EMPLOYEE')")
@RequiredArgsConstructor
public class WorkerController {

    private final AuthService authService;

    @PostMapping("getCompany")
    public ResponseEntity<String> getCompanyName(Long id) {
        return ResponseEntity.ok(authService.findCompanyNameById(id));
    }

}