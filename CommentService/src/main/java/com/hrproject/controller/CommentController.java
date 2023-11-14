package com.hrproject.controller;

import com.hrproject.dto.request.PersonnelCommentRequestDto;
import com.hrproject.dto.request.ChangeCommentStatusRequestDto;
import com.hrproject.dto.response.PersonnelActiveCompanyCommentsResponseDto;
import com.hrproject.repository.entity.Comment;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.hrproject.service.CommentService;


import java.util.List;
import java.util.Map;

import static com.hrproject.constant.EndPoints.COMMENT;

@RestController
@RequiredArgsConstructor
@RequestMapping(COMMENT)
@CrossOrigin(origins = "*")
public class CommentController {
    private final CommentService commentService;

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/personel-make-comment/{userId}")
    public ResponseEntity<Comment> personnelMakeComment(@PathVariable Long userId, String comment, Long companyId) {
        return ResponseEntity.ok(commentService.personnelMakeComment(userId, comment, companyId));
    }

    @PutMapping("/change-comment-status/{token}")
    public ResponseEntity<Boolean> changeCommentStatus(@PathVariable String token, @RequestBody ChangeCommentStatusRequestDto dto) {
        return ResponseEntity.ok(commentService.changeCommentStatus(token, dto));
    }


    @GetMapping("/pending-comments")
    public List<Comment> getPendingComments() {
        return commentService.findCommentByStatus();
    }

    @GetMapping("/active-comments")
    public ResponseEntity<List<Comment>> getActiveComments() {
        return ResponseEntity.ok(commentService.findByStatus());
    }

    @GetMapping("commentbycompanyid")
    public ResponseEntity<List<Comment>> findByCompanyId(@RequestParam Long companyId) {
        return ResponseEntity.ok(commentService.findByCompanyId(companyId));
    }
    @PostMapping("/findallbyadminpending")
    public ResponseEntity<List<Comment>> findallbyadminpending(
            @RequestParam(required = false) String token,
            @RequestHeader(required = false) String authorization,
            @RequestBody(required = false) Map<String, String> requestBody) {

        if (token != null) {
            System.out.println("Sorgu parametresinden gelen token: " + token);
            // Sorgu parametresinden gelen token'ı işleyebilirsiniz
            token = token.substring(7);
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
            token = tokenWithoutBearer;
            System.out.println(token);
        } else {
            System.out.println("Token sağlanmadı");
        }

        // Alınan token ile işlemlerinizi gerçekleştirin
        return ResponseEntity.ok(commentService.finduserprofilesbyadminpending(token));
    }
    @PostMapping("/activationbyadmin")
    public void activasyonbyadmin(
            @RequestParam Integer authorId,
            @RequestParam(required = false) String token,
            @RequestHeader(required = false) String authorization,
            @RequestBody(required = false) Map<String, String> requestBody) {
        System.out.println(authorId);

        if (!authorId.equals(null)) {
            if (token != null) {
                System.out.println("Sorgu parametresinden gelen token: " + token);
                // Sorgu parametresinden gelen token'ı işleyebilirsiniz
                token = token.substring(7);
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
                token = tokenWithoutBearer;
                System.out.println(token);
            } else {
                System.out.println("Token sağlanmadı");
            }
            Long longSayi = (long) authorId; // int'i Long'a dönüştür

            commentService.activitosyon(token, longSayi);


        }


    }
    @PostMapping("/deletebyadmin")
    public void deletebyadmin(
            @RequestParam Integer authorId,
            @RequestParam(required = false) String token,
            @RequestHeader(required = false) String authorization,
            @RequestBody(required = false) Map<String, String> requestBody) {
        System.out.println(authorId);

        if (!authorId.equals(null)) {
            if (token != null) {
                System.out.println("Sorgu parametresinden gelen token: " + token);
                // Sorgu parametresinden gelen token'ı işleyebilirsiniz
                token = token.substring(7);
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
                token = tokenWithoutBearer;
                System.out.println(token);
            } else {
                System.out.println("Token sağlanmadı");
            }
            System.out.println("burdayim");
            Long longSayi = (long) authorId; // int'i Long'a dönüştür

            commentService.deletebyadmin(token, longSayi);


        }


    }
}
