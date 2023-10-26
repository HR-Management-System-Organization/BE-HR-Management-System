package com.hrproject.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorType {

    INTERNAL_ERROR_SERVER(HttpStatus.INTERNAL_SERVER_ERROR, 5300, "Sunucu Hatası!"),

    BAD_REQUEST(HttpStatus.BAD_REQUEST, 4300, "Parametre Hatası!"),

    USER_NOT_FOUND(HttpStatus.NOT_FOUND, 4310, "Böyle Bir Kullanıcı Bulunamadı!"),

    ACCOUNT_NOT_ACTIVE(HttpStatus.BAD_REQUEST, 4311, "Hesabınız Aktif Değil!"),

    INVALID_CODE(HttpStatus.BAD_REQUEST, 4312, "Geçersiz Kod!"),

    UNEXPECTED_ERROR(HttpStatus.BAD_REQUEST, 4313, "Beklenmeyen Bir Hata OluŞtu!"),

    INVALID_TOKEN(HttpStatus.BAD_REQUEST, 4314, "Geçersiz Token!"),

    TOKEN_NOT_CREATED(HttpStatus.BAD_REQUEST, 4315, "Token Oluşturulamadı!"),

    USER_NOT_CREATED(HttpStatus.BAD_REQUEST, 4316, "Kullanıcı Oluşturulamadı!");


    HttpStatus httpStatus;

    private int code;

    private String message;
}