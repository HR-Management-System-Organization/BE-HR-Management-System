package com.hrproject.controller;

import com.hrproject.constant.EndPoints;
import com.hrproject.dto.request.*;
import com.hrproject.repository.entity.Izintelebi;
import com.hrproject.repository.entity.UserProfile;
import com.hrproject.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
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









    @GetMapping(EndPoints.FIND_BY_ID + "/{authId}")
    public ResponseEntity<UserProfile> findByAuthId(@PathVariable Long authId) {
        return ResponseEntity.ok(userService.findEmployeeByAuthId(authId));
    }

    @GetMapping("/allEmployees/{companyId}")
    public ResponseEntity<List<UserProfile>> getAllEmployees(@PathVariable Long companyId) {


        return ResponseEntity.ok(userService.getAllEmployees(companyId));
    }


    @PostMapping("/addemployee/{id}")
    public ResponseEntity<String> addEmployee(@PathVariable Long id, AddEmployeeDto dto){

        userService.addEmployee(id,dto);

        return ResponseEntity.ok("Kayıt oldu");
    }
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
            String tokenWithoutBearer = requestBody.get("token").substring(7); // 7, "Bearer " prefix uzunluğudur
            // İstek gövdesinden gelen token'ı işleyebilirsiniz
            token = tokenWithoutBearer;
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


    @PutMapping("/update1" + "/{Id}")
    public ResponseEntity<UserProfile> updateById(@PathVariable Long Id, @RequestBody UserProfileUpdateRequestDto dto) {

        return ResponseEntity.ok(userService.updateprofile(Id,dto));
    }

    @PostMapping("/findallguestbycompanymanager")
    public ResponseEntity<List<UserProfile>> findallguestbycompanymanager(
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
        return ResponseEntity.ok(userService.findallguestbycompanymanager(token));
    }@CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/izinal")
    public ResponseEntity<Boolean> izintalebi( @RequestParam(required = false) String token,
                                               @RequestHeader(required = false) String authorization,
                                               @RequestBody(required = false) Map<String, String> requestBody,
                                               @RequestParam String sebep,@RequestParam String izinTur,
                                               @RequestParam String tarihler) throws ParseException {
        System.out.println(tarihler);
        System.out.println("icerdyi<");
        System.out.println(token);
        String tokenWithoutBearer = token.substring(7);




        // Alınan token ile işlemlerinizi gerçekleştirin
        return ResponseEntity.ok(userService.izintelebi(tokenWithoutBearer,sebep,tarihler,izinTur));
    }
    @PostMapping("/findallrequesbycompanymanager")
    public ResponseEntity<List<Izintelebi>> findallrequestbycompanymanager(
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
        return ResponseEntity.ok(userService.findallreguestbycompanymanager(token));
    }
    @PostMapping("/deletebyadmin")
    public void deletebyadmin(
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
            System.out.println("burdayim");
            Long longSayi = (long) authorId; // int'i Long'a dönüştür

            userService.deletebyadmin(token, longSayi);


        }


    }
    @PostMapping("/deleterequestbycompanymanager")
    public void deleterequestbycompanymanager(
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
            System.out.println("burdayim");
            Long longSayi = (long) authorId; // int'i Long'a dönüştür

            userService.deleterequestbyadmin(token, longSayi);


        }


    }
    @PostMapping("/activerequestbycompanymanager")
    public void activerequestbycompanymanager(
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
            System.out.println("burdayim");
            Long longSayi = (long) authorId; // int'i Long'a dönüştür

            userService.activerequestbyadmin(token, longSayi);


        }


    }
    @PostMapping("/findalloldrequesbycompanymanager")
    public ResponseEntity<List<Izintelebi>> findalloldrequesbycompanymanager(
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
        return ResponseEntity.ok(userService.findalloldreguestbycompanymanager(token));
    }
    @PostMapping("/addEmployee")
    public ResponseEntity<Boolean> addEmployee(@RequestBody AddEmployeeCompanyDto dto,@RequestHeader(required = false) String authorization) throws ParseException {
        String token=null;
        if (authorization.startsWith("Bearer ")) {
            String tokenWithoutBearer = authorization.substring(7); // 7, "Bearer " prefix uzunluğudur
            System.out.println("İstek başlığından gelen token: " + tokenWithoutBearer);
            // tokenWithoutBearer artık Bearer prefix'inden arındırılmış token'ı içerir
            // tokenWithoutBearer değişkenini kullanarak işlemlerinizi gerçekleştirebilirsiniz
            token = tokenWithoutBearer;
        }
        System.out.println("burdayim");
        return ResponseEntity.ok(userService.addEmployee(token,dto));
}

    @DeleteMapping("deleteemployee/{id}")
    public ResponseEntity<String> deleteEmployee(@PathVariable Long id) {
        if (userService.deleteUserById(id)) {
            return ResponseEntity.ok( + id + " silindi");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Employee not found with ID " + id);
        }
    }

    @PutMapping("updateemployee/{id}")
    public ResponseEntity<UserProfile> updateUserProfile(@RequestParam Long Id, @RequestBody UserProfileUpdateDto dto){
        return ResponseEntity.ok(userService.updateprofileManager(Id,dto));
    }


}
