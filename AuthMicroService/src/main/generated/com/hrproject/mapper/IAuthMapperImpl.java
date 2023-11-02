package com.hrproject.mapper;

import com.hrproject.dto.request.RegisterGuestRequestDto;
import com.hrproject.dto.request.RegisterRequestDto;
import com.hrproject.dto.request.UserSaveRequestDto;
import com.hrproject.dto.response.RegisterResponseDto;
import com.hrproject.rabbitmq.model.MailModel;
import com.hrproject.rabbitmq.model.RegisterModel;
import com.hrproject.repository.entity.Auth;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-11-02T09:29:38+0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.9 (Oracle Corporation)"
)
@Component
public class IAuthMapperImpl implements IAuthMapper {

    @Override
    public Auth toAuth(RegisterRequestDto dto) {
        if ( dto == null ) {
            return null;
        }

        Auth.AuthBuilder<?, ?> auth = Auth.builder();

        auth.username( dto.getUsername() );
        auth.password( dto.getPassword() );
        auth.taxNumber( dto.getTaxNumber() );
        auth.email( dto.getEmail() );
        auth.companyName( dto.getCompanyName() );
        auth.gender( dto.getGender() );

        return auth.build();
    }

    @Override
    public RegisterResponseDto toRegisterResponseDto(Auth auth) {
        if ( auth == null ) {
            return null;
        }

        RegisterResponseDto.RegisterResponseDtoBuilder registerResponseDto = RegisterResponseDto.builder();

        registerResponseDto.id( auth.getId() );
        registerResponseDto.activationCode( auth.getActivationCode() );
        registerResponseDto.username( auth.getUsername() );

        return registerResponseDto.build();
    }

    @Override
    public Auth toAuth(RegisterGuestRequestDto dto) {
        if ( dto == null ) {
            return null;
        }

        Auth.AuthBuilder<?, ?> auth = Auth.builder();

        auth.username( dto.getUsername() );
        auth.password( dto.getPassword() );
        auth.email( dto.getEmail() );
        auth.gender( dto.getGender() );

        return auth.build();
    }

    @Override
    public UserSaveRequestDto toUserSaveRequestDto(Auth auth) {
        if ( auth == null ) {
            return null;
        }

        UserSaveRequestDto.UserSaveRequestDtoBuilder userSaveRequestDto = UserSaveRequestDto.builder();

        userSaveRequestDto.authId( auth.getId() );
        userSaveRequestDto.username( auth.getUsername() );
        userSaveRequestDto.email( auth.getEmail() );

        return userSaveRequestDto.build();
    }

    @Override
    public RegisterModel toRegisterModel(Auth auth) {
        if ( auth == null ) {
            return null;
        }

        RegisterModel.RegisterModelBuilder registerModel = RegisterModel.builder();

        registerModel.authId( auth.getId() );
        registerModel.username( auth.getUsername() );
        registerModel.password( auth.getPassword() );
        registerModel.email( auth.getEmail() );
        registerModel.gender( auth.getGender() );
        registerModel.role( auth.getRole() );
        registerModel.status( auth.getStatus() );

        return registerModel.build();
    }

    @Override
    public MailModel toMailModel(Auth auth) {
        if ( auth == null ) {
            return null;
        }

        MailModel.MailModelBuilder mailModel = MailModel.builder();

        mailModel.email( auth.getEmail() );

        return mailModel.build();
    }
}
