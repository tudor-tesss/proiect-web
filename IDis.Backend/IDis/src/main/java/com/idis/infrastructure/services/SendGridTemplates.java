package com.idis.infrastructure.services;

public class SendGridTemplates {
    public static String userGateTemplate(String firstName, String code) {
        return String.format("Hello %s, your code is %s", firstName, code);
    }
}
