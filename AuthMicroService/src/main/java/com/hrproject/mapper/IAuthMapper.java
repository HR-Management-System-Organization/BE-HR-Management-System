package com.hrproject.mapper;


import com.hrproject.dto.request.RegisterGuestRequestDto;
import com.hrproject.dto.request.RegisterRequestDto;
import com.hrproject.dto.request.UserSaveRequestDto;
import com.hrproject.dto.response.RegisterResponseDto;
import com.hrproject.rabbitmq.model.MailModel;
import com.hrproject.rabbitmq.model.RegisterModel;
import com.hrproject.repository.entity.Auth;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IAuthMapper {//////?

    IAuthMapper INSTANCE = Mappers.getMapper(IAuthMapper.class);
    Auth toAuth(RegisterRequestDto dto);
    RegisterResponseDto toRegisterResponseDto(Auth auth);
    Auth toAuth(RegisterGuestRequestDto dto);

    @Mapping(source = "id",target = "authId")
    UserSaveRequestDto toUserSaveRequestDto(Auth auth);

    @Mapping(source = "id",target = "authId")
    RegisterModel toRegisterModel(Auth auth);

    MailModel toMailModel(Auth auth);
}
