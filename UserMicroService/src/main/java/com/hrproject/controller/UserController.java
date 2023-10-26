package com.hrproject.controller;

import com.hrproject.dto.request.UserLoginDto;
import com.hrproject.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.hrproject.constant.EndPoints.LOGIN;
import static com.hrproject.constant.EndPoints.USER;

@RestController
@RequestMapping(USER)
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

    private final UserService userService;

    @PostMapping(LOGIN)
    public ResponseEntity<String> dologin(@RequestBody UserLoginDto dto) {

        return ResponseEntity.ok(userService.logindto(dto));
    }

    @PostMapping("/addAnnualPermission ")
    public ResponseEntity<String> addAnnualPermission(@RequestBody UserLoginDto dto, @RequestParam int days) {

        return ResponseEntity.ok(userService.requestAnnualLeave(dto, days));
    }

    @PostMapping("/addAParentalPermission ")
    public ResponseEntity<String> addAParentalPermission(@RequestBody UserLoginDto dto, @RequestParam int days) {

        return ResponseEntity.ok(userService.requestParentalLeave(dto, days));
    }

    @GetMapping("/getTotalAnnualLeave/{username}")
    public ResponseEntity<Integer> getPermission(@RequestBody UserLoginDto dto) {

        return ResponseEntity.ok(userService.getTotalAnnualLeave(dto));
    }
}