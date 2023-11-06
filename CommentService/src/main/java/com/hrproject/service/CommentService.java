package com.hrproject.service;


import com.hrproject.dto.request.ChangeCommentStatusRequestDto;
import com.hrproject.dto.request.PersonnelCommentRequestDto;
import com.hrproject.dto.response.FindCompanyCommentsResponseDto;
import com.hrproject.dto.response.PersonnelActiveCompanyCommentsResponseDto;
import com.hrproject.dto.response.UserProfileCommentResponseDto;
import com.hrproject.exception.CommentException;
import com.hrproject.exception.ErrorType;
import com.hrproject.mapper.ICommentMapper;
import com.hrproject.rabbitmq.model.CreateCommentModel;
import com.hrproject.rabbitmq.producer.CreateCommentProducer;
import com.hrproject.repository.ICommentRepository;
import com.hrproject.repository.entity.Comment;
import com.hrproject.repository.enums.ECommentStatus;
import com.hrproject.repository.enums.ERole;
import com.hrproject.utility.JwtTokenProvider;
import com.hrproject.utility.ServiceManager;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService extends ServiceManager<Comment, Long> {
    private final ICommentRepository commentRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final CreateCommentProducer createCommentProducer;

    public CommentService(ICommentRepository commentRepository, JwtTokenProvider jwtTokenProvider, CreateCommentProducer createCommentProducer) {
        super(commentRepository);
        this.commentRepository = commentRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.createCommentProducer = createCommentProducer;
    }

    public List<FindCompanyCommentsResponseDto> findCompanyComments(Long companyId) {
        List<Comment> commentList = commentRepository.findByCompanyId(companyId);
        List<FindCompanyCommentsResponseDto> companyComments = commentList.stream().filter(y ->
                y.getECommentStatus() == ECommentStatus.ACTIVE).map(x ->
                ICommentMapper.INSTANCE.fromCompanyToFindCompanyCommentsResponseDto(x)).collect(Collectors.toList());
        return companyComments;
    }

    public Boolean changeCommentStatus(String token, ChangeCommentStatusRequestDto dto) {
        List<String> roles = jwtTokenProvider.getRoleFromToken(token);
        if (roles.isEmpty())
            throw new CommentException(ErrorType.INVALID_TOKEN);
        if (roles.contains(ERole.ADMIN.toString())) {
            Comment commentId = findById(dto.getCommentId()).orElseThrow(() -> {
                throw new CommentException(ErrorType.COMMENT_NOT_FOUND);
            });
            if (commentId.getECommentStatus() == ECommentStatus.PENDING) {
                if (dto.getAction()) {
                    commentId.setECommentStatus(ECommentStatus.ACTIVE);
                } else {
                    commentId.setECommentStatus(ECommentStatus.DELETED);
                }
                update(commentId);
                return true;
            }
            throw new CommentException(ErrorType.COMMENT_NOT_PENDING);
        }
        throw new CommentException(ErrorType.NO_AUTHORIZATION);
    }


    public Comment personnelMakeComment(Long userId, String comment) {
        Comment yorum = Comment.builder()
                .comment(comment)
                .userId(userId)
                .build();

        return save(yorum);
    }

    public List<Comment> findCommentByStatus() {
        List<Comment> commentList = commentRepository.findAll();
        List<Comment> pendingComment = new ArrayList<>();
        commentList.forEach(x -> {
            if (x.getECommentStatus() == ECommentStatus.PENDING) {
                pendingComment.add(x);
            }
        });
        return pendingComment;
    }

    public List<Comment> findByStatus() {
        List<Comment> activeList = findAll().stream().filter(x -> x.getECommentStatus().equals(ECommentStatus.ACTIVE)).toList();

        return activeList;
    }
//    public List<PersonnelActiveCompanyCommentsResponseDto> findAllActiveCompanyComments(String token){
//        Long authId = jwtTokenProvider.getIdFromToken(token).orElseThrow(()->{throw new CommentException(ErrorType.USER_NOT_FOUND);});
//        List<String> roles = jwtTokenProvider.getRoleFromToken(token);
//        if(roles.isEmpty())
//            throw new CommentException(ErrorType.USER_NOT_FOUND);
//        if(roles.contains(ERole.PERSONEL.toString())) {
//            PersonnelDashboardCommentResponseDto dto = userManager.findAllActiveCompanyComments(authId).getBody();
//            List<Comment> commentList = commentRepository.findByCompanyId(dto.getCompanyId());
//            System.out.println(commentList);
//            List<Comment> filteredComments= new ArrayList<>();
//            commentList.stream().forEach(x -> {
//                if(x.getECommentStatus() == ECommentStatus.ACTIVE)
//                    filteredComments.add(x);
//            });
//            List<PersonnelActiveCompanyCommentsResponseDto> activeCompanyCommentsResponseDtos =
//                    filteredComments.stream().map(comment -> {
//                        PersonnelActiveCompanyCommentsResponseDto dto1 = ICommentMapper.INSTANCE.fromCommentToPersonnelActiveCompanyCommentsResponseDto(comment);
//                        String avatar = String.valueOf(userManager.getUserAvatarByUserId(comment.getUserId()).getBody());
//                        dto1.setAvatar(avatar);
//                        return dto1;
//                    }).collect(Collectors.toList());
//            System.out.println(activeCompanyCommentsResponseDtos);
//            return activeCompanyCommentsResponseDtos;
//        }
//        throw new CommentException(ErrorType.NO_AUTHORIZATION);
//    }
}
