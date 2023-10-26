package com.hrproject.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorType {

    INTERNAL_ERROR_SERVER(HttpStatus.INTERNAL_SERVER_ERROR, 5100, "Sunucu Hatası!"),

    BAD_REQUEST(HttpStatus.BAD_REQUEST, 4100, "Parametre Hatası!"),

    USER_NOT_FOUND(HttpStatus.NOT_FOUND, 4110, "Böyle Bir Kullanıcı Bulunamadı!"),

    ACCOUNT_NOT_ACTIVE(HttpStatus.BAD_REQUEST, 4111, "Hesabınız Aktif Değil!"),

    INVALID_CODE(HttpStatus.BAD_REQUEST, 4112, "Geçersiz Kod!"),

    ALREADY_ACTIVE(HttpStatus.BAD_REQUEST, 4113, "Hesabınız Zaten Aktif!"),

    UNEXPECTED_ERROR(HttpStatus.BAD_REQUEST, 4114, "Beklenmeyen Bir Hata Oluştu!"),

    USERNAME_ALREADY_EXIST(HttpStatus.BAD_REQUEST, 4115, "Böyle bir Kullanıcı Adı Bulunmaktadır!"),

    DATA_INTEGRITY(HttpStatus.BAD_REQUEST, 4116, "Hatalı Veri!"),

    LOGIN_ERROR(HttpStatus.BAD_REQUEST, 4117, "Kullanıcı Adı Veya Şifre Hatalı!"),

    INVALID_TOKEN(HttpStatus.BAD_REQUEST, 4118, "Geçersiz Token!"),

    TOKEN_NOT_CREATED(HttpStatus.BAD_REQUEST, 4119, "Token Oluşturulamadı!"),

    INVALID_ACTION(HttpStatus.BAD_REQUEST, 4900, "Kullanıcı İstenilen Statüye Geçirilemedi!"),

    PASSWORD_DUPLICATE(HttpStatus.BAD_REQUEST, 5000, "Şifre Son Kullanılan Şifreyle Aynıdır!"),

    COMPANY_NOT_FOUND(HttpStatus.NOT_FOUND, 5010, "Böyle Bir Şirket Bulunamadı!"),

    DUPLICATE_USER(HttpStatus.BAD_REQUEST, 5200, "Bu Kullanıcı Zaten Kayıtlı!");


    HttpStatus httpStatus;

    private int code;

    private String message;
}