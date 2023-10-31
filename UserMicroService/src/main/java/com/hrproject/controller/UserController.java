package com.hrproject.controller;

import com.hrproject.constant.EndPoints;
import com.hrproject.dto.request.UserLoginDto;
import com.hrproject.repository.entity.UserProfile;
import com.hrproject.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/findallbyadmin")
    public ResponseEntity<List<UserProfile>> findallbyadmin(String tokken) {
        return ResponseEntity.ok(userService.finduserprofilesbyadmin(tokken));
    }

    @GetMapping(EndPoints.FIND_BY_ID+"/{authId}")
    public ResponseEntity<UserProfile> findByAuthId(@PathVariable Long authId) {
        return ResponseEntity.ok(userService.findEmployeeByAuthId(authId));
    }
}