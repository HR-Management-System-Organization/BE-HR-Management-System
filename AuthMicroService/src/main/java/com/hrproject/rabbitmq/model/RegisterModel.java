package com.hrproject.rabbitmq.model;

import com.hrproject.repository.enums.EGender;
import com.hrproject.repository.enums.ERole;
import com.hrproject.repository.enums.EStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterModel implements Serializable {

    private Long authId;

    private String username;

    private String  password;

    private String email;

    private EGender gender;

    private ERole role = ERole.GUEST;

    private EStatus status = EStatus.PENDING;
    private LocalDate activationDate;
    private String companyName;
}