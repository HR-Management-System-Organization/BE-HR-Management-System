package com.hrproject.utility;

import org.springframework.stereotype.Service;

import java.security.SecureRandom;
@Service
public class PasswordGenerator {

    private static final String CHAR_LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String CHAR_UPPER = CHAR_LOWER.toUpperCase();
    private static final String NUMBER = "0123456789";
    private static final String OTHER_CHAR = "!@#$%&*()_+-=[]{}|;':,.<>?";

    private static final String PASSWORD_ALLOW_BASE = CHAR_LOWER + CHAR_UPPER + NUMBER + OTHER_CHAR;

    private static SecureRandom random = new SecureRandom();



    public static String generatePassword(int length) {
        if (length < 1) {
            throw new IllegalArgumentException("Uzunluk en az 1 olmalıdır.");
        }

        StringBuilder password = new StringBuilder(length);

        // İlk karakteri büyük harf yap
        password.append(CHAR_UPPER.charAt(random.nextInt(CHAR_UPPER.length())));

        // Geri kalan karakterleri rastgele seç
        for (int i = 1; i < length; i++) {
            password.append(PASSWORD_ALLOW_BASE.charAt(random.nextInt(PASSWORD_ALLOW_BASE.length())));
        }

        return password.toString();
    }
}