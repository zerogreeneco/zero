package com.zerogreen.zerogreeneco.service.mail;

public interface MailService {
    void sendAuthMail(String mail, String key);

    String createAuthKey();

    void sendTempPassword(String mail, String phoneNumber);
}
