package com.hrproject.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorType {

    INTERNAL_ERROR_SERVER(5100,"Sunucu Hatası",HttpStatus.INTERNAL_SERVER_ERROR),
    BAD_REQUEST(4100,"Parametre hatası",HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND(4110, "Böyle bir kullanıcı bulunamadı",HttpStatus.NOT_FOUND),
    ACCOUNT_NOT_ACTIVE(4111, "Hesabınız Aktif değildir",HttpStatus.BAD_REQUEST),
    INVALID_CODE(4112, "Geçersiz Kod",HttpStatus.BAD_REQUEST),
    ALREADY_ACTIVE(4113, "Hesabınız zaten aktif",HttpStatus.BAD_REQUEST),
    UNEXPECTED_ERROR(4114,"Beklenmeyen bir hata olustu",HttpStatus.BAD_REQUEST),
    USERNAME_ALREADY_EXIST(4115,"Böyle bir kullanıcı adı bulunmaktadır !!!",HttpStatus.BAD_REQUEST),
    DATA_INTEGRITY(4116, "hatalı veri",HttpStatus.BAD_REQUEST),
    LOGIN_ERROR(4117, "Kullanıcı adı veya şifre hatalı!!!",HttpStatus.BAD_REQUEST),
    INVALID_TOKEN(4118, "Geçersiz Token !!!",HttpStatus.BAD_REQUEST),
    TOKEN_NOT_CREATED(4119, "Token Oluşturulamadı !!!",HttpStatus.BAD_REQUEST),
    INVALID_ACTION(4900,"Kullanıcı istenilen statüye geçirilemedi.",HttpStatus.BAD_REQUEST),
    PASSWORD_DUPLICATE(5000,"Şifre son kullanılan şifreyle aynıdır.",HttpStatus.BAD_REQUEST),
    COMPANY_NOT_FOUND(5010, "Böyle bir şirket bulunamadı", HttpStatus.NOT_FOUND),
    DUPLICATE_USER(5200, "Bu kullanıcı zaten kayıtlı", HttpStatus.BAD_REQUEST),


    ;


    private int code;
    private String message;
    HttpStatus httpStatus;


}
