package com.hrproject.rabbitmq.model;

import com.hrproject.repository.enums.ERole;
import com.hrproject.repository.enums.EStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterModel implements Serializable {
    private Long authId;
    private String username;
    private String email;


    private ERole role = ERole.GUEST;

    private EStatus status = EStatus.PENDING;
}
