package com.hrproject.mapper;

import com.hrproject.dto.request.AddEmployeeCompanyDto;
import com.hrproject.dto.request.UserProfileUpdateRequestDto;
import com.hrproject.dto.request.UserSaveRequestDto;
import com.hrproject.dto.response.UserProfileFindAllResponseDto;
import com.hrproject.rabbitmq.model.RegisterElasticModel;
import com.hrproject.rabbitmq.model.RegisterModel;
import com.hrproject.repository.entity.UserProfile;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-11-06T17:05:08+0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.9 (Amazon.com Inc.)"
)
@Component
public class IUserMapperImpl implements IUserMapper {

    @Override
    public UserProfile toUserProfile(UserSaveRequestDto dto) {
        if ( dto == null ) {
            return null;
        }

        UserProfile.UserProfileBuilder<?, ?> userProfile = UserProfile.builder();

        userProfile.authId( dto.getAuthId() );
        userProfile.username( dto.getUsername() );
        userProfile.email( dto.getEmail() );
        userProfile.password( dto.getPassword() );
        userProfile.role( dto.getRole() );

        return userProfile.build();
    }

    @Override
    public UserProfile toUserProfile(RegisterModel model) {
        if ( model == null ) {
            return null;
        }

        UserProfile.UserProfileBuilder<?, ?> userProfile = UserProfile.builder();

        userProfile.authId( model.getAuthId() );
        userProfile.username( model.getUsername() );
        userProfile.email( model.getEmail() );
        userProfile.password( model.getPassword() );
        userProfile.role( model.getRole() );
        userProfile.gender( model.getGender() );
        userProfile.status( model.getStatus() );

        return userProfile.build();
    }

    @Override
    public UserProfile toUserProfile(AddEmployeeCompanyDto model) {
        if ( model == null ) {
            return null;
        }

        UserProfile.UserProfileBuilder<?, ?> userProfile = UserProfile.builder();

        userProfile.username( model.getUsername() );
        userProfile.password( model.getPassword() );
        userProfile.phone( model.getPhone() );
        userProfile.address( model.getAddress() );
        userProfile.avatar( model.getAvatar() );
        userProfile.name( model.getName() );

        return userProfile.build();
    }

    @Override
    public UserProfile toUserProfile(UserProfileUpdateRequestDto dto) {
        if ( dto == null ) {
            return null;
        }

        UserProfile.UserProfileBuilder<?, ?> userProfile = UserProfile.builder();

        userProfile.username( dto.getUsername() );
        userProfile.email( dto.getEmail() );
        userProfile.phone( dto.getPhone() );
        userProfile.address( dto.getAddress() );
        userProfile.avatar( dto.getAvatar() );
        userProfile.about( dto.getAbout() );
        userProfile.name( dto.getName() );
        userProfile.surName( dto.getSurName() );

        return userProfile.build();
    }

    @Override
    public UserProfileFindAllResponseDto toUserProfileFindAllResponseDto(UserProfile userProfile) {
        if ( userProfile == null ) {
            return null;
        }

        UserProfileFindAllResponseDto.UserProfileFindAllResponseDtoBuilder userProfileFindAllResponseDto = UserProfileFindAllResponseDto.builder();

        if ( userProfile.getId() != null ) {
            userProfileFindAllResponseDto.id( String.valueOf( userProfile.getId() ) );
        }
        userProfileFindAllResponseDto.authId( userProfile.getAuthId() );
        userProfileFindAllResponseDto.username( userProfile.getUsername() );
        userProfileFindAllResponseDto.email( userProfile.getEmail() );
        userProfileFindAllResponseDto.phone( userProfile.getPhone() );
        userProfileFindAllResponseDto.address( userProfile.getAddress() );
        userProfileFindAllResponseDto.avatar( userProfile.getAvatar() );
        userProfileFindAllResponseDto.about( userProfile.getAbout() );
        userProfileFindAllResponseDto.name( userProfile.getName() );
        userProfileFindAllResponseDto.surName( userProfile.getSurName() );
        userProfileFindAllResponseDto.birthDate( userProfile.getBirthDate() );
        userProfileFindAllResponseDto.status( userProfile.getStatus() );

        return userProfileFindAllResponseDto.build();
    }

    @Override
    public RegisterElasticModel toRegisterElasticModel(UserProfile userProfile) {
        if ( userProfile == null ) {
            return null;
        }

        RegisterElasticModel.RegisterElasticModelBuilder registerElasticModel = RegisterElasticModel.builder();

        if ( userProfile.getId() != null ) {
            registerElasticModel.id( String.valueOf( userProfile.getId() ) );
        }
        registerElasticModel.authId( userProfile.getAuthId() );
        registerElasticModel.username( userProfile.getUsername() );
        registerElasticModel.email( userProfile.getEmail() );

        return registerElasticModel.build();
    }
}
