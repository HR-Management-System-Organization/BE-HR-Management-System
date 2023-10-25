package com.hrproject.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Getter
@AllArgsConstructor
public enum ErrorType {

    INTERNAL_ERROR_SERVER(HttpStatus.INTERNAL_SERVER_ERROR, 5200, "Sunucu Hatası!"),

    BAD_REQUEST(HttpStatus.BAD_REQUEST, 4200, "Parametre Hatası!"),

    USER_NOT_FOUND(HttpStatus.NOT_FOUND, 4210, "Böyle Bir Kullanıcı Bulunamadı!"),

    ACCOUNT_NOT_ACTIVE(HttpStatus.BAD_REQUEST, 4211, "Hesabınız Aktif Değildir!"),

    INVALID_CODE(HttpStatus.BAD_REQUEST, 4212, "Geçersiz Kod!"),

    UNEXPECTED_ERROR(HttpStatus.BAD_REQUEST, 4213, "Beklenmeyen Bir Hata Oluştu!"),
    DOLOGIN_USERNAMEORPASSWORD_NOTEXISTS(HttpStatus.BAD_REQUEST,1006,"Kullanıcı adı veya şifre hatalı."),
    NO_PERMISION(HttpStatus.BAD_REQUEST, 4317, "Rolun bu istek icin yetersiz"),

    INVALID_TOKEN(HttpStatus.BAD_REQUEST, 4214, "Geçersiz Token!"),

    TOKEN_NOT_CREATED(HttpStatus.BAD_REQUEST, 4215, "Token Oluşturulamadı!"),

    USER_NOT_CREATED(HttpStatus.BAD_REQUEST, 4216, "Kullanıcı Oluşturulamadı!"),

    STATUS_NOT_FOUND(HttpStatus.BAD_REQUEST, 4217, "Böyle Bir Kullanıcı Durumu Bulunamadı!");



    HttpStatus httpStatus;

    private int code;

    private String message;
}