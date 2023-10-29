package com.hrproject.controller;

import com.hrproject.dto.request.LoginRequestDto;
import com.hrproject.dto.request.RegisterGuestRequestDto;
import com.hrproject.dto.request.RegisterRequestDto;
import com.hrproject.dto.response.RegisterResponseDto;
import com.hrproject.service.AuthService;
import com.hrproject.utility.JwtTokenManager;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import static com.hrproject.constant.EndPoints.*;

@RestController
@RequestMapping(AUTH)
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {

    private final AuthService authService;

    private final JwtTokenManager jwtTokenManager;

    @PostMapping(LOGIN)
    public ResponseEntity<String> login(@RequestBody LoginRequestDto dto) {

        return ResponseEntity.ok(authService.login(dto));
    }

    @GetMapping("/activation")
    public RedirectView activation(String token) {
        return authService.activation(token);
    }

    @PostMapping(REGISTER + "_with_rabbitmqguset")
    public ResponseEntity<RegisterResponseDto> registerWithRabbitMq(@RequestBody @Valid RegisterGuestRequestDto dto) {
        System.out.println(dto.toString());
        System.out.println("burdasin1");

        return ResponseEntity.ok(authService.registerWithRabbitMq(dto));
    }

    @PostMapping(REGISTER + "_with_rabbitmq")
    public ResponseEntity<RegisterResponseDto> registerWithRabbitMq(@RequestBody @Valid RegisterRequestDto dto) {

        System.out.println("burdasin2");

        return ResponseEntity.ok(authService.registerWithRabbitMq(dto));
    }
}
