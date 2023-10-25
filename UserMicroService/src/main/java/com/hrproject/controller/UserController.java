package com.hrproject.controller;

import com.hrproject.dto.request.UserLoginDto;

import com.hrproject.service.UserService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;



import static com.hrproject.constant.EndPoints.*;

@RestController
@RequestMapping(USER)
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @PostMapping("/activationcode")
    public ResponseEntity<String> activationcode(@RequestParam String tokken){
        String sonuc=userService.activation(tokken);
        String https="http://127.0.0.1:5500/"+sonuc+".html";
        return ResponseEntity.status(HttpStatus.FOUND)
                .header("Location", https)  // Yönlendirilecek sayfanın URL'sini belirtin
                .build();
    }

    @PostMapping(LOGIN)
    public ResponseEntity<String> dologin(@RequestBody UserLoginDto dto){
        return ResponseEntity.ok(userService.logindto(dto));
    }




}