package com.hrproject.mapper;

import com.hrproject.dto.response.FindCompanyCommentsResponseDto;
import com.hrproject.dto.response.UserProfileCommentResponseDto;
import com.hrproject.repository.entity.Comment;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-11-09T14:36:10+0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.9 (Amazon.com Inc.)"
)
@Component
public class ICommentMapperImpl implements ICommentMapper {

    @Override
    public Comment fromUserProfileCommentResponseDtoToComment(UserProfileCommentResponseDto dto) {
        if ( dto == null ) {
            return null;
        }

        Comment.CommentBuilder<?, ?> comment = Comment.builder();

        comment.userId( dto.getUserId() );
        comment.companyId( dto.getCompanyId() );

        return comment.build();
    }

    @Override
    public FindCompanyCommentsResponseDto fromCompanyToFindCompanyCommentsResponseDto(Comment comment) {
        if ( comment == null ) {
            return null;
        }

        FindCompanyCommentsResponseDto.FindCompanyCommentsResponseDtoBuilder findCompanyCommentsResponseDto = FindCompanyCommentsResponseDto.builder();

        findCompanyCommentsResponseDto.commentId( comment.getCommentId() );
        findCompanyCommentsResponseDto.comment( comment.getComment() );
        findCompanyCommentsResponseDto.companyId( comment.getCompanyId() );

        return findCompanyCommentsResponseDto.build();
    }
}
