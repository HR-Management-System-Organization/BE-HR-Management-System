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
public class RegisterGuestRequestDto {

    @NotBlank(message = "Kullanıcı Adı Boş Geçilemez!")
    private String username;

    @NotBlank(message = "E-mail Boş Geçilemez!")
    @Email
    private String email;

    private EGender gender;

    @NotBlank(message = "Şifre Boş Geçilemez!")
    @Size(min = 8, max = 32, message = "Şifre Uzunluğu En Az 8 Karakter, En Fazla 32 Karakter Olabilir!")
    private String password;

    @NotBlank(message = "Şifre Boş Geçilemez!")
    private String rePassword;

}