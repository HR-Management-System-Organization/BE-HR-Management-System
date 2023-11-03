package com.hrproject.dto.request;

import com.hrproject.repository.enums.EGender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddEmployeeDto {
    private Long companyId;
    private String token;
    private String name;
    private String userName;
    private String password;
    private String address;
    private String surName;
    private String email;
    private String phone;
    private String avatar;
    private String about;
    private LocalDate birthDate;
    private EGender gender;
}
