package com.hrproject.controller;

import com.hrproject.constant.EndPoints;
import com.hrproject.dto.request.AuthUpdateRequestDto;
import com.hrproject.repository.entity.Auth;
import com.hrproject.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/manager")
@PreAuthorize("hasAuthority('MANAGER')")
@RequiredArgsConstructor
public class ManagerController {


    private final AuthService authService;

    @GetMapping("/find_all")
    public ResponseEntity<List<Auth>> findAll(){
        return ResponseEntity.ok(authService.findAll());
    }

    @PutMapping(EndPoints.UPDATE)
    public ResponseEntity< String> updateAuth(@RequestBody AuthUpdateRequestDto dto , @RequestHeader("Authorization")String token){
        return ResponseEntity.ok(authService.updateAuth(dto));
    }







}