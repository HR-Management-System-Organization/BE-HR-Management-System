package com.hrproject.service;

import org.springframework.stereotype.Service;

@Service
public class Htmlmail {
    public String html(String link){
        String html="<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>Aktivasyon Maili</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <p>Mesaj</p>\n" +
                "    <p>Onay bağlantısı: <a href=\"http://localhost:7071/api/v1/auth/activation?token=${"+link+"}\">Buraya tıklayarak onaylayabilirsiniz</a></p>\n" +
                "    \n" +
                "    \n" +
                "    <img src=\"URL_TO_IMAGE\" alt=\"Onay Görseli\" style=\"max-width: 100%; height: auto;\">\n" +
                "\n" +
                "    \n" +
                "    <a href=\"\" style=\"display: inline-block; padding: 10px 20px; background-color: aqua; color: white; text-decoration: none;\">\n" +
                "        Hesabını Aktive Et\n" +
                "    </a>\n" +
                "\n" +
                "    <p>Teşekkürler,</p>\n" +
                "    <p>İmzanız veya şirket adınız</p>\n" +
                "</body>\n" +
                "</html>";

        return html;
    }
}
