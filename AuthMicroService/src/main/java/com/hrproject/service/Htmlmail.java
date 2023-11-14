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
                "    <style>\n" +
                "        body {\n" +
                "            font-family: 'Arial', sans-serif;\n" +
                "            background-color: #f4f4f4;\n" +
                "            text-align: center;\n" +
                "            margin: 0;\n" +
                "            padding: 20px;\n" +
                "        }\n" +
                "        .container {\n" +
                "            max-width: 600px;\n" +
                "            margin: 0 auto;\n" +
                "            background-color: #ffffff;\n" +
                "            padding: 20px;\n" +
                "            border-radius: 8px;\n" +
                "            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);\n" +
                "        }\n" +
                "        h1 {\n" +
                "            color: #1f00ff;\n" +
                "        }\n" +
                "        p {\n" +
                "            font-size: 16px;\n" +
                "            color: #555555;\n" +
                "            margin-bottom: 30px;\n" +
                "        }\n" +
                "        .activation-btn {\n" +
                "            display: inline-block;\n" +
                "            padding: 15px 30px;\n" +
                "            background-color: #1f00ff;\n" +
                "            color: #ffffff;\n" +
                "            text-decoration: none;\n" +
                "            border-radius: 5px;\n" +
                "            font-weight: bold;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div class=\"container\">\n" +
                "        <h1>Hoş Geldiniz!</h1>\n" +
                "        <p>Aramıza katıldığınız için teşekkür ederiz. Hesabınızı aktive etmek için aşağıdaki butona tıklayın.</p>\n" +
                "        <a href=\"http://localhost:7071/api/v1/auth/activation?token=${"+link+"}\" class=\"activation-btn\">Hesabını Aktive Et</a>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>\n";

        return html;
    }
}
