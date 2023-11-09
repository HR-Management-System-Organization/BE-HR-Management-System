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
}
