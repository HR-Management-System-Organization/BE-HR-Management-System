package com.hrproject.mapper;

import com.hrproject.dto.request.UserProfileUpdateRequestDto;
import com.hrproject.dto.request.UserSaveRequestDto;
import com.hrproject.dto.response.UserProfileFindAllResponseDto;
import com.hrproject.rabbitmq.model.RegisterElasticModel;
import com.hrproject.rabbitmq.model.RegisterModel;
import com.hrproject.repository.entity.UserProfile;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IUserMapper {

    IUserMapper INSTANCE = Mappers.getMapper(IUserMapper.class);

    UserProfile toUserProfile(UserSaveRequestDto dto);

    UserProfile toUserProfile(RegisterModel model);

    UserProfile toUserProfile(UserProfileUpdateRequestDto dto);

    // @Mapping(source = "id", target = "userProfileId")
    UserProfileFindAllResponseDto toUserProfileFindAllResponseDto(UserProfile userProfile);

    RegisterElasticModel toRegisterElasticModel(UserProfile userProfile);
}