package com.hrproject.controller;

import com.hrproject.constant.EndPoints;
import com.hrproject.dto.request.UserLoginDto;
import com.hrproject.repository.entity.UserProfile;
import com.hrproject.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
    //@GetMapping("/findallbyadmin")
    // public ResponseEntity<List<UserProfile>> findallbyadmin(String tokken){
    //    return ResponseEntity.ok(userService.finduserprofilesbyadmin(tokken));
    //  }

//    @GetMapping("/allEmployees")
//    public ResponseEntity<List<UserProfile>> getAllEmployees() {
//
////        List<UserProfile> employeeList = userService.getAllEmployees();
//
//        return ResponseEntity.ok(employeeList);
//    }

    @PostMapping("/findallbyadmin")
    public ResponseEntity<List<UserProfile>> findallbyadmin(
            @RequestParam(required = false) String token,
            @RequestHeader(required = false) String authorization,
            @RequestBody(required = false) Map<String, String> requestBody) {

        if (token != null) {
            System.out.println("Sorgu parametresinden gelen token: " + token);
            // Sorgu parametresinden gelen token'ı işleyebilirsiniz
        } else if (authorization != null) {
            // İstek başlığından gelen token'ı Bearer prefix'ini ayırarak işleyebilirsiniz
            if (authorization.startsWith("Bearer ")) {
                String tokenWithoutBearer = authorization.substring(7); // 7, "Bearer " prefix uzunluğudur
                System.out.println("İstek başlığından gelen token: " + tokenWithoutBearer);
                // tokenWithoutBearer artık Bearer prefix'inden arındırılmış token'ı içerir
                // tokenWithoutBearer değişkenini kullanarak işlemlerinizi gerçekleştirebilirsiniz
                token = tokenWithoutBearer;
            }
        } else if (requestBody != null && requestBody.containsKey("token")) {
            System.out.println("İstek gövdesinden gelen token: " + requestBody.get("token"));
            // İstek gövdesinden gelen token'ı işleyebilirsiniz
        } else {
            System.out.println("Token sağlanmadı");
        }

        // Alınan token ile işlemlerinizi gerçekleştirin
        return ResponseEntity.ok(userService.finduserprofilesbyadmin(token));
    }

    @PostMapping("/findallbyadminpending")
    public ResponseEntity<List<UserProfile>> findallbyadminpending(
            @RequestParam(required = false) String token,
            @RequestHeader(required = false) String authorization,
            @RequestBody(required = false) Map<String, String> requestBody) {

        if (token != null) {
            System.out.println("Sorgu parametresinden gelen token: " + token);
            // Sorgu parametresinden gelen token'ı işleyebilirsiniz
        } else if (authorization != null) {
            // İstek başlığından gelen token'ı Bearer prefix'ini ayırarak işleyebilirsiniz
            if (authorization.startsWith("Bearer ")) {
                String tokenWithoutBearer = authorization.substring(7); // 7, "Bearer " prefix uzunluğudur
                System.out.println("İstek başlığından gelen token: " + tokenWithoutBearer);
                // tokenWithoutBearer artık Bearer prefix'inden arındırılmış token'ı içerir
                // tokenWithoutBearer değişkenini kullanarak işlemlerinizi gerçekleştirebilirsiniz
                token = tokenWithoutBearer;
            }
        } else if (requestBody != null && requestBody.containsKey("token")) {
            System.out.println("İstek gövdesinden gelen token: " + requestBody.get("token"));
            // İstek gövdesinden gelen token'ı işleyebilirsiniz
        } else {
            System.out.println("Token sağlanmadı");
        }

        // Alınan token ile işlemlerinizi gerçekleştirin
        return ResponseEntity.ok(userService.finduserprofilesbyadminpending(token));
    }

    @PostMapping("/activationbyadmin")
    public void activasyonbyadmin(
            @RequestParam Integer authorId,
            @RequestHeader(required = false) String authorization) {
        System.out.println(authorId);
        String token = null;
        if (!authorId.equals(null)) {
            if (authorization.startsWith("Bearer ")) {
                String tokenWithoutBearer = authorization.substring(7); // 7, "Bearer " prefix uzunluğudur
                System.out.println("İstek başlığından gelen token: " + tokenWithoutBearer);
                // tokenWithoutBearer artık Bearer prefix'inden arındırılmış token'ı içerir
                // tokenWithoutBearer değişkenini kullanarak işlemlerinizi gerçekleştirebilirsiniz
                token = tokenWithoutBearer;
                System.out.println(token);

            }
            Long longSayi = (long) authorId; // int'i Long'a dönüştür

            userService.activitosyon(token, longSayi);


        }


    }

    @PostMapping("/finduserbyadmin")
    public ResponseEntity<UserProfile> findallbyadmin(
            @RequestParam(required = false) String token,
            @RequestHeader(required = false) String authorization) {
        if (authorization.startsWith("Bearer ")) {
            String tokenWithoutBearer = authorization.substring(7); // 7, "Bearer " prefix uzunluğudur
            System.out.println("İstek başlığından gelen token: " + tokenWithoutBearer);
            // tokenWithoutBearer artık Bearer prefix'inden arındırılmış token'ı içerir
            // tokenWithoutBearer değişkenini kullanarak işlemlerinizi gerçekleştirebilirsiniz
            token = tokenWithoutBearer;
        }
        System.out.println(token);
        return ResponseEntity.ok(userService.userProfilefindbidwithtokken(token));

    }

    @GetMapping(EndPoints.FIND_BY_ID+"/{authId}")
    public ResponseEntity<UserProfile> findByAuthId(@PathVariable Long authId) {
        return ResponseEntity.ok(userService.findEmployeeByAuthId(authId));
    }

}
