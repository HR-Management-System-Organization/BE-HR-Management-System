package com.hrproject.mapper;

import com.hrproject.dto.response.FindCompanyCommentsResponseDto;
import com.hrproject.dto.response.UserProfileCommentResponseDto;
import com.hrproject.repository.entity.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ICommentMapper {
    ICommentMapper INSTANCE = Mappers.getMapper(ICommentMapper.class);
    Comment fromUserProfileCommentResponseDtoToComment(final UserProfileCommentResponseDto dto);
    FindCompanyCommentsResponseDto fromCompanyToFindCompanyCommentsResponseDto(final Comment comment);

}
