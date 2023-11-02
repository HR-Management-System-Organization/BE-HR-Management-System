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
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/personel-make-comment/{token}")
    public ResponseEntity<Comment> personnelMakeComment(@PathVariable String token, @RequestBody PersonnelCommentRequestDto dto){
        return ResponseEntity.ok(commentService.personnelMakeComment(token,dto));
    }

    @PutMapping("/change-comment-status/{token}")
    public ResponseEntity<Boolean> changeCommentStatus(@PathVariable String token, @RequestBody ChangeCommentStatusRequestDto dto){
        return ResponseEntity.ok(commentService.changeCommentStatus(token,dto));
    }


    @GetMapping("/pending-comments")
    public List<Comment> getPendingComments() {
        return commentService.findCommentByStatus();
    }
}
