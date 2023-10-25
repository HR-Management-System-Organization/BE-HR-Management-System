package com.hrproject.dto.request;

import com.hrproject.repository.enums.ERole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSaveRequestDto {

    private Long authId;

    private String username;

    private String email;

    private String password;

    private ERole role;
}