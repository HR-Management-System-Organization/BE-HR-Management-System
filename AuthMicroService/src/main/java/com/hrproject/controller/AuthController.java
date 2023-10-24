package com.hrproject.controller;


import com.hrproject.constant.EndPoints;
import com.hrproject.dto.request.ActivateRequestDto;
import com.hrproject.dto.request.AuthUpdateRequestDto;
import com.hrproject.dto.request.LoginRequestDto;
import com.hrproject.dto.request.RegisterRequestDto;
import com.hrproject.dto.response.RegisterResponseDto;
import com.hrproject.repository.entity.Auth;
import com.hrproject.service.AuthService;
import com.hrproject.utility.JwtTokenManager;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.hrproject.constant.EndPoints.*;

@RestController
@RequestMapping(AUTH)
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final JwtTokenManager jwtTokenManager;
    private final CacheManager cacheManager;







}
