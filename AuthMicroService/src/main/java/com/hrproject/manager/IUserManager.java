package com.hrproject.manager;

import com.hrproject.constant.EndPoints;
import com.hrproject.dto.request.UserSaveRequestDto;
//import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.hrproject.constant.EndPoints.*;

//@FeignClient(url = "http://localhost:7072/api/v1/user",decode404 = true,name = "auth-userprofile")  //interface proxy
public interface IUserManager {

    @PostMapping(EndPoints.SAVE)
   ResponseEntity<Boolean> save(@RequestBody UserSaveRequestDto dto,@RequestHeader("Authorization") String token);

    @PostMapping(EndPoints.ACTIVATE_STATUS)
    ResponseEntity<String> activateStatus(@RequestParam String token);

    @DeleteMapping(EndPoints.DELETE_BY_ID)
    public ResponseEntity<Long> deleteById(@RequestHeader("Authorization") String token);

}
