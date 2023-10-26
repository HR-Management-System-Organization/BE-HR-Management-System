package com.hrproject.dto.request;


import com.hrproject.repository.enums.EGender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class RegisterRequestDto {
    @NotBlank(message = "Email adı boş geçilemez!!!")
    @Email
    private String email;
    @NotBlank(message = "Kullanıcı adı boş geçilemez!!!")
    private String username;
    private EGender gender;
    @NotBlank(message = "Şifre adı boş geçilemez!!!")
    @Size(min = 5, max = 32, message = "Şifre uzunlugu en az 5 karakter en fazla 32 karakter olabilir !!!")
    @NotBlank(message = "Şifre boş geçilemez!!!")
    @Size(min = 8, max = 32, message = "Şifre uzunlugu en az 8 karakter en fazla 32 karakter olabilir !!!")
    private String password;
    @NotBlank(message = "Şifre boş geçilemez!!!")
    private String rePassword;
    private String taxNumber;
    private String companyName;


}