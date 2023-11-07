package com.hrproject.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfileUpdateDto {

    @NotBlank(message = "Username Boş Geçilemez!")
    private String username;

    @Email
    private String email;

    private String phone;

    private String address;

    private String avatar;

    private String about;

    private String name;

    private String surName;
}